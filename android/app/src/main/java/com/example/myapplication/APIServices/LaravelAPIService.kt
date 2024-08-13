package com.example.myapplication.APIServices

import com.example.myapplication.dataModels.LaravelServer.CreateGameRequest
import com.example.myapplication.dataModels.LaravelServer.GetUserResponse
import com.example.myapplication.dataModels.LaravelServer.LoginRequest
import com.example.myapplication.dataModels.LaravelServer.LoginResponse
import com.example.myapplication.dataModels.LaravelServer.RegisterRequest
import com.example.myapplication.dataModels.LaravelServer.UpdatePwd
import com.example.myapplication.dataModels.LaravelServer.UpdateUserRequest
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface LaravelAPIService {
    @POST("register")
    fun register(@Body request: RegisterRequest): Call<ResponseBody>

    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("games")
    fun createGame(@Body request: CreateGameRequest): Call<ResponseBody>

    @GET("auth/user")
    fun getUser(): Call<GetUserResponse>

    @PUT("users/{id}")
    fun updateUser(@Path("id") id: String, @Body request: UpdateUserRequest): Call<GetUserResponse>

    @PUT("users/password/{id}")
    fun updatePwd(@Path("id") id: String, @Body request: UpdatePwd): Call<GetUserResponse>

    @Multipart
    @POST("users/image/{id}")
    fun updateImage(@Path("id") id: String, @Part file: MultipartBody.Part): Call<ResponseBody>
}