package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.APIServices.LaravelAPIService
import com.example.myapplication.dataModels.LaravelServer.AuthInterceptor
import com.example.myapplication.dataModels.Global.BASE_URL_LARAVEL
import com.example.myapplication.dataModels.LaravelServer.GetUserResponse
import com.example.myapplication.dataModels.LaravelServer.UpdatePwd
import com.example.myapplication.dataModels.LaravelServer.UserResponse
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CambiarContrasenaPerfilActivity : AppCompatActivity() {
    private lateinit var apiService: LaravelAPIService
    private var isPasswordVisible = false
    private var isPasswordVisible2 = false
    private var isPasswordVisible3 = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambiar_contrasena_perfil)

        val actualPwd = findViewById<EditText>(R.id.editTextTextPassword4)
        val newPwd = findViewById<EditText>(R.id.editTextTextPassword5)
        val confirmNewPwd = findViewById<EditText>(R.id.editTextTextPassword6)
        val botonGuardar = findViewById<Button>(R.id.button21)
        val botonCancelar = findViewById<Button>(R.id.button22)
        val image1 = findViewById<ImageView>(R.id.imageView24)
        val image2 = findViewById<ImageView>(R.id.imageView25)
        val image3 = findViewById<ImageView>(R.id.imageView26)
        val context = this

        image1.setOnClickListener{
            if (isPasswordVisible) {
                // Mostrar la contraseña
                actualPwd.transformationMethod = null
                image1.setImageResource(R.drawable.baseline_visibility_24)
            } else {
                // Ocultar la contraseña
                actualPwd.transformationMethod = PasswordTransformationMethod.getInstance()
                image1.setImageResource(R.drawable.baseline_visibility_off_24)
            }

            // Cambiar el estado del indicador de visibilidad
            isPasswordVisible = !isPasswordVisible
        }

        image2.setOnClickListener{
            if (isPasswordVisible2) {
                // Mostrar la contraseña
                newPwd.transformationMethod = null
                image2.setImageResource(R.drawable.baseline_visibility_24)
            } else {
                // Ocultar la contraseña
                newPwd.transformationMethod = PasswordTransformationMethod.getInstance()
                image2.setImageResource(R.drawable.baseline_visibility_off_24)
            }

            // Cambiar el estado del indicador de visibilidad
            isPasswordVisible2 = !isPasswordVisible2
        }

        image3.setOnClickListener{
            if (isPasswordVisible3) {
                // Mostrar la contraseña
                confirmNewPwd.transformationMethod = null
                image3.setImageResource(R.drawable.baseline_visibility_24)
            } else {
                // Ocultar la contraseña
                confirmNewPwd.transformationMethod = PasswordTransformationMethod.getInstance()
                image3.setImageResource(R.drawable.baseline_visibility_off_24)
            }

            // Cambiar el estado del indicador de visibilidad
            isPasswordVisible3 = !isPasswordVisible3
        }

        botonGuardar.setOnClickListener {
            val actualPassword = actualPwd.text.toString()
            val newPassword = newPwd.text.toString()
            val confirmNewPassword = confirmNewPwd.text.toString()
            val customBorder = ContextCompat.getDrawable(this, R.drawable.edit_text_error)

            if (newPassword == confirmNewPassword) {
                // Aquí realizas la llamada a la API para actualizar la contraseña
                val sharedPrefs = getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
                val token = sharedPrefs.getString("token", "")
                val userJson = sharedPrefs.getString("user", "")

                if (!userJson.isNullOrEmpty()) {
                    // Deserializa el JSON de user de nuevo a un objeto User utilizando Gson
                    val gson = Gson()
                    val user = gson.fromJson(userJson, UserResponse::class.java)

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

                        apiService = retrofit.create(LaravelAPIService::class.java)


                        val userId = user.id.toString()
                        val updatePwdRequest = UpdatePwd(actualPassword, newPassword)

                        apiService.updatePwd(userId, updatePwdRequest)
                            .enqueue(object : Callback<GetUserResponse> {
                                override fun onResponse(call: Call<GetUserResponse>, response: Response<GetUserResponse>) {
                                    if (response.isSuccessful) {
                                        val responseUpdate = response.body()
                                        val userUpdated = responseUpdate?.user
                                        Log.e("UpdatedPwd", "$userUpdated")

                                        val userJsonUpdate = gson.toJson(userUpdated)

                                        // Guardar el usuario y el token en SharedPreferences
                                        val editor = sharedPrefs.edit()
                                        editor.putString("user", userJsonUpdate)
                                        editor.apply()
                                        val intent = Intent(context, PerfilUsuarioActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Log.e("UpdatePwd", "Error al actualizar password: ${response.message()}")
                                        Toast.makeText(context, "Error al actualizar password", Toast.LENGTH_LONG).show()
                                        actualPwd.background = customBorder
                                        newPwd.background = customBorder
                                        confirmNewPwd.background = customBorder
                                    }
                                }

                                override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                                    Log.e("UpdatePwd", "Error: ${t.message}")
                                }
                            })
                    }
                }
            } else {
                newPwd.background = customBorder
                confirmNewPwd.background = customBorder
                // Mostrar un mensaje de error indicando que las contraseñas no coinciden
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            }
        }

        botonCancelar.setOnClickListener{
            val intent = Intent(this, PerfilUsuarioActivity::class.java)
            startActivity(intent)
        }
    }
}