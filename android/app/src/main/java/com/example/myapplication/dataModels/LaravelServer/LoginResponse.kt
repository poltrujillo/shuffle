package com.example.myapplication.dataModels.LaravelServer

data class LoginResponse(
    val user: UserLaravel,
    val token: String,
    val is_admin: Int
)