package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myapplication.dataModels.Server.ErrorMessage
import com.example.myapplication.dataModels.Server.RoomHasBeenCreateMessage
import com.example.myapplication.dataModels.Server.RoomHasBeenJoinedMessage
import com.example.myapplication.dataModels.Server.RoomJoinMessage
import com.example.myapplication.dataModels.Server.ServerMessage
import com.example.myapplication.dataModels.Server.SocketManager
import com.google.gson.Gson

class UnirseSalaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unirse_sala)

        val botonUnirse = findViewById<Button>(R.id.button19)
        val botonCancel = findViewById<Button>(R.id.button20)
        val codigo = findViewById<EditText>(R.id.editTextText3)
        SocketManager.initzializeBuffer()

        botonCancel.setOnClickListener{
            val intent = Intent(this, SeleccionMultiActivity::class.java)
            startActivity(intent)
        }

        botonUnirse.setOnClickListener{
            var msg:RoomJoinMessage =  RoomJoinMessage();
            msg.type = "join"
            msg.usuario = "guest"
            msg.RoomID = codigo.text.toString()

            Thread{
                SocketManager.sendData(msg)
            }.start()

            Thread{
                val gson = Gson()
                val response = SocketManager.getMessage()
                var res = gson.fromJson(response, ServerMessage::class.java)
                if (res.type.equals("error"))
                {
                    res = gson.fromJson(response, ErrorMessage::class.java)
                    Toast.makeText(this, res.ErrorText , Toast.LENGTH_SHORT).show()
                } else{
                    val res2 = gson.fromJson(response, RoomHasBeenJoinedMessage::class.java)

                    val intent = Intent(this, WaitRoom::class.java)
                    intent.putExtra("state", "joined")

                    intent.putExtra("room_message", res2)

                    startActivity(intent)
                }
            }.start()
        }
    }
}