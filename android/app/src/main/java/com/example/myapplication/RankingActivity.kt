package com.example.myapplication

import ListAdapterRanking
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.dataModels.Firebase.JugadorRanking
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore

class RankingActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListAdapterRanking
    private var dataList = mutableListOf<JugadorRanking>()
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        // Inicializa el RecyclerView
        recyclerView = findViewById(R.id.recyclerViewRanking)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Obtener y mostrar los datos de Firebase
        obtenerDatosDeFirebase()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationViewRank)

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

    private fun obtenerDatosDeFirebase() {
        val rankingRef = db.collection("ranking")
        val dataList = mutableListOf<JugadorRanking>()

        val rankingListener = EventListener<QuerySnapshot> { querySnapshot, e ->
            if (e != null) {
                Log.e("Firebase", "Error al obtener documentos: ", e)
                return@EventListener
            }

            dataList.clear() // Limpiar la lista antes de agregar nuevos datos

            val sortedQuerySnapshot = querySnapshot?.documents?.sortedByDescending { it.getLong("score") ?: 0 }
            // Ordenar los documentos por score de mayor a menor

            sortedQuerySnapshot?.forEach { document ->
                val username = document.getString("username") ?: ""
                val score = document.getLong("score")?.toInt() ?: 0
                val jugador = JugadorRanking(score, username)
                dataList.add(jugador)
            }

            // Después de obtener los datos, crea y establece el adaptador
            val adapter = ListAdapterRanking(dataList)
            recyclerView.adapter = adapter
        }

        rankingRef.addSnapshotListener(rankingListener)
    }


}