package com.example.myapplication.dataModels.LaravelServer

data class CreateGameRequest(
    val game_modes_id: Int,
    val user_id: Int
)