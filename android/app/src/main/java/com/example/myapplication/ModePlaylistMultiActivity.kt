package com.example.myapplication

import PlaylistAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.APIServices.SpotifyAPIService
import com.example.myapplication.dataModels.Spotify.PlayList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ModePlaylistMultiActivity : AppCompatActivity() {
    var posicion = 0
    var token = ""
    private var modeId = 0
    var playlistList : List<PlayList> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode_playlist_multi)
        token = intent.getStringExtra("token").toString()
        modeId = intent.getIntExtra("modeId", modeId)
        playlistList = intent.getParcelableArrayListExtra<PlayList>("myPlaylists") ?: emptyList()
        val spinner: Spinner = findViewById(R.id.spinner4)
        val botonSelect: Button = findViewById(R.id.button7)
        val imagen: ImageView = findViewById(R.id.imageView8)
        val botonCancel: Button = findViewById(R.id.button15)

        botonCancel.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val adapter = PlaylistAdapter(this, playlistList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val elementoSeleccionado = playlistList[position]
                posicion = position
                val imagenUrl = elementoSeleccionado.images.firstOrNull()?.url
                if (!imagenUrl.isNullOrEmpty()) {
                    Glide.with(this@ModePlaylistMultiActivity)
                        .load(imagenUrl)
                        .apply(RequestOptions().centerCrop())
                        .into(imagen) // Carga la imagen en el ImageView usando Glide
                } else {
                    // Si no hay URL de imagen, puedes mostrar una imagen de placeholder o hacer cualquier otra cosa
                    imagen.setImageResource(R.drawable.ic_launcher_foreground)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No hacer nada cuando no se selecciona nada
            }
        }
        botonSelect.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Perform network request
                    val call = getRetrofit().create(SpotifyAPIService::class.java)
                        .getPlaylist("Authorization: Bearer " + token, playlistList[posicion].spotifyId )
                    val response = call.execute()

                    // Check if the request was successful
                    if (response.isSuccessful) {

                        // Once the loop is done, start the new activity
                        val intent = Intent(this@ModePlaylistMultiActivity, ConfigurarPartidaMultiActivity::class.java)
                        val posts = response.body() as PlayList
                        intent.putExtra("trackList", posts.tracks) // Convert MutableList to ArrayList
                        intent.putExtra("token", token)
                        intent.putExtra("modeId", modeId)
                        intent.putExtra("id",posts.spotifyId)
                        startActivity(intent)
                    } else {
                        Log.e("MainActivity", "Error: ${response.code()} - ${response.message()}")
                    }
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error: ${e.message}", e)
                }
            }
        }

    }
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.spotify.com/v1/")
            .addConverterFactory(GsonConverterFactory.create()) .build()
    }
}