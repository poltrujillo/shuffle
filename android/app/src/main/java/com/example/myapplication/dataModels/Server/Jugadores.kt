package com.example.myapplication.dataModels.Server


import java.io.Serializable

data class Jugadores(
    var nombre: String = "",
    var puntuacion: Double = 1.0,
    var listo: Boolean = false
) : Serializable

