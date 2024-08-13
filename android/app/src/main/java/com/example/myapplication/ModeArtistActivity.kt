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

class ModeArtistActivity : AppCompatActivity() {
    var posicion = 0
    var token = ""
    private var modeId = 0
    val SpotyId = listOf("37i9dQZF1DX6bnzK9KPvrz","37i9dQZF1DWWxPM4nWdhyI","37i9dQZF1DWYlCv3D85m6m","37i9dQZF1DZ06evO0nVzAk"
    ,"37i9dQZF1DWWqjEVD8TBr9","37i9dQZF1DZ06evO3tJsDm")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode_artist)
        token = intent.getStringExtra("token").toString()
        modeId = intent.getIntExtra("modeId", modeId)
        val spinner: Spinner = findViewById(R.id.spinner2)
        val imageView: ImageView = findViewById(R.id.imageView6)
        val botonSelect: Button = findViewById(R.id.button5)
        val botonCancel : Button = findViewById(R.id.button13)

        botonCancel.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val elementos = listOf("The weekend", "Ed Sheeran", "Twice", "ABBA", "Stray Kids", "C.Tangana")
        val imagenes = mapOf(
            "The weekend" to R.drawable.weekend,
            "Ed Sheeran" to R.drawable.edsheran,
            "Twice" to R.drawable.twice,
            "ABBA" to R.drawable.abba,
            "Stray Kids" to R.drawable.straykids,
            "C.Tangana" to R.drawable.ctangana
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
                    val intent = Intent(this@ModeArtistActivity, ConfigurarPartidaActivity::class.java)
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