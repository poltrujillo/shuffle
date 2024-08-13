using Newtonsoft.Json;
using ShuffleConsole;
using System;
using System.IO;
using System.Net.Sockets;
using System.Text;
using System.Threading;

class TCPClient
{
    static bool close = false;
    static bool menuloop = true;
    static bool gameplay = false;
    static void Main(string[] args)
    {
        // Set the server IP address and port number
        string serverIP = "192.168.195.40";
        int port = 8888;

        try
        {
            // Establish a connection with the server
            TcpClient client = new TcpClient(serverIP, port);
            Console.WriteLine("Connected to the server.");
            NetworkStream stream = client.GetStream();

            Thread loop = new Thread(() => GameLoop(stream));
            //Thread receiveThread = new Thread(() => ReadFromServer(stream));
            //receiveThread.Start();
            loop.Start();

            loop.Join();
           // receiveThread.Join();
            // Close the connection
            client.Close();
        }
        catch (Exception e)
        {
            Console.WriteLine(e.ToString());
        }

        Console.WriteLine("\nPress ENTER to continue...");
        Console.Read();
    }

    static void GameLoop(NetworkStream stream)
    {
        string message;
        Message send;
        do
        {
            // pestaña Selecionar Crear o unirse
            Console.WriteLine("Escribe Unirte para unirse a una partida o Crear para crear una sala");
            message = Console.ReadLine();
            if (message.Equals("Crear"))
            {
                Console.WriteLine("Preparando");
                // Crear la instancia de Message y asignar el valor
                send = new RoomCreateMessage();
                send.type = "crear";
                byte[] buffer;
                if (send is RoomCreateMessage roomCreateMessage)
                {
                    roomCreateMessage.PlaylistID = "asnfasklfn";
                    roomCreateMessage.Usuario = "cliente";
                    // Convertir el objeto a JSON
                    string json = JsonConvert.SerializeObject(roomCreateMessage);

                    // Convertir el JSON a bytes
                    buffer = Encoding.UTF8.GetBytes(json);

                    // Enviar los bytes al servidor
                    Console.WriteLine("Enviado");
                    stream.Write(buffer, 0, buffer.Length);
                }
                buffer = new byte[1024];
                int bytesRead = stream.Read(buffer, 0, buffer.Length);
                string jsonData = Encoding.UTF8.GetString(buffer, 0, bytesRead);
                RoomHasBeenCreateMessage receivedMessage = JsonConvert.DeserializeObject<RoomHasBeenCreateMessage>(jsonData);
                Console.WriteLine("La sala ha sido creada con el codigo " + receivedMessage.RoomID + " La playlist es: " + "asnfasklfn");
                Console.Read();



            } else if ( message.Equals("Unirse"))
            {
                Console.WriteLine("Escribe el codigo para unirse");
                string code = Console.ReadLine();
                send = new RoomJoinMessage();
                send.type = "join";
                byte[] buffer;
                if (send is RoomJoinMessage roomJoinMessage)
                {
                    roomJoinMessage.Usuario = "unido";
                    roomJoinMessage.RoomId = code;
                    // Convertir el objeto a JSON
                    string json = JsonConvert.SerializeObject(roomJoinMessage);

                    // Convertir el JSON a bytes
                    buffer = Encoding.UTF8.GetBytes(json);

                    // Enviar los bytes al servidor
                    Console.WriteLine("Enviado");
                    stream.Write(buffer, 0, buffer.Length);
                    buffer = new byte[1024];
                    int bytesRead = stream.Read(buffer, 0, buffer.Length);
                    string jsonData = Encoding.UTF8.GetString(buffer, 0, bytesRead);
                    RoomHasBeenJoinedMessage receivedMessage = JsonConvert.DeserializeObject<RoomHasBeenJoinedMessage>(jsonData);
                    Console.WriteLine("Te has unido a la sala con el codigo " + receivedMessage.RoomID + " La playlist es: " + receivedMessage.RoomID);
                    Console.WriteLine("You are playing with");
                    for (int i = 0; i < receivedMessage.Participants.Count; i++)
                    {
                        Console.WriteLine("Nombre; " + receivedMessage.Participants[i] + " esta listo? " + receivedMessage.Success[i]);

                    }
                    do
                    {
                        Console.WriteLine("Escribe Q para salir y R para esart list");
                        code = Console.ReadLine();
                        if (code.Equals("Q"))
                        {
                            gameplay = false;
                            //send quit msg
                        }
                        if (code.Equals("R"))
                        {
                            send = new ReadySent();
                            send.type = "playerReady";
                            gameplay = true;

                            if (send is ReadySent sendingReady)
                            {
                                sendingReady.State = true;
                                sendingReady.Position = receivedMessage.Position;
                                sendingReady.RoomID = receivedMessage.RoomID;
                                json = JsonConvert.SerializeObject(sendingReady);

                                // Convertir el JSON a bytes
                                buffer = Encoding.UTF8.GetBytes(json);

                                // Enviar los bytes al servidor
                                Console.WriteLine("Enviado");
                                stream.Write(buffer, 0, buffer.Length);
                            }
                        }
                    } while (menuloop);

                    while(gameplay)
                    {
                        //playing loop
                    }
                    
                }
                else if (send is ErrorMessage sendErrorMessage)
                {
                    Console.WriteLine(sendErrorMessage.ErrorText);
                }
               
                Console.Read();
            }

        } while (!close);
    }

    static void ReadFromServer(NetworkStream stream)
    {
        byte[] buffer = new byte[256];
        string receivedMessage;

        do
        {
            int bytesRead = stream.Read(buffer, 0, buffer.Length);
            receivedMessage = Encoding.UTF8.GetString(buffer, 0, bytesRead);
            Console.WriteLine("Mensaje recibido del servidor: {0}", receivedMessage);
        } while (!close);
    }
}
