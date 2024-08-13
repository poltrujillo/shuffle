package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.APIServices.SpotifyAPIService
import com.example.myapplication.dataModels.Server.Jugadores
import com.example.myapplication.dataModels.Server.NewPlayerJoinedMessage
import com.example.myapplication.dataModels.Server.ReadySent
import com.example.myapplication.dataModels.Server.RoomHasBeenJoinedMessage
import com.example.myapplication.dataModels.Server.SalirSala
import com.example.myapplication.dataModels.Server.SalirSalaRecvido
import com.example.myapplication.dataModels.Server.ServerMessage
import com.example.myapplication.dataModels.Server.SocketManager
import com.example.myapplication.dataModels.Server.StartSent
import com.example.myapplication.dataModels.Server.getPlaylist
import com.example.myapplication.dataModels.Spotify.MyTrack
import com.example.myapplication.dataModels.Spotify.PlayList
import com.example.myapplication.dataModels.LaravelServer.UserResponse
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

class WaitRoom : AppCompatActivity() {
    var jugadores: MutableList<Jugadores> = mutableListOf()
    var token = ""
    var listenFornew: Boolean = true
    var position: Int = 0
    var entryWay:String = ""
    var roomId:String =""
    var tiempo = 0
    var tracksArray: List<MyTrack> = emptyList()
    var modeId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wait_room)

        val Code:TextView = findViewById(R.id.textView11)
        val readyButton:Button = findViewById(R.id.button13)
        val start:Button = findViewById(R.id.button14)
        listenFornew = true

        token = intent.getStringExtra("token").toString()
        roomId = intent.getStringExtra("roomId").toString()
        modeId = intent.getIntExtra("modeId", modeId)
        Code.text = intent.getStringExtra("roomId")
        entryWay = intent.getStringExtra("state").toString()

        val firstTime = intent.getBooleanExtra("firstTime",true)

        if(firstTime)
        {

            if(entryWay.equals("create"))
            {
                tiempo = intent.getIntExtra("tiempo", 0)
                tracksArray = intent.getParcelableArrayListExtra<MyTrack>("trackList")!!
                readyButton.isEnabled = false

                val sharedPrefs = getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
                // Recupera el JSON de user almacenado en SharedPreferences
                val userJson = sharedPrefs.getString("user", "")
                // Verifica si el JSON de user no es nulo ni está vacío
                if (!userJson.isNullOrEmpty()) {
                    // Deserializa el JSON de user de nuevo a un objeto User utilizando Gson
                    val gson = Gson()
                    val user = gson.fromJson(userJson, UserResponse::class.java)
                    val jugador:Jugadores = Jugadores(user.username,0.0,true)
                    jugadores.add(jugador)

                } else {
                    val jugador:Jugadores = Jugadores("host",0.0,true)
                    jugadores.add(jugador)
                }


                viewConfig(jugadores)
            } else{
                start.isEnabled = false
                val receivedMessage = intent.getSerializableExtra("room_message") as? RoomHasBeenJoinedMessage
                val participants = receivedMessage!!.Participants
                val ready = receivedMessage!!.Success
                position = receivedMessage.Position!!
                Code.text = receivedMessage.RoomID
                roomId = receivedMessage.RoomID!!
                modeId = receivedMessage.Tipo!!
                var i = 0
                while (i < participants!!.size)
                {
                    val jugador:Jugadores = Jugadores(participants.get(i),0.0, ready!!.get(i))
                    i++;
                    jugadores.add(jugador)
                }
                val sharedPrefs = getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
                // Recupera el JSON de user almacenado en SharedPreferences
                val userJson = sharedPrefs.getString("user", "")
                // Verifica si el JSON de user no es nulo ni está vacío
                if (!userJson.isNullOrEmpty()) {
                    // Deserializa el JSON de user de nuevo a un objeto User utilizando Gson
                    val gson = Gson()
                    val user = gson.fromJson(userJson, UserResponse::class.java)
                    val jugador:Jugadores = Jugadores(user.username,0.0,true)
                    jugadores.add(jugador)

                } else {
                    val jugador:Jugadores = Jugadores("me",0.0,false)
                    jugadores.add(jugador)
                }

                viewConfig(jugadores)
            }
        } else{
            start.isEnabled = false
            jugadores = (intent?.getSerializableExtra("jugadores") as? ArrayList<Jugadores>)!!
            val pos = 0
            for (j in jugadores)
            {
                j.puntuacion = 0.0
                if (pos == 0)
                {
                    j.listo = true
                    Thread{
                        val msg = getPlaylist()
                        msg.type = "sendTrack"
                        msg.RoomId = roomId
                        SocketManager.sendData(msg)
                    }.start()
                } else
                {
                    j.listo = false
                }
            }
            viewConfig(jugadores)
        }

        Thread{
            while (listenFornew)
            {
                val gson = Gson()
                val response = SocketManager.getMessage()
                var res = gson.fromJson(response, ServerMessage::class.java)
                if (res.type.equals("joined"))
                {
                    var res2 = gson.fromJson(response, NewPlayerJoinedMessage::class.java)
                    val player = Jugadores(res2.PlayerName.toString(),0.0,false)
                    jugadores.add(player)
                    runOnUiThread {
                        // Call viewConfig function to update UI elements based on the modified jugadores list
                        viewConfig(jugadores)
                        start.isEnabled=false
                    }

                } else if (res.type.equals("salirRecivido"))
                {
                    var res2 = gson.fromJson(response, SalirSalaRecvido::class.java)
                    jugadores.removeAt(res2.PositionDelete!!)
                    position = res2.PositionMinePoistion!!
                    if (position == 0)
                    {
                        val msg = getPlaylist()
                        msg.type = "sendTrack"
                        msg.RoomId = roomId
                        SocketManager.sendData(msg)
                    }
                    runOnUiThread()
                    {
                        viewConfig(jugadores)
                    }
                } else if (res.type.equals("sendTrack"))
                {
                    runOnUiThread(){
                        start.isEnabled = true
                        readyButton.isEnabled = false
                    }
                    var res2 = gson.fromJson(response, getPlaylist::class.java)
                    var amount = res2.Amount
                    var listID = res2.Playlist
                    tiempo = res2.Tiempo
                    modeId = res2.Tipo!!
                    entryWay = "create"
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            // Perform network request
                            val call = getRetrofit().create(SpotifyAPIService::class.java)
                                .getPlaylist("Authorization: Bearer " + token,listID!! )
                            val response = call.execute()

                            // Check if the request was successful
                            if (response.isSuccessful) {

                                val posts = response.body() as PlayList
                                val allTracks = posts.tracks
                                var encontradas = 0
                                val trackList: MutableList<MyTrack> = mutableListOf()
                                val t = allTracks.items
                                while (encontradas < amount!!) {
                                    val randomNumber = Random.nextInt(allTracks.total)

                                    val randomTrack = allTracks.items[randomNumber].track

                                    if (trackList.none { it == randomTrack }) {
                                        trackList.add(randomTrack) // Modify this part to create MyTrack object using the id
                                        encontradas++
                                    }
                                }
                                tracksArray = trackList
                            } else {
                                Log.e("MainActivity", "Error: ${response.code()} - ${response.message()}")
                            }
                        } catch (e: Exception) {
                            Log.e("MainActivity", "Error: ${e.message}", e)
                        }
                    }


                } else if (res.type.equals("playerReady"))
                {
                    var res2 = gson.fromJson(response, ReadySent::class.java)
                    jugadores.get(res2.Position!!).listo = true
                    runOnUiThread {
                        // Call viewConfig function to update UI elements based on the modified jugadores list
                        viewConfig(jugadores)
                        if(checkReadys() && entryWay.equals("create"))
                        {
                            start.isEnabled=true
                        }
                    }

                } else if (res.type.equals("startGame"))
                {
                    var res2 = gson.fromJson(response, StartSent::class.java)
                    val intent = Intent(this, MultiGameplay::class.java)
                    intent.putExtra("trackList", res2.Tracks) // Convert MutableList to ArrayList
                    intent.putExtra("tiempo", res2.Tiempo)
                    intent.putExtra("modoDificil", res2.Dificil)
                    intent.putExtra("jugadores", ArrayList(jugadores))
                    intent.putExtra("positcion", position)
                    intent.putExtra("roomID", res2.RoomId)
                    intent.putExtra("usuario", entryWay)
                    intent.putExtra("token", token)
                    intent.putExtra("modeId", modeId)
                    listenFornew = false
                    startActivity(intent)
                }

            }

        }.start()

        readyButton.setOnClickListener()
        {
            jugadores.get(position).listo = !jugadores.get(position).listo
            viewConfig(jugadores)
            Thread {
                val send = ReadySent()
                send.type = "playerReady"
                send.RoomId = roomId
                send.State = jugadores.get(position).listo
                send.Position = position
                SocketManager.sendData(send)
            }.start()
        }

        start.setOnClickListener(){

            Thread {
                val send = StartSent()
                send.RoomId = Code.text.toString()
                send.Tiempo = tiempo
                send.type = "startGame"
                send.Position = position
                send.Dificil = intent.getBooleanExtra("modoDificil", true)
                send.Tracks = tracksArray as ArrayList<MyTrack>

                SocketManager.sendData(send)
                listenFornew = false
                val intent = Intent(this@WaitRoom, MultiGameplay::class.java)
                intent.putExtra("trackList", send.Tracks) // Convert MutableList to ArrayList
                intent.putExtra("tiempo", send.Tiempo)
                intent.putExtra("modoDificil", send.Dificil)
                intent.putExtra("jugadores", ArrayList(jugadores))
                intent.putExtra("positcion", position)
                intent.putExtra("roomID", send.RoomId)
                intent.putExtra("usuario", entryWay)
                intent.putExtra("token",token)
                intent.putExtra("modeId",modeId)
                startActivity(intent)
            }.start()

        }
    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val intent = Intent(this@WaitRoom, MainActivity::class.java)
        val msg = SalirSala()
        msg.RoomId = roomId
        msg.Position = position
        msg.type = "salir"
        listenFornew = false
        Thread{
            SocketManager.sendData(msg)
        }.start()
        startActivity(intent)



    }
    private fun checkReadys():Boolean{

        for (jug in jugadores){
            if(!jug.listo){
                return false
            }
        }

        return true
    }
    private fun viewConfig(items:MutableList<Jugadores>) {
        val rvList = findViewById<RecyclerView>(R.id.recyclerView2)

        // Configurar el adaptador y el administrador de diseño de la RecyclerView
        rvList.adapter = ListAdapter(items, this)
        rvList.layoutManager = LinearLayoutManager(this)

    }
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.spotify.com/v1/")
            .addConverterFactory(GsonConverterFactory.create()) .build()
    }
}