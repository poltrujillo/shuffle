package com.example.myapplication.dataModels.Spotify

import com.google.gson.annotations.SerializedName



//Classe Usuario para guardar todo lo que queramos
data class User (@SerializedName("id") var userId:String)