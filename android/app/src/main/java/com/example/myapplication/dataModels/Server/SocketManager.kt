package com.example.myapplication.dataModels.Server

// SocketManager.kt

import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

object SocketManager {
    private lateinit var clientSocket: Socket
    private lateinit var bufferedReader: BufferedReader
    private lateinit var printWriter: PrintWriter
    private lateinit var Ip: String
    private var Port: Int = 0

    fun initialize(serverIP: String, port: Int) {
        Thread {
            try {
                Ip = serverIP
                Port = port
                // Establish a connection with the server
                clientSocket = Socket(serverIP, port)
                bufferedReader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
                printWriter = PrintWriter(clientSocket.getOutputStream(), true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    fun initzializeBuffer(){
        Thread {
            bufferedReader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
        }
    }
    fun getMessage(): String? {
        return if (::bufferedReader.isInitialized) {
            bufferedReader.readLine()
        } else {
            null
        }
    }



    fun sendData(data: ServerMessage) {
        val gson = Gson()
        val json = gson.toJson(data)
        printWriter.println(json)
    }

    fun closeConnection() {
        try {
            clientSocket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}