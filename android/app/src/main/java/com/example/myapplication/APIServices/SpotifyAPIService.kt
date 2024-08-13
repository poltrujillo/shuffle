package com.example.myapplication.APIServices

import com.example.myapplication.dataModels.Spotify.PlayList
import com.example.myapplication.dataModels.Spotify.MyPlaylists
import com.example.myapplication.dataModels.LaravelServer.UserResponse
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header
import retrofit2.http.Path

//interficie para preparar todas las API call que vamos ha usar con Spotify
interface SpotifyAPIService {
    @GET("me")
    open fun getUserInfo(@Header("Authorization") bearerToken: String?): Call<UserResponse>
    @GET("playlists/{playlistId}")
    fun getPlaylist(
        @Header("Authorization") accessToken: String,
        @Path("playlistId") playlistId: String
    ): Call<PlayList>
    @GET("me/playlists")
    fun getMyPlaylist(
        @Header("Authorization") accessToken: String
    ): Call<MyPlaylists>
}