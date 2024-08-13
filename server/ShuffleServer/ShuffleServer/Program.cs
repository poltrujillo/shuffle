using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Text;
using Newtonsoft.Json;
using ShuffleConsole;

class TCPServer
{
    static List<Room> Rooms = new List<Room>();
    static object lockObject = new object();

    static void Main(string[] args)
    {
        int tcpPort = 8888;
        string ipAddress = "192.168.194.237";

        // Start TCP server
        StartTCPServer(ipAddress, tcpPort);

        Console.WriteLine("\nPress ENTER to exit...");
        Console.ReadLine();
    }

    static void StartTCPServer(string ipAddress, int port)
    {
        try
        {
            IPAddress localAddr = IPAddress.Parse(ipAddress);
            TcpListener tcpListener = new TcpListener(localAddr, port);

            tcpListener.Start();
            Console.WriteLine($"TCP server started at {ipAddress}:{port}");

            // Start listening for connections
            while (true)
            {
                TcpClient client = tcpListener.AcceptTcpClient();
                Console.WriteLine("TCP client connected.");

                // Handle TCP client in a separate thread
                Thread clientThread = new Thread(() => HandleTCPClient(client));
                clientThread.Start();
            }
        }
        catch (Exception e)
        {
            Console.WriteLine("Error: " + e.ToString());
        }
    }

    static void HandleTCPClient(TcpClient client)
    {
        try
        {
           
            NetworkStream stream = client.GetStream();
            byte[] buffer = new byte[2048];
            int bytesRead;
           string message = "You have been connected via TCP\n";
           byte[] data = Encoding.ASCII.GetBytes(message);
            //stream.Write(data, 0, data.Length);
            // Read data sent by the client
            while ((bytesRead = stream.Read(buffer, 0, buffer.Length)) != 0)
            {
                string jsonData = Encoding.UTF8.GetString(buffer, 0, bytesRead);
                Console.WriteLine(jsonData);
                Message receivedMessage = JsonConvert.DeserializeObject<Message>(jsonData);
                string json;
                // Handle different types of messages
                switch (receivedMessage.type)
                {
                    case "crear":
                        RoomCreateMessage m = JsonConvert.DeserializeObject<RoomCreateMessage>(jsonData);
                        string key = GenerateUniqueRandomString();
                        lock (lockObject)
                        {
                            Room r = new Room(key, m.PlaylistID, m.Amount, (int)m.Tiempo, m.Tipo);
                            r.AddClient(new Cliente(m.Usuario, client, true));
                            Rooms.Add(r);
                        }
                        RoomHasBeenCreateMessage response = new RoomHasBeenCreateMessage();
                        response.RoomID = key;
                        response.type = "created";
                        json = JsonConvert.SerializeObject(response);
                        json += "\n";

                        // Convertir el JSON a bytes
                        buffer = Encoding.UTF8.GetBytes(json);

                        // Enviar los bytes al servidor
                        stream.Write(buffer, 0, buffer.Length);
                        break;
                    case "join":
                        RoomJoinMessage joinM = JsonConvert.DeserializeObject<RoomJoinMessage>(jsonData);
                        if (Rooms.Any(room => room.Key == joinM.RoomId))
                        {
                            var roomWithSameKey = Rooms.First(room => room.Key == joinM.RoomId);
                            if (roomWithSameKey.Clientes.Count < 4)
                            {

                                RoomHasBeenJoinedMessage joinedMessage = new RoomHasBeenJoinedMessage();
                                joinedMessage.RoomID = joinM.RoomId;
                                joinedMessage.Playlist = roomWithSameKey.PlaylistToken;
                                joinedMessage.Success = new List<bool>();
                                joinedMessage.Participants = new List<string>();
                                int pos = 0;
                                foreach (var c in roomWithSameKey.Clientes)
                                {
                                    joinedMessage.Success.Add(c.ready);
                                    joinedMessage.Participants.Add(c.Name);
                                    //enviar a cada usuario que hay uno nuevo
                                    NetworkStream PartnerStream = c.Conexion.GetStream();
                                    NewPlayerJoinedMessage newJoinedMessage = new NewPlayerJoinedMessage();
                                    newJoinedMessage.PlayerName = joinM.Usuario;
                                    newJoinedMessage.type = "joined";
                                    json = JsonConvert.SerializeObject(newJoinedMessage);
                                    json += "\n";

                                    // Convertir el JSON a bytes
                                    buffer = Encoding.UTF8.GetBytes(json);
                                    PartnerStream.Write(buffer, 0, buffer.Length);
                                    pos++;

                                }
                                roomWithSameKey.Clientes.Add(new Cliente(joinM.Usuario, client, false));
                                joinedMessage.Position = pos;
                                joinedMessage.RoomID = joinM.RoomId;
                                json = JsonConvert.SerializeObject(joinedMessage);
                                json += "\n";

                                // Convertir el JSON a bytes

                                buffer = Encoding.UTF8.GetBytes(json);

                                // Enviar los bytes al servidor
                                stream.Write(buffer, 0, buffer.Length);

                            }
                            else
                            {
                                string a = "sfasd";
                                buffer = Encoding.UTF8.GetBytes(a);

                                // Enviar los bytes al servidor
                                stream.Write(buffer, 0, buffer.Length);
                            }
                        }
                        else
                        {
                            string a = "sfasd";
                            buffer = Encoding.UTF8.GetBytes(a);

                            // Enviar los bytes al servidor
                            stream.Write(buffer, 0, buffer.Length);
                        }
                        break;
                    case "playerReady":
                        var readyM = JsonConvert.DeserializeObject<ReadySent>(jsonData);
                        if (Rooms.Any(room => room.Key == readyM.RoomID))
                        {
                            if (Rooms.Any(room => room.Key == readyM.RoomID))
                            {
                                var roomWithSameKey = Rooms.First(room => room.Key == readyM.RoomID);
                                int pos = 0;
                                foreach (var c in roomWithSameKey.Clientes)
                                {
                                    if(pos != readyM.Position)
                                    {
                                        NetworkStream PartnerStream = c.Conexion.GetStream();
        
                                        json = JsonConvert.SerializeObject(readyM);
                                        json += "\n";
                                        buffer = Encoding.UTF8.GetBytes(json);
                                        PartnerStream.Write(buffer, 0, buffer.Length);
                                        pos++;
                                    }
                                }
                            }
                        }
                            break;
                    case "startGame":
                        var startM = JsonConvert.DeserializeObject<StartSent>(jsonData);
                        if (Rooms.Any(room => room.Key == startM.RoomId))
                        {
                            if (Rooms.Any(room => room.Key == startM.RoomId))
                            {
                                var roomWithSameKey = Rooms.First(room => room.Key == startM.RoomId);
                                int pos = 0;
                                foreach (var c in roomWithSameKey.Clientes)
                                {
                                    if (pos != startM.Position)
                                    {
                                        NetworkStream PartnerStream = c.Conexion.GetStream();

                                        json = JsonConvert.SerializeObject(startM);
                                        json += "\n";
                                        buffer = Encoding.UTF8.GetBytes(json);
                                        PartnerStream.Write(buffer, 0, buffer.Length);
                                        
                                    }
                                    pos++;
                                }
                            }
                        }
                        break;
                    case "readyPlay":
                        var readyPlayM = JsonConvert.DeserializeObject<ReadySent>(jsonData);
                        if (Rooms.Any(room => room.Key == readyPlayM.RoomID))
                        {
                            if (Rooms.Any(room => room.Key == readyPlayM.RoomID))
                            {
                                var roomWithSameKey = Rooms.First(room => room.Key == readyPlayM.RoomID);
                                roomWithSameKey.Ready[readyPlayM.Position] = readyPlayM.State;
                                bool allReady = true;
                                foreach (var r in roomWithSameKey.Ready)
                                {
                                    if (!r)
                                    {
                                        allReady = false;
                                    }
                                }
                                if (allReady)
                                {
                                    var pos = 0;
                                    foreach (var r in roomWithSameKey.Clientes)
                                    {
                                        roomWithSameKey.Ready[pos] = false;
                                        pos++;
                                    }
                                    Message msg = new Message();
                                    msg.type = "startP";
                                    pos = 0;
                                    foreach (var c in roomWithSameKey.Clientes)
                                    {
                                        
                                            NetworkStream PartnerStream = c.Conexion.GetStream();

                                            json = JsonConvert.SerializeObject(msg);
                                            json += "\n";
                                            buffer = Encoding.UTF8.GetBytes(json);
                                           PartnerStream.Write(buffer, 0, buffer.Length);
                                        
                                        
                                    }
                                    json = JsonConvert.SerializeObject(msg);
                                    json += "\n";
                                    Console.WriteLine(json);
                                    buffer = Encoding.UTF8.GetBytes(json);
                                    

                                    stream.Write(buffer, 0, buffer.Length);
                                }
                            }
                        }
                        break;
                    case "puntuacion":
                        var PuntM = JsonConvert.DeserializeObject<PuntuacionSent>(jsonData);
                        if (Rooms.Any(room => room.Key == PuntM.RoomId))
                        {
                            if (Rooms.Any(room => room.Key == PuntM.RoomId))
                            {

                                var roomWithSameKey = Rooms.First(room => room.Key == PuntM.RoomId);
                                roomWithSameKey.Ready[PuntM.Position?? 0] = true;
                                var pos = 0;
                                foreach (var c in roomWithSameKey.Clientes)
                                {
                                    buffer = new byte[2048];
                                        NetworkStream PartnerStream = c.Conexion.GetStream();

                                        json = JsonConvert.SerializeObject(PuntM);
                                        json += "\n";
                                        buffer = Encoding.UTF8.GetBytes(json);
                                        PartnerStream.Write(buffer, 0, buffer.Length);
                                    
                                }

                            }
                        }
                        break;
                    case "next":
                        readyPlayM = JsonConvert.DeserializeObject<ReadySent>(jsonData);
                        if (Rooms.Any(room => room.Key == readyPlayM.RoomID))
                        {
                            if (Rooms.Any(room => room.Key == readyPlayM.RoomID))
                            {

                                var roomWithSameKey = Rooms.First(room => room.Key == readyPlayM.RoomID);
                                foreach (var c in roomWithSameKey.Clientes)
                                {
                                    NetworkStream PartnerStream = c.Conexion.GetStream();

                                    json = JsonConvert.SerializeObject(readyPlayM);
                                    json += "\n";
                                    buffer = Encoding.UTF8.GetBytes(json);
                                    PartnerStream.Write(buffer, 0, buffer.Length);
                                }
                            }
                        }
                        break;
                    case "sendTrack":
                        var sendTrackM = JsonConvert.DeserializeObject<getPlaylist>(jsonData);
                        if (Rooms.Any(room => room.Key == sendTrackM.RoomId))
                        {
                            if (Rooms.Any(room => room.Key == sendTrackM.RoomId))
                            {

                                var roomWithSameKey = Rooms.First(room => room.Key == sendTrackM.RoomId);

                                sendTrackM.Playlist = roomWithSameKey.PlaylistToken;
                                sendTrackM.Amount = roomWithSameKey.amount;
                                sendTrackM.Tiempo = roomWithSameKey.timer;
                                sendTrackM.Tipo = roomWithSameKey.typeNumber;
                                json = JsonConvert.SerializeObject(sendTrackM);
                                json += "\n";
                                buffer = Encoding.UTF8.GetBytes(json);

                                stream.Write(buffer, 0, buffer.Length);
                                json = JsonConvert.SerializeObject(sendTrackM);
                                json += "\n";
                                buffer = Encoding.UTF8.GetBytes(json);

                                stream.Write(buffer, 0, buffer.Length);
                            }
                        }
                        break;
                    case "salir":
                        var exitTrackM = JsonConvert.DeserializeObject<ExitMessage>(jsonData);
                        if (Rooms.Any(room => room.Key == exitTrackM.RoomId))
                        {
                            if (Rooms.Any(room => room.Key == exitTrackM.RoomId))
                            {

                                var roomWithSameKey = Rooms.First(room => room.Key == exitTrackM.RoomId);
                                roomWithSameKey.Clientes.RemoveAt(exitTrackM.Position);
                                roomWithSameKey.Ready.RemoveAt(exitTrackM.Position);
                                stream.Write(buffer, 0, buffer.Length);
                                json = JsonConvert.SerializeObject(exitTrackM);
                                json += "\n";
                                buffer = Encoding.UTF8.GetBytes(json);

                                stream.Write(buffer, 0, buffer.Length);
                                int pos = 0;
                                foreach (var c in roomWithSameKey.Clientes)
                                {
                                    var exitToSend = new ExitMessageRecived();
                                    exitToSend.PositionDelete = exitTrackM.Position;
                                    exitToSend.RoomId = exitTrackM.RoomId;
                                    exitToSend.type = "salirRecivido";
                                    exitToSend.PositionMinePoistion = pos;
                                    pos++;

                                    NetworkStream PartnerStream = c.Conexion.GetStream();

                                    json = JsonConvert.SerializeObject(exitToSend);
                                    json += "\n";
                                    buffer = Encoding.UTF8.GetBytes(json);
                                    PartnerStream.Write(buffer, 0, buffer.Length);
                                }
                                if(roomWithSameKey.Clientes.Count == 0)
                                {
                                    Rooms.Remove(roomWithSameKey);
                                }

                            }
                        }
                        break;
                    default:
                        Console.WriteLine("Unrecognized message type.");
                        break;
                }
                buffer = new byte[2048];
            }

            // Close the client connection
            client.Close();
        }
        catch (Exception ex)
        {
            Console.WriteLine("Error handling client: " + ex.Message);
        }
    }

    static string GenerateUniqueRandomString()
    {
        // Caracteres válidos: dígitos (0-9) y letras mayúsculas (A-Z)
        string validChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        // Generar un generador de números aleatorios
        Random random = new Random();

        // Mientras se encuentre la cadena generada en alguna de las salas, seguir generando nuevas cadenas
        string randomString;
        do
        {
            // Construir la cadena aleatoria
            StringBuilder sb = new StringBuilder(4); // Longitud de 4 caracteres
            for (int i = 0; i < 4; i++)
            {
                // Elegir un carácter aleatorio de los caracteres válidos
                char randomChar = validChars[random.Next(validChars.Length)];
                sb.Append(randomChar);
            }

            randomString = sb.ToString();
        } while (Rooms.Any(room => room.Key == randomString));

        return randomString;
    }
}
