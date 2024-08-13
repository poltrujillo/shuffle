package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.dataModels.Server.Jugadores
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ListAdapterGameplay(val items:MutableList<Jugadores>, val context: Context): RecyclerView.Adapter<ListAdapterGameplay.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_jugador_gameplay, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
        //Devuelve numero de items que hay en la array
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvItem.text = item.nombre
        holder.tvNumber.text = item.puntuacion.toInt().toString()
        if (item.listo){
            holder.tvItem.setBackgroundColor(ContextCompat.getColor(holder.context, R.color.green))
            holder.tvItem.setTextColor(ContextCompat.getColor(holder.context, R.color.white))
        } else
        {
            holder.tvItem.setBackgroundColor(ContextCompat.getColor(holder.context, R.color.white))
            holder.tvItem.setTextColor(ContextCompat.getColor(holder.context, R.color.black))
        }

        // Boton para ir al detail y le pasamos la id de nuestro personaje
        holder.tvItem.setOnClickListener{
        }


    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvItem = view.findViewById<TextView>(R.id.item_rv)
        val tvNumber = view.findViewById<TextView>(R.id.textView47)
        val context = view.context
        //La clase que representa la llista de items.
    }


}