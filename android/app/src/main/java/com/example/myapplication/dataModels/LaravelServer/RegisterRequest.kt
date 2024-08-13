package com.example.myapplication.dataModels.LaravelServer

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)