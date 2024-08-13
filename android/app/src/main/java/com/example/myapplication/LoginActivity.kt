package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.APIServices.LaravelAPIService
import com.example.myapplication.dataModels.LaravelServer.AuthInterceptor
import com.example.myapplication.dataModels.Global.BASE_URL_LARAVEL
import com.example.myapplication.dataModels.LaravelServer.GetUserResponse
import com.example.myapplication.dataModels.LaravelServer.LoginRequest
import com.example.myapplication.dataModels.LaravelServer.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var apiService: LaravelAPIService
    private var isPasswordVisible = false
    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.editTextTextEmailAddress2)
        val password = findViewById<EditText>(R.id.editTextTextPassword3)
        val image1 = findViewById<ImageView>(R.id.imageView16)
        val irRegistro = findViewById<TextView>(R.id.textView20)
        val login = findViewById<Button>(R.id.button11)


        initRetrofit(token)

        image1.setOnClickListener{
            if (isPasswordVisible) {
                // Mostrar la contraseña
                password.transformationMethod = null
                image1.setImageResource(R.drawable.baseline_visibility_24)
            } else {
                // Ocultar la contraseña
                password.transformationMethod = PasswordTransformationMethod.getInstance()
                image1.setImageResource(R.drawable.baseline_visibility_off_24)
            }

            // Cambiar el estado del indicador de visibilidad
            isPasswordVisible = !isPasswordVisible
        }

        irRegistro.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        login.setOnClickListener {
            val customBorder = ContextCompat.getDrawable(this, R.drawable.edit_text_error)

            val context = this

            if (email.text.isEmpty() || password.text.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_LONG).show()
            } else {
                // Aquí se crea directamente la instancia de RegisterRequest sin necesidad de un RequestBody manual
                val request = LoginRequest(email.text.toString(), password.text.toString())

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        apiService.login(request)
                            .enqueue(object : Callback<LoginResponse> {
                                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                                    if (response.isSuccessful) {
                                        Log.e("Login", "Login correcto!")
                                        val loginResponse = response.body()
                                        token = loginResponse?.token.toString()

                                        // Actualiza Retrofit con el nuevo token
                                        updateRetrofitWithToken(token)

                                        apiService.getUser()
                                            .enqueue(object : Callback<GetUserResponse>{
                                                override fun onResponse(call: Call<GetUserResponse>, response: Response<GetUserResponse>) {
                                                    if (response.isSuccessful){
                                                        val responseUser = response.body()
                                                        val user = responseUser?.user
                                                        Log.e("Login", "$user")

                                                        // Convertir el objeto UserLaravel a JSON usando Gson
                                                        val gson = Gson()
                                                        val userJson = gson.toJson(user)

                                                        // Guardar el usuario y el token en SharedPreferences
                                                        val sharedPrefs = getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
                                                        val editor = sharedPrefs.edit()
                                                        editor.putString("user", userJson)
                                                        editor.putString("token", token)
                                                        editor.apply()

                                                    } else {
                                                        Log.e("Login", "Error al obtener usuario: ${response.message()}")
                                                    }
                                                }

                                                override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                                                    Log.e("Login", "Error al obtener usuario: ${t.message}")
                                                }
                                            })

                                        val intent = Intent(context, MainActivity::class.java)
                                        startActivity(intent)
                                    } else {
                                        Log.e("Login", "Fallo" + response.message())

                                        email.background = customBorder
                                        password.background = customBorder

                                        // Muestra un mensaje de error
                                        Toast.makeText(context, "Login fallido. Por favor, revise los campos e inténtelo de nuevo.", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                    Log.e("Login", "${t.message}")
                                }
                            })
                    } catch (e: Exception) {
                        Log.e("Login", "Error: ${e.message}", e)
                    }
                }
            }
        }
    }

    private fun initRetrofit(token: String) {
        val interceptor = AuthInterceptor(token)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_LARAVEL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        apiService = retrofit.create(LaravelAPIService::class.java)
    }

    // Función para actualizar Retrofit con un nuevo token
    private fun updateRetrofitWithToken(token: String) {
        initRetrofit(token)
    }
}