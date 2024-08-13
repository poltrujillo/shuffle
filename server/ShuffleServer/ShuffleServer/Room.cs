using Microsoft.AspNetCore.SignalR;
using System.Collections.Generic;
using System.Net.Sockets;

public class Room
{
    private string playlistID;
    private int? tiempo;

    public string Key { get; set; }
    public string PlaylistToken { get; set; }
    public int amount { get; set; }

    public int typeNumber { get; set; }

    public int timer { get; set; }
    public List<Cliente> Clientes { get; } = new List<Cliente>();
    public List<bool> Ready { get; set; } = new List<bool>();

    // Constructor
    public Room(string key, string playlistToken, int amount, int timer, int typeNumber)
    {
        Key = key;
        PlaylistToken = playlistToken;
        this.amount = amount;
        this.timer = timer;
        this.typeNumber = typeNumber;
    }

    // Método para añadir un cliente a la lista
    public void AddClient(Cliente client)
    {
        if (Clientes.Count < 4)
        {
            Clientes.Add(client);
            Ready.Add(false);
        }
        else
        {
            throw new System.Exception("The room is full.");
        }
    }
}

