package com.example.myapplication

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
import com.example.myapplication.dataModels.Global.BASE_URL_LARAVEL
import com.example.myapplication.dataModels.LaravelServer.RegisterRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var apiService: LaravelAPIService
    private var isPasswordVisible = false
    private var isPasswordVisible2 = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val username = findViewById<EditText>(R.id.editTextText2)
        val correo = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val password = findViewById<EditText>(R.id.editTextTextPassword)
        val confirmPasswd = findViewById<EditText>(R.id.editTextTextPassword2)
        val crearCuenta = findViewById<Button>(R.id.button10)
        val irLogin = findViewById<TextView>(R.id.textView18)
        val image1 = findViewById<ImageView>(R.id.imageView14)
        val image2 = findViewById<ImageView>(R.id.imageView15)

        val retrofit = getRetrofit()

        // Crear una instancia del servicio de API
        apiService = retrofit.create(LaravelAPIService::class.java)

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

        image2.setOnClickListener{
            if (isPasswordVisible2) {
                // Mostrar la contraseña
                confirmPasswd.transformationMethod = null
                image2.setImageResource(R.drawable.baseline_visibility_24)
            } else {
                // Ocultar la contraseña
                confirmPasswd.transformationMethod = PasswordTransformationMethod.getInstance()
                image2.setImageResource(R.drawable.baseline_visibility_off_24)
            }

            // Cambiar el estado del indicador de visibilidad
            isPasswordVisible2 = !isPasswordVisible2
        }

        irLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        crearCuenta.setOnClickListener {
            val passwordValue = password.text.toString()
            val confirmPasswdValue = confirmPasswd.text.toString()
            val customBorder = ContextCompat.getDrawable(this, R.drawable.edit_text_error)

            val context = this

            if (passwordValue != confirmPasswdValue) {
                Toast.makeText(context, "Las contraseñas no coinciden!", Toast.LENGTH_SHORT).show()
                password.background = customBorder
                confirmPasswd.background = customBorder
            } else if (username.text.isEmpty() || correo.text.isEmpty() || password.text.isEmpty() || confirmPasswd.text.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_LONG).show()
            } else {
                // Aquí se crea directamente la instancia de RegisterRequest sin necesidad de un RequestBody manual
                val request = RegisterRequest(username.text.toString(), correo.text.toString(), password.text.toString())

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        apiService.register(request)
                            .enqueue(object : Callback<ResponseBody> {
                                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                    if (response.isSuccessful) {
                                        Log.e("Register", "Registro correcto!")

                                        val intent = Intent(context, LoginActivity::class.java)
                                        startActivity(intent)
                                    } else {
                                        Log.e("Register", "Fallo" + response.message())

                                        username.background = customBorder
                                        correo.background = customBorder
                                        password.background = customBorder
                                        confirmPasswd.background = customBorder

                                        // Muestra un mensaje de error
                                        Toast.makeText(context, "Registro fallido. Por favor, revise los campos e inténtelo de nuevo.", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    Log.e("Register", "${t.message}")
                                }
                            })
                    } catch (e: Exception) {
                        Log.e("Register", "Error: ${e.message}", e)
                    }
                }
            }
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_LARAVEL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}