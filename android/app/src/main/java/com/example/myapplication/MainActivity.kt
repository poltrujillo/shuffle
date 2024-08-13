package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.dataModels.Server.SocketManager
import com.example.myapplication.dataModels.Global.BASE_URL_SERVER
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.LoginActivity

class MainActivity : AppCompatActivity() {
    private val CLIENT_ID = "a4347d0dc7f3407eb136e85da349e7e1"
    private val REDIRECT_URI = "shuffleapp://callback"
    private var TOKENSPOTIFY = "token"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SocketManager.initialize(BASE_URL_SERVER, 8888)
        
        val buttonMulti = findViewById<Button>(R.id.button2)
        val buttonSingleplayer = findViewById<Button>(R.id.button)
        val account = findViewById<ImageView>(R.id.imageView17)
        val tokenSpotify = intent.getStringExtra("token")
        val buttonRanking = findViewById<Button>(R.id.button3)

        buttonMulti.setOnClickListener{
            val  intent = Intent(this, SeleccionMultiActivity::class.java)
            intent.putExtra("token", TOKENSPOTIFY)
            startActivity(intent)
        }
        account.setOnClickListener{
            val intent = Intent(this, PerfilUsuarioActivity::class.java)
            startActivity(intent)
        }

        buttonRanking.setOnClickListener{
            val intent = Intent(this, RankingActivity::class.java)
            startActivity(intent)
        }

        buttonSingleplayer.setOnClickListener{
            val intent = Intent(this, GameModesActivity::class.java)
            intent.putExtra("token", TOKENSPOTIFY)
            startActivity(intent)
        }

        val builder =
            AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)

        builder.setScopes(arrayOf("streaming"))
        val request = builder.build()

        AuthorizationClient.openLoginActivity(this, LoginActivity.REQUEST_CODE, request)
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
}