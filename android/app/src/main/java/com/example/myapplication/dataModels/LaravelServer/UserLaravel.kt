package com.example.myapplication.dataModels.LaravelServer

import java.io.Serializable

data class UserLaravel (
    var username: String? = null,
    var email: String? = null,
    var password: String? = null,
    var image: String? = null
): Serializable {

}
