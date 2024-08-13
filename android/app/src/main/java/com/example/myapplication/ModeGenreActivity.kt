package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.APIServices.SpotifyAPIService
import com.example.myapplication.dataModels.Spotify.PlayList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ModeGenreActivity : AppCompatActivity() {
    var token = ""
    private var modeId = 0
    val SpotyId = listOf("37i9dQZF1EQncLwOalG3K7","37i9dQZF1EQpesGsmIyqcW","37i9dQZF1EQpj7X7UK8OOF","37i9dQZF1EQmg9rwHdCwFW"
    , "37i9dQZF1EIgtdfeeWwF7B", "37i9dQZF1EIgbjUtLiWmHt")
    var posicion = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode_genre)
        token = intent.getStringExtra("token").toString()
        modeId = intent.getIntExtra("modeId", modeId)

        val spinner: Spinner = findViewById(R.id.spinner3)
        val imageView: ImageView = findViewById(R.id.imageView5)
        val botonSelect: Button = findViewById(R.id.button4)
        val botonCancel: Button = findViewById(R.id.button14)

        botonCancel.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val elementos = listOf("Pop", "Kpop", "Rock", "Reggaeton", "Electronica", "Rap")
        val imagenes = mapOf(
            "Pop" to R.drawable.pop,
            "Kpop" to R.drawable.kpop,
            "Rock" to R.drawable.rock,
            "Reggaeton" to R.drawable.regeton,
            "Electronica" to R.drawable.electronica,
            "Rap" to R.drawable.rap
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, elementos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val elementoSeleccionado = elementos[position]
                posicion = position
                val imagenId = imagenes[elementoSeleccionado]
                if (imagenId != null) {
                    imageView.setImageResource(imagenId)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No hacer nada cuando no se selecciona nada
            }
        }

        botonSelect.setOnClickListener{
            goToConfigurar()
        }
    }
    fun goToConfigurar()
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Perform network request
                val call = getRetrofit().create(SpotifyAPIService::class.java)
                    .getPlaylist("Authorization: Bearer " + token,SpotyId[posicion] )
                val response = call.execute()

                // Check if the request was successful
                if (response.isSuccessful) {

                    // Once the loop is done, start the new activity
                    val intent = Intent(this@ModeGenreActivity, ConfigurarPartidaActivity::class.java)
                    val posts = response.body() as PlayList
                    intent.putExtra("trackList", posts.tracks) // Convert MutableList to ArrayList
                    intent.putExtra("token", token)
                    intent.putExtra("modeId", modeId)
                    startActivity(intent)
                } else {
                    Log.e("MainActivity", "Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error: ${e.message}", e)
            }
        }
    }
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.spotify.com/v1/")
            .addConverterFactory(GsonConverterFactory.create()) .build()
    }
}