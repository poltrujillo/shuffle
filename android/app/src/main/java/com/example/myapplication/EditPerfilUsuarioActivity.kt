package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.APIServices.LaravelAPIService
import com.example.myapplication.dataModels.LaravelServer.AuthInterceptor
import com.example.myapplication.dataModels.Global.BASE_URL_LARAVEL
import com.example.myapplication.dataModels.LaravelServer.GetUserResponse
import com.example.myapplication.dataModels.LaravelServer.UpdateUserRequest
import com.example.myapplication.dataModels.LaravelServer.UserResponse
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EditPerfilUsuarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_perfil_usuario)

        val nombreUser = findViewById<EditText>(R.id.editTextText3)
        val correoUser = findViewById<EditText>(R.id.editTextText4)
        val botonGuardar = findViewById<Button>(R.id.button18)
        val botonCancelar = findViewById<Button>(R.id.button19)
        val botonCambiarPwd = findViewById<Button>(R.id.button20)
        val context = this

        val sharedPrefs = getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)

        // Recupera el JSON de user almacenado en SharedPreferences
        val userJson = sharedPrefs.getString("user", "")
        val token = sharedPrefs.getString("token", "")

        // Verifica si el JSON de user no es nulo ni está vacío
        if (!userJson.isNullOrEmpty()) {
            // Deserializa el JSON de user de nuevo a un objeto User utilizando Gson
            val gson = Gson()
            val user = gson.fromJson(userJson, UserResponse::class.java)

            nombreUser.setText(user.username)
            correoUser.setText(user.email)

            botonGuardar.setOnClickListener {
                val nuevoNombre = nombreUser.text.toString()
                val nuevoCorreo = correoUser.text.toString()
                val customBorder = ContextCompat.getDrawable(this, R.drawable.edit_text_error)

                token?.let { authToken ->
                    val interceptor = AuthInterceptor(authToken)
                    val okHttpClient = OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .build()

                    val retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL_LARAVEL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okHttpClient)
                        .build()

                    val apiService = retrofit.create(LaravelAPIService::class.java)

                    val updateRequest = UpdateUserRequest(nuevoNombre, nuevoCorreo)
                    val userId = user.id.toString()

                    apiService.updateUser(userId, updateRequest)
                        .enqueue(object : Callback<GetUserResponse> {
                            override fun onResponse(call: Call<GetUserResponse>, response: Response<GetUserResponse>) {
                                if (response.isSuccessful) {
                                    val responseUpdate = response.body()
                                    val userUpdated = responseUpdate?.user
                                    Log.e("Updated User", "$userUpdated")

                                    val userJsonUpdate = gson.toJson(userUpdated)

                                    // Guardar el usuario y el token en SharedPreferences
                                    val editor = sharedPrefs.edit()
                                    editor.putString("user", userJsonUpdate)
                                    editor.apply()
                                    val intent = Intent(context, PerfilUsuarioActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    Log.e("UpdateUser", "Error al actualizar usuario: ${response.message()}")
                                    Toast.makeText(context, "Error al actualizar usuario", Toast.LENGTH_LONG).show()
                                    nombreUser.background = customBorder
                                    correoUser.background = customBorder
                                }
                            }

                            override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                                Log.e("UpdateUser", "Error: ${t.message}")
                            }
                        })
                }
            }

            botonCancelar.setOnClickListener {
                val intent = Intent(context, PerfilUsuarioActivity::class.java)
                startActivity(intent)
            }

            botonCambiarPwd.setOnClickListener {
                val intent = Intent(context, CambiarContrasenaPerfilActivity::class.java)
                startActivity(intent)
            }

        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}