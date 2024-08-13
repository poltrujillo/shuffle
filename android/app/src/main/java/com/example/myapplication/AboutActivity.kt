package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView2)

        // Establecer el listener para manejar los clics en los elementos del menú
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.ranking -> {
                    // Si se hace clic en el primer elemento, iniciar la Activity deseada
                    startActivity(Intent(this, RankingActivity::class.java))
                    true
                }

                R.id.account -> {
                    // Si se hace clic en el primer elemento, iniciar la Activity deseada
                    startActivity(Intent(this, PerfilUsuarioActivity::class.java))
                    true
                }

                R.id.main -> {
                    // Si se hace clic en el primer elemento, iniciar la Activity deseada
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }

                R.id.help -> {
                    // Si se hace clic en el primer elemento, iniciar la Activity deseada
                    startActivity(Intent(this, HelpActivity::class.java))
                    true
                }

                R.id.about -> {
                    // Si se hace clic en el primer elemento, iniciar la Activity deseada
                    startActivity(Intent(this, AboutActivity::class.java))
                    true
                }

                else -> false
            }
        }
    }
}