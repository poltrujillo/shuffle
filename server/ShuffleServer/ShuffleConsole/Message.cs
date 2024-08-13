using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ShuffleConsole
{
    [Serializable]
    internal class Message
    {
        public string type { get; set; }
    }

    [Serializable]
    internal class PlaylistMessage : Message
    {
        public string[] Playlist { get; set; }
    }

    [Serializable]
    internal class RoomJoinMessage : Message
    {
        public string RoomId { get; set; }
        public string Usuario { get; set; }
    }

    [Serializable]
    internal class RoomCreateMessage : Message
    {
        public string PlaylistID { get; set; }
        public string Usuario { get; set; }
    }

    [Serializable]
    internal class RoomHasBeenCreateMessage : Message
    {
        public string RoomID { get; set; }
    }

    [Serializable]
    internal class RoomHasBeenJoinedMessage : Message
    {
        public string RoomID { get; set; }
        public List<bool> Success { get; set; }
        public List<string> Participants { get; set; }
        public string Playlist { get; set; }

        public int Position { get; set; }
    }
    [Serializable]
    internal class ReadySent : Message
    {
        public bool State { get; set; }

        public int Position { get; set; }
        public string RoomID { get; set; }
    }

    [Serializable]
    internal class NewPlayerJoinedMessage : Message
    {
        public string PlayerName { get; set; }
    }

    [Serializable]
    internal class StartSent : Message
    {
        public int? Position { get; set; }
        public string RoomId { get; set; }
        public int? Tiempo { get; set; }
        public List<MyTrack> Tracks { get; set; }
        public bool? Dificil { get; set; }
    }

    [Serializable]

    public class MyTrack
    {
        public string Id { get; set; }
        public string Name { get; set; }
    }

    [Serializable]
    internal class ErrorMessage : Message
    {
        public string ErrorText { get; set; }
    }
}
