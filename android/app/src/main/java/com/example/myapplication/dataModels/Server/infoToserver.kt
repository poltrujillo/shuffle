package com.example.myapplication.dataModels.Server

import com.example.myapplication.dataModels.Spotify.MyTrack
import java.io.Serializable

open class ServerMessage : Serializable {
    var type: String? = null
}

class PlaylistMessage : ServerMessage() {
    var playlist: Array<String>? = null
}

class RoomJoinMessage : ServerMessage() {
    var RoomID: String? = null
    var usuario: String? = null
}

class RoomCreateMessage : ServerMessage() {
    var PlaylistID: String? = null
    var Usuario: String? = null
    var Amount: Int? = 5
    var Tiempo: Int? = 5
    var Tipo: Int? = null
}

class RoomHasBeenCreateMessage : ServerMessage() {
    var RoomID: String? = null
}

class RoomHasBeenJoinedMessage : ServerMessage() {
    var RoomID: String? = null
    var Success: ArrayList<Boolean>? = null
    var Participants: ArrayList<String>? = null
    var Playlist: String? = null
    var Position: Int? = null
    var Tipo: Int? = null
}

class NewPlayerJoinedMessage : ServerMessage() {
    var PlayerName: String? = null
}

class ReadySent : ServerMessage() {
    var State: Boolean? = null
    var Position: Int? = null
    var RoomId: String? = null
}

class PuntuacionSent : ServerMessage() {
    var State: Boolean? = null
    var Position: Int? = null
    var RoomId: String? = null
    var Puntos: Double? = null
}

class SalirSala : ServerMessage() {
    var Position: Int? = null
    var RoomId: String? = null
}
class SalirSalaRecvido : ServerMessage() {
    var PositionDelete: Int? = null
    var PositionMinePoistion: Int? = null
    var RoomId: String? = null
}
class getPlaylist : ServerMessage() {
    var Playlist: String? = null
    var RoomId: String? = null
    var Amount: Int? = 5
    var Tiempo: Int = 0
    var Tipo: Int? = null
}



class StartSent : ServerMessage() {
    var Position: Int? = null
    var RoomId: String? = null
    var Tiempo: Int? = null
    var Tracks: ArrayList<MyTrack>? = null
    var Dificil: Boolean? = null
}

class ErrorMessage : ServerMessage() {
    var ErrorText: String? = null
}
