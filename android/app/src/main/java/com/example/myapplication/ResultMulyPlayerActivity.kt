package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.echo.holographlibrary.Bar
import com.echo.holographlibrary.BarGraph
import com.example.myapplication.dataModels.Server.Jugadores
import com.example.myapplication.dataModels.Server.SalirSala
import com.example.myapplication.dataModels.Server.SocketManager
import com.example.myapplication.dataModels.Spotify.Tracks
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.LoginActivity
import kotlin.math.roundToInt

class ResultMulyPlayerActivity : AppCompatActivity() {
    var token = ""
    private val CLIENT_ID = "a4347d0dc7f3407eb136e85da349e7e1"
    private val REDIRECT_URI = "shuffleapp://callback"
    private var TOKENSPOTIFY = "token"
    var roundTime:Int = 0
    var entryWay:String = ""
    var hardMode:Boolean = false
    var pos:Int = 0
    var roomID:String = ""
    var jugadores: MutableList<Jugadores> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_muly_player)
        token = intent.getStringExtra("token").toString()
        val volver = findViewById<Button>(R.id.button8)
        val salir = findViewById<Button>(R.id.button9)
        val puntos = ArrayList<Bar>()
        jugadores = (intent?.getSerializableExtra("jugadores") as? ArrayList<Jugadores>)!!
        roundTime = intent.getIntExtra("tiempo", 0)
        entryWay = intent.getStringExtra("usuario").toString()
        hardMode = intent.getBooleanExtra("modoDificil", true)
        pos = intent.getIntExtra("positcion",0)
        roomID = intent.getStringExtra("roomID").toString()

        graficarBarras(puntos, jugadores)

        volver.setOnClickListener{
            val builder =
                AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)

            builder.setScopes(arrayOf("streaming"))
            val request = builder.build()

            AuthorizationClient.openLoginActivity(this, LoginActivity.REQUEST_CODE, request)

        }

        salir.setOnClickListener{
            val intent = Intent(this@ResultMulyPlayerActivity, MainActivity::class.java)
            val msg = SalirSala()
            msg.RoomId = roomID
            msg.Position = pos
            msg.type = "salir"
            Thread{
                SocketManager.sendData(msg)
            }.start()
            startActivity(intent)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        // Check if result comes from the correct activity
        if (requestCode == 1138) {
            val response = AuthorizationClient.getResponse(resultCode, intent)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    val connectionParams = ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build()
                    Log.d("MainActivity", "Connected! Yay!")
                    TOKENSPOTIFY = response.accessToken
                    val intent = Intent(this, WaitRoom::class.java)
                    intent.putExtra("tiempo", roundTime)
                    intent.putExtra("modoDificil", hardMode)
                    intent.putExtra("positcion", pos)
                    intent.putExtra("roomId", roomID)
                    intent.putExtra("usuario", entryWay)
                    intent.putExtra("jugadores", ArrayList(jugadores))
                    intent.putExtra("firstTime", false)
                    intent.putExtra("token",TOKENSPOTIFY)
                    startActivity(intent)
                }
                AuthorizationResponse.Type.ERROR -> {
                    val context: Context = applicationContext
                    val message = "This is an error message"
                    val duration = Toast.LENGTH_LONG
                    val toast = Toast.makeText(context, message, duration)
                    toast.show()
                }
                else -> {}
            }
        }

    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val intent = Intent(this@ResultMulyPlayerActivity, MainActivity::class.java)
        val msg = SalirSala()
        msg.RoomId = roomID
        msg.Position = pos
        msg.type = "salir"
        Thread{
            SocketManager.sendData(msg)
        }.start()
        startActivity(intent)
    }
    fun graficarBarras(puntos: ArrayList<Bar>, puntuacion: MutableList<Jugadores>) {

        for ( p in puntuacion)
        {
            val barra = Bar()
            var color = generarColorHexAleatorio()
            barra.color = Color.parseColor(color)
            barra.name = p.nombre
            barra.value = p.puntuacion.toInt().toFloat()
            puntos.add(barra)
        }


        val grafica = findViewById<View>(R.id.graphBar) as BarGraph
        grafica.bars = puntos
    }

    fun generarColorHexAleatorio(): String {
        val letras = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F")
        var color = "#"
        for (i in 0..5) {
            color += letras[(Math.random() * 15).roundToInt()]
        }

        return color
    }
}