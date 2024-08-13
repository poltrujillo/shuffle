package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.dataModels.Spotify.MyTrack
import com.example.myapplication.dataModels.Spotify.Tracks
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.random.Random

class ConfigurarPartidaActivity : AppCompatActivity() {
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
            
            goToGame(valorSeleccionado, segundos.text.toString().toInt(), !switch.isChecked)
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
            } R.id.about -> {
                // Si se hace clic en el primer elemento, iniciar la Activity deseada
                startActivity(Intent(this, AboutActivity::class.java))
                true
            }
                else -> false
            }
        }
    }

    fun goToGame(rondas:Int, tiempo:Int, modoDificil: Boolean)
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
        val intent = Intent(this@ConfigurarPartidaActivity, SinglePlayerGameplay::class.java)
        intent.putExtra("trackList", ArrayList(trackList)) // Convert MutableList to ArrayList
        intent.putExtra("tiempo", tiempo)
        intent.putExtra("modoDificil", modoDificil)
        intent.putExtra("modeId", modeId)
        startActivity(intent)
    }
}