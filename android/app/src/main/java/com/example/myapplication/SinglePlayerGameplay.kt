package com.example.myapplication

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.APIServices.LaravelAPIService
import com.example.myapplication.dataModels.Global.BASE_URL_LARAVEL
import com.example.myapplication.dataModels.LaravelServer.AuthInterceptor
import com.example.myapplication.dataModels.LaravelServer.CreateGameRequest
import com.example.myapplication.dataModels.LaravelServer.UserResponse
import com.example.myapplication.dataModels.Spotify.MyTrack
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SinglePlayerGameplay : AppCompatActivity() {
    private val clientId = "a4347d0dc7f3407eb136e85da349e7e1"
    private val redirectUri = "shuffleapp://callback"
    private var spotifyAppRemote: SpotifyAppRemote? = null
    private var playing = false
    var tracksArray: List<MyTrack> = emptyList()
    private val handler = Handler(Looper.getMainLooper())
    private var currentIndex = 0
    private lateinit var textView: TextView
    private lateinit var textViewPuntuacion: TextView
    private lateinit var textViewCountdown: TextView
    private lateinit var editText: EditText
    private lateinit var button: Button
    private var hardMode = true
    private var roundTime = 0
    private var countdown = 31 // Poner el tiempo de repdorucion + 1
    private var puntuacion = 0.0f
    private var modeId = 0
    private var user: UserResponse = UserResponse(1, "Carla", "carla@gmail.com", "image", 0, 0, "ds", "sd")
    private var userId = 0
    private lateinit var apiService: LaravelAPIService
    val db = Firebase.firestore
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_player_gameplay)
        tracksArray = intent.getParcelableArrayListExtra<MyTrack>("trackList") ?: emptyList()
        roundTime = intent.getIntExtra("tiempo", 0)
        hardMode = intent.getBooleanExtra("modoDificil", true)
        modeId = intent.getIntExtra("modeId", modeId)

        //Recoger Usuario activo
        val sharedPrefs = getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)

        // Recupera el JSON de user almacenado en SharedPreferences
        val userJson = sharedPrefs.getString("user", "")
        val token = sharedPrefs.getString("token", "")

        // Verifica si el JSON de user no es nulo ni está vacío
        if (!userJson.isNullOrEmpty()) {
            // Deserializa el JSON de user de nuevo a un objeto User utilizando Gson
            val gson = Gson()
            user = gson.fromJson(userJson, UserResponse::class.java)
            userId = user.id.toInt()
            token?.let { authToken ->
                val interceptor = AuthInterceptor(authToken)
                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL_LARAVEL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()

                apiService = retrofit.create(LaravelAPIService::class.java)
            }
        } else {
            Toast.makeText(this, "Error al obtener el usuario", Toast.LENGTH_SHORT).show()
        }

        countdown = roundTime +1
        val builder =
            AuthorizationRequest.Builder(clientId, AuthorizationResponse.Type.TOKEN, redirectUri)

        builder.setScopes(arrayOf("streaming"))
        val request = builder.build()
        textView = findViewById(R.id.testTextView)
        textViewCountdown = findViewById(R.id.countdownSP)
        button = findViewById(R.id.button5)
        editText = findViewById(R.id.editTextText)
        textViewPuntuacion  = findViewById(R.id.PuntuacionSP)
        button.setOnClickListener{
            val userInput = editText.text.toString().trim().lowercase()
            val tocheck = tracksArray[currentIndex-1].name.substringBefore(" (").lowercase()
            editText.clearFocus()
            editText.text.clear()
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
            if (userInput.equals(tocheck))
            {
                handler.removeCallbacksAndMessages(null)
                val regex = Regex("\\d+")
                var number = regex.find(textViewPuntuacion.text.toString())?.value?.toFloat()
                val n  = countdown/roundTime.toFloat()
                val operation = n * 100
                number = number?.plus(operation)
                if (number != null) {
                    puntuacion = number
                }
                textViewPuntuacion.text = "Puntuacion: ${number?.toInt()}"
                countdown = 10
                handler.post(pausa)
            }
            else
            {
                shakeScreen()
                val textos = StringBuilder()
                if (!hardMode) {
                    for (i in tocheck.indices) {
                        if (i < userInput.length && userInput[i] == tocheck[i]) {
                            textos.append(tocheck[i])
                        } else {
                            if (!tocheck[i].equals(' ')) {
                                textos.append('-')
                            } else {
                                textos.append(' ')
                            }
                        }
                    }
                    textView.text = textos.toString().capitalize()
                    Log.d("MainActivity", textView.text.toString())
                }


            }
            editText.text.clear()
        }
        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()
        SpotifyAppRemote.connect(this, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.d("MainActivity", "Connected! Yay!")
                // Now you can start interacting with App Remote
                handler.postDelayed(playNextSong, 0)
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("MainActivity", throwable.message, throwable)
                // Something went wrong when attempting to connect! Handle errors here
            }
        })
        //AuthorizationClient.openLoginActivity(this, LoginActivity.REQUEST_CODE, request)
    }

    private val playNextSong: Runnable = object : Runnable {
        override fun run() {
            playing = true
            spotifyAppRemote?.let {
                if (currentIndex < tracksArray.size) {
                    button.isClickable = true
                    // Play a playlist
                    if (hardMode)
                    {
                        textView.text = ""
                    } else {
                        val textos = tracksArray[currentIndex].name.substringBefore(" (").map { if (it != ' ') '-' else ' ' }.joinToString("")
                        textView.text = textos
                    }


                    val trackURI = "spotify:track:" + tracksArray[currentIndex].trackId
                    it.playerApi.play(trackURI)
                    // Subscribe to PlayerSftate
                    it.playerApi.subscribeToPlayerState().setEventCallback { playerState ->
                        val track: Track = playerState.track
                        Log.d("MainActivity", track.name + " by " + track.artist.name)
                    }
                    currentIndex++
                    startCountdown()
                } else {
                    textView.text = "SE ACABO"
                    it.playerApi.pause()
                    handler.removeCallbacks(this)

                    // Verificar si existe un documento con el email especificado
                    db.collection("ranking").whereEqualTo("email", user.email)
                        .get()
                        .addOnSuccessListener { documents ->
                            if (documents.isEmpty) {
                                // Si no existe un documento con el email, crear uno nuevo
                                val user1 = hashMapOf(
                                    "email" to user.email,
                                    "username" to user.username,
                                    "score" to puntuacion.toInt()
                                )
                                db.collection("ranking").document()
                                    .set(user1)
                                    .addOnSuccessListener {
                                        Log.e("Firebase", "Nuevo usuario creado")
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e("Firebase", "Error al crear nuevo usuario", exception)
                                    }
                            } else {
                                // Si existe un documento con el email, actualizar el campo "score"
                                for (document in documents) {
                                    // Obtener el valor actual del campo "score"
                                    val currentScore = document.getLong("score") ?: 0

                                    // Calcular el nuevo valor sumando puntuacion
                                    val newScore = currentScore + puntuacion.toInt()

                                    // Actualizar el campo "score" en Firestore
                                    document.reference.update("score", newScore)
                                        .addOnSuccessListener {
                                            Log.e("Firebase", "Campo score actualizado exitosamente")
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.e("Firebase", "Error al actualizar campo score", exception)
                                        }
                                }
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.e("Firebase", "Error al obtener documentos: ", exception)
                        }

                    val request = CreateGameRequest(modeId, userId)

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            apiService.createGame(request)
                                .enqueue(object : Callback<ResponseBody> {
                                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                        if (response.isSuccessful) {
                                            Log.e("CrearPartida", "Partida creada!")
                                            //mover a la pagina de final de partida
                                            val intent = Intent(this@SinglePlayerGameplay, ResultSinglePlayerActivity::class.java)
                                            intent.putExtra("puntos", puntuacion)
                                            startActivity(intent)

                                        } else {
                                            Log.e("CrearPartida", "Fallo" + response.message())
                                            //mover a la pagina de final de partida
                                            val intent = Intent(this@SinglePlayerGameplay, ResultSinglePlayerActivity::class.java)
                                            intent.putExtra("puntos", puntuacion)
                                            startActivity(intent)
                                        }
                                    }

                                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                        Log.e("CrearPartida", "${t.message}")
                                        //mover a la pagina de final de partida
                                        val intent = Intent(this@SinglePlayerGameplay, ResultSinglePlayerActivity::class.java)
                                        intent.putExtra("puntos", puntuacion)
                                        startActivity(intent)
                                    }
                                })
                        } catch (e: Exception) {
                            Log.e("CrearPartida", "Error: ${e.message}", e)
                            //mover a la pagina de final de partida
                            val intent = Intent(this@SinglePlayerGameplay, ResultSinglePlayerActivity::class.java)
                            intent.putExtra("puntos", puntuacion)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    private fun startCountdown() {
        handler.post(object : Runnable {
            override fun run() {
                if (countdown > 0) {
                    countdown--
                    textViewCountdown.text = countdown.toString()
                    handler.postDelayed(this, 1000) // Update every second
                } else {
                    countdown = 10
                    handler.removeCallbacks(this)
                    editText.text.clear()
                    if (currentIndex < tracksArray.size) {
                        handler.post(pausa)
                    }
                    else{
                        textView.text = "SE ACABO"
                        spotifyAppRemote?.let {
                            it.playerApi.pause()
                        }

                        // Verificar si existe un documento con el email especificado
                        db.collection("ranking").whereEqualTo("email", user.email)
                            .get()
                            .addOnSuccessListener { documents ->
                                if (documents.isEmpty) {
                                    // Si no existe un documento con el email, crear uno nuevo
                                    val user1 = hashMapOf(
                                        "email" to user.email,
                                        "username" to user.username,
                                        "score" to puntuacion
                                    )
                                    db.collection("ranking").document()
                                        .set(user1)
                                        .addOnSuccessListener {
                                            Log.e("Firebase", "Nuevo usuario creado")
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.e("Firebase", "Error al crear nuevo usuario", exception)
                                        }
                                } else {
                                    // Si existe un documento con el email, actualizar el campo "score"
                                    for (document in documents) {
                                        // Obtener el valor actual del campo "score"
                                        val currentScore = document.getLong("score") ?: 0

                                        // Calcular el nuevo valor sumando puntuacion
                                        val newScore = currentScore + puntuacion

                                        // Actualizar el campo "score" en Firestore
                                        document.reference.update("score", newScore)
                                            .addOnSuccessListener {
                                                Log.e("Firebase", "Campo score actualizado exitosamente")
                                            }
                                            .addOnFailureListener { exception ->
                                                Log.e("Firebase", "Error al actualizar campo score", exception)
                                            }
                                    }
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.e("Firebase", "Error al obtener documentos: ", exception)
                            }

                        val request = CreateGameRequest(modeId, userId)

                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                apiService.createGame(request)
                                    .enqueue(object : Callback<ResponseBody> {
                                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                            if (response.isSuccessful) {
                                                Log.e("CrearPartida", "Partida creada!")
                                                //mover a la pagina de final de partida
                                                val intent = Intent(this@SinglePlayerGameplay, ResultSinglePlayerActivity::class.java)
                                                intent.putExtra("puntos", puntuacion)
                                                startActivity(intent)

                                            } else {
                                                Log.e("CrearPartida", "Fallo" + response.message())
                                                //mover a la pagina de final de partida
                                                val intent = Intent(this@SinglePlayerGameplay, ResultSinglePlayerActivity::class.java)
                                                intent.putExtra("puntos", puntuacion)
                                                startActivity(intent)
                                            }
                                        }

                                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                            Log.e("CrearPartida", "${t.message}")
                                            //mover a la pagina de final de partida
                                            val intent = Intent(this@SinglePlayerGameplay, ResultSinglePlayerActivity::class.java)
                                            intent.putExtra("puntos", puntuacion)
                                            startActivity(intent)
                                        }
                                    })
                            } catch (e: Exception) {
                                Log.e("CrearPartida", "Error: ${e.message}", e)
                                //mover a la pagina de final de partida
                                val intent = Intent(this@SinglePlayerGameplay, ResultSinglePlayerActivity::class.java)
                                intent.putExtra("puntos", puntuacion)
                                startActivity(intent)
                            }
                        }
                    }
                }
            }
        })
    }

    private val pausa: Runnable = object : Runnable {
        override fun run() {
            spotifyAppRemote?.let {
                textView.text = tracksArray[currentIndex-1].name
                button.isClickable = false
                startPauseCountdown()
            }
        }
    }
    private fun startPauseCountdown() {
        handler.post(object : Runnable {
            override fun run() {
                if (countdown > 0) {
                    textViewCountdown.text = countdown.toString()
                    countdown--
                    handler.postDelayed(this, 1000) // Update every second
                } else {
                    textView.text = "JUGANDO"
                    countdown = roundTime +1 // Poner el tiempo de repdorucion + 1
                    handler.removeCallbacks(this)
                    handler.post(playNextSong)
                }
            }
        })
    }

    private fun shakeScreen() {
        val duration: Long = 100 // Duration of one shake cycle in milliseconds
        val magnitude: Float = 10f // Magnitude of the shake in pixels

        val translationAnim = ObjectAnimator.ofFloat(textView, "translationX", -magnitude, magnitude)
        translationAnim.duration = duration
        translationAnim.repeatCount = 5 // Number of times to repeat the animation

        translationAnim.addUpdateListener {
            // Reverse the direction of the translation
            if (it.currentPlayTime >= duration / 2) {
                translationAnim.setFloatValues(magnitude, -magnitude)
            }
        }

        translationAnim.start()
    }
    override fun onPause() {
        spotifyAppRemote?.let {
            it.playerApi.pause()
        }
        handler.removeCallbacksAndMessages(null)
        super.onPause()
    }

    override fun onDestroy() {
        spotifyAppRemote?.let {
            it.playerApi.pause()
        }
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()


    }
    override fun onResume() {
        super.onResume()

        if (playing) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}