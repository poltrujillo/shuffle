package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.APIServices.SpotifyAPIService
import com.example.myapplication.dataModels.Spotify.MyPlaylists
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GameModesActivity : AppCompatActivity() {
    val list = mutableListOf<CarouselItem>()
    var token = ""
    private var modeId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_modes)
        token = intent.getStringExtra("token").toString()

        val carousel: ImageCarousel = findViewById(R.id.carousel)

        list.add(CarouselItem(R.drawable.artistmode, "Artist"))
        list.add(CarouselItem(R.drawable.genre, "Genre"))
        list.add(CarouselItem(R.drawable.playlist, "Playlist"))

        carousel.carouselListener = object : CarouselListener {
            override fun onClick(position: Int, carouselItem: CarouselItem) {
                if (carouselItem.caption == "Artist"){
                    modeId = 2
                    val intent = Intent(this@GameModesActivity, ModeArtistActivity::class.java)
                    intent.putExtra("token", token)
                    intent.putExtra("modeId", modeId)
                    startActivity(intent)
                } else if (carouselItem.caption == "Genre") {
                    modeId = 1
                    val intent = Intent(this@GameModesActivity, ModeGenreActivity::class.java)
                    intent.putExtra("token", token)
                    intent.putExtra("modeId", modeId)
                    startActivity(intent)
                } else if (carouselItem.caption == "Playlist") {
                    goToPlaylist()
                }
            }

            override fun onLongClick(position: Int, dataObject: CarouselItem) {
                // ...
            }

        }

        carousel.addData(list)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

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
                } R.id.about -> {
                // Si se hace clic en el primer elemento, iniciar la Activity deseada
                startActivity(Intent(this, AboutActivity::class.java))
                true
                }
                else -> false
            }
        }
    }
    private fun goToPlaylist()
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val call = getRetrofit().create(SpotifyAPIService::class.java)
                    .getMyPlaylist("Authorization: Bearer " + token)
                val response = call.execute()
                if (response.isSuccessful) {
                    modeId = 3
                    val posts = response.body() as MyPlaylists
                    val intent = Intent(this@GameModesActivity, ModePlaylistActivity::class.java)
                    intent.putExtra("token", token)
                    intent.putExtra("myPlaylists", ArrayList(posts.items))
                    intent.putExtra("modeId", modeId)
                    startActivity(intent)

                }else {
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