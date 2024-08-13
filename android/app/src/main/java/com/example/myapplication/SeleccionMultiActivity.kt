package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.myapplication.dataModels.Server.RoomCreateMessage
import com.example.myapplication.dataModels.Server.SocketManager

class SeleccionMultiActivity : AppCompatActivity() {
    var token = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccion_multi)

        token = intent.getStringExtra("token").toString()

        val unirseSala = findViewById<Button>(R.id.button16)
        val crearSala = findViewById<Button>(R.id.button17)
        val cancelar = findViewById<Button>(R.id.button18)

        unirseSala.setOnClickListener{
            val intent = Intent(this, UnirseSalaActivity::class.java)
            startActivity(intent)
        }

        crearSala.setOnClickListener{
            val intent = Intent(this, GameModesMultiActivity::class.java)
            intent.putExtra("token", token)
            startActivity(intent)
        }

        cancelar.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}