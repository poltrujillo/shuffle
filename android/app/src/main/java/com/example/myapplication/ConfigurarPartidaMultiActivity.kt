package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.dataModels.Server.ErrorMessage
import com.example.myapplication.dataModels.Server.RoomCreateMessage
import com.example.myapplication.dataModels.Server.RoomHasBeenCreateMessage
import com.example.myapplication.dataModels.Server.ServerMessage
import com.example.myapplication.dataModels.Server.SocketManager
import com.example.myapplication.dataModels.Spotify.MyTrack
import com.example.myapplication.dataModels.Spotify.Tracks
import com.example.myapplication.dataModels.LaravelServer.UserResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import kotlin.random.Random

class ConfigurarPartidaMultiActivity : AppCompatActivity() {
    var Tracks = Tracks(total = 0, items = emptyList())
    var token = ""
    private var modeId = 0

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configurar_partida)
        Tracks = intent.getParcelableExtra("trackList") ?: Tracks(total = 0, items = emptyList())
        val a = Tracks.items
        token = intent.getStringExtra("token").toString()
        modeId = intent.getIntExtra("modeId", modeId)

        val spinner: Spinner = findViewById(R.id.spinner)
        val segundos: EditText = findViewById(R.id.editTextNumber)
        val switch: Switch = findViewById(R.id.switch1)
        val botonJugar: Button = findViewById(R.id.button6)

        val rondas = listOf(5, 10, 15, 20)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, rondas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter


        botonJugar.setOnClickListener{
            // Obtiene el valor seleccionado del Spinner
            val valorSeleccionado = spinner.selectedItem as Int
            var msg: RoomCreateMessage = RoomCreateMessage();
            msg.type = "crear"
            val sharedPrefs = getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)

            // Recupera el JSON de user almacenado en SharedPreferences
            val userJson = sharedPrefs.getString("user", "")


            // Verifica si el JSON de user no es nulo ni está vacío
            if (!userJson.isNullOrEmpty()) {
                // Deserializa el JSON de user de nuevo a un objeto User utilizando Gson
                val gson = Gson()
                val user = gson.fromJson(userJson, UserResponse::class.java)
                msg.Usuario = user.username

            } else {
                msg.Usuario = "host"
            }

            msg.PlaylistID = intent.getStringExtra("id").toString()
            msg.Amount = valorSeleccionado
            msg.Tiempo = segundos.text.toString().toInt()
            msg.Tipo = modeId

            Thread{
                SocketManager.sendData(msg)

                val gson = Gson()
                val response = SocketManager.getMessage()
                var res = gson.fromJson(response, ServerMessage::class.java)
                if (res.type.equals("error"))
                {
                    res = gson.fromJson(response, ErrorMessage::class.java)
                    Toast.makeText(this, res.ErrorText , Toast.LENGTH_SHORT).show()
                } else {
                    val res2 = gson.fromJson(response, RoomHasBeenCreateMessage::class.java)
                    goToGame(valorSeleccionado, segundos.text.toString().toInt(), !switch.isChecked, res2.RoomID!!)
                }
            }.start()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView3)

        // Establecer el listener para manejar los clics en los elementos del menú
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.ranking -> {
                    // Si se hace clic en el primer elemento, iniciar la Activity deseada
                    startActivity(Intent(this, RankingActivity::class.java))
                    true
                } R.id.account -> {
                // Si se hace clic en el primer elemento, iniciar la Activity deseada
                startActivity(Intent(this, PerfilUsuarioActivity::class.java))
                true
            } R.id.main -> {
                // Si se hace clic en el primer elemento, iniciar la Activity deseada
                startActivity(Intent(this, MainActivity::class.java))
                true
            } R.id.help -> {
                // Si se hace clic en el primer elemento, iniciar la Activity deseada
                startActivity(Intent(this, HelpActivity::class.java))
                true
            }
                else -> false
            }
        }
    }

    fun goToGame(rondas:Int, tiempo:Int, modoDificil: Boolean, roomId:String)
    {
        var encontradas = 0
        val trackList: MutableList<MyTrack> = mutableListOf()
        val t = Tracks.items
        while (encontradas < rondas) {
            val randomNumber = Random.nextInt(Tracks.total)

            val randomTrack = Tracks.items[randomNumber].track

            if (trackList.none { it == randomTrack }) {
                trackList.add(randomTrack) // Modify this part to create MyTrack object using the id
                encontradas++
            }
        }
        val intent = Intent(this@ConfigurarPartidaMultiActivity, WaitRoom::class.java)

        intent.putExtra("state", "create")
        intent.putExtra("trackList", ArrayList(trackList)) // Convert MutableList to ArrayList
        intent.putExtra("tiempo", tiempo)
        intent.putExtra("modoDificil", modoDificil)
        intent.putExtra("roomId", roomId)
        intent.putExtra("modeId", modeId)
        startActivity(intent)
    }
}