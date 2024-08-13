package com.example.myapplication.dataModels.LaravelServer

import com.fasterxml.jackson.annotation.JsonProperty

data class GetUserResponse(
    val user: UserResponse,
)

data class UserResponse(
    val id: Long,
    val username: String,
    val email: String,
    val image: String,
    @JsonProperty("is_admin")
    val isAdmin: Long,
    @JsonProperty("is_premium")
    val isPremium: Long,
    @JsonProperty("created_at")
    val createdAt: String,
    @JsonProperty("updated_at")
    val updatedAt: String,
)