package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.echo.holographlibrary.Bar
import com.echo.holographlibrary.BarGraph
import com.example.myapplication.databinding.ActivityMainBinding
import java.text.DecimalFormat
import kotlin.math.roundToInt

class ResultSinglePlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_single_player)
        val volver = findViewById<Button>(R.id.button8)
        val salir = findViewById<Button>(R.id.button9)
        val puntos = ArrayList<Bar>()
        val puntuacion = intent.getFloatExtra("puntos", 0.0f)
        graficarBarras(puntos, puntuacion)

        volver.setOnClickListener{
            val intent = Intent(this, GameModesActivity::class.java)
            startActivity(intent)
        }

        salir.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    fun graficarBarras(puntos: ArrayList<Bar>, puntuacion: Float) {
        val numeroFormateado = puntuacion.toInt().toFloat()

        val barra = Bar()
        var color = generarColorHexAleatorio()
        barra.color = Color.parseColor(color)
        barra.name = "Player1 - Puntos"
        barra.value = numeroFormateado
        puntos.add(barra)

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