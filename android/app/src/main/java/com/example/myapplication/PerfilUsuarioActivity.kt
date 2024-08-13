package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.APIServices.LaravelAPIService
import com.example.myapplication.dataModels.Global.BASE_URL_LARAVEL
import com.example.myapplication.dataModels.Global.BASE_URL_LARAVEL_SHORT
import com.example.myapplication.dataModels.LaravelServer.UserResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class PerfilUsuarioActivity : AppCompatActivity() {
    private lateinit var fotoPerfil: ImageView
    private var user: UserResponse = UserResponse(1, "Carla", "carla@gmail.com", "image", 0, 0, "ds", "sd")
    private lateinit var currentPhotoUri: Uri
    private lateinit var retrofit: Retrofit
    private lateinit var apiService: LaravelAPIService
    private lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_usuario)

        val nombre = findViewById<TextView>(R.id.textView35)
        val correo = findViewById<TextView>(R.id.textView39)
        val password = findViewById<TextView>(R.id.textView40)
        val botonEdit = findViewById<Button>(R.id.button16)
        val botonLogout = findViewById<Button>(R.id.button17)
        context = this

        val sharedPrefs = getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)

        // Recupera el JSON de user almacenado en SharedPreferences
        val userJson = sharedPrefs.getString("user", "")
        val token = sharedPrefs.getString("token", "")


        // Verifica si el JSON de user no es nulo ni está vacío
        if (!userJson.isNullOrEmpty()) {
            // Deserializa el JSON de user de nuevo a un objeto User utilizando Gson
            val gson = Gson()
            user = gson.fromJson(userJson, UserResponse::class.java)

            nombre.text = user.username
            correo.text = user.email
            password.text = "*********"

        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        botonLogout.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        botonEdit.setOnClickListener{
            val intent = Intent(this, EditPerfilUsuarioActivity::class.java)
            startActivity(intent)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView5)

        // Establecer el listener para manejar los clics en los elementos del menú
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.ranking -> {
                    // Si se hace clic en el primer elemento, iniciar la Activity deseada
                    startActivity(Intent(this, RankingActivity::class.java))
                    true
                } R.id.account -> {
                // Si se hace clic en el primer elemento, iniciar la Activity deseada
                startActivity(Intent(this, PerfilUsuarioActivity::class.java))
                true
            } R.id.main -> {
                // Si se hace clic en el primer elemento, iniciar la Activity deseada
                startActivity(Intent(this, MainActivity::class.java))
                true
            } R.id.help -> {
                // Si se hace clic en el primer elemento, iniciar la Activity deseada
                startActivity(Intent(this, HelpActivity::class.java))
                true
            } R.id.about -> {
                // Si se hace clic en el primer elemento, iniciar la Activity deseada
                startActivity(Intent(this, AboutActivity::class.java))
                true
            }
                else -> false
            }
        }

        fotoPerfil = findViewById(R.id.imageView19)
        var imageUrl = BASE_URL_LARAVEL_SHORT + user.image

        // Cargar la imagen utilizando Glide
        Glide.with(this)
            .load(imageUrl)
            .into(fotoPerfil)

        fotoPerfil.setOnClickListener {
            openGallery()
        }

        // Inicializar Retrofit
        retrofit = getRetrofit()
    }

    private val selectImageFromGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageUri: Uri? = result.data?.data
                // Aquí puedes cargar la imagen en tu ImageView y guardar la URI en una variable
                fotoPerfil.setImageURI(imageUri)
                var currentPhotoUri = imageUri!!

                Log.e("PergilUsuario", currentPhotoUri.toString())
                uploadImageToServer(currentPhotoUri)
            }
        }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        selectImageFromGallery.launch(galleryIntent)
    }

    private fun uploadImageToServer(imageUri: Uri) {
        val file = File(getRealPathFromURI(imageUri))
        val mediaType = "multipart/form-data".toMediaTypeOrNull()
        val requestBody = file.asRequestBody(mediaType)
        val multipartBody = MultipartBody.Part.createFormData("image", file.name, requestBody)
        apiService = retrofit.create(LaravelAPIService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                apiService.updateImage(user.id.toString(), multipartBody)
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>,response: Response<ResponseBody>) {
                            if (response.isSuccessful){
                                Log.e("UpdateImg", "Foto actualizada")

                            } else {
                                Log.e("UpdateImg", "Foto no actualizada: ${response.message()}")
                                Toast.makeText(context, "Error al actualizar la foto", Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.e("UpdateImg", "Foto no actualizada: ${t.message.toString()}")
                            Toast.makeText(context, "Error al actualizar la foto", Toast.LENGTH_SHORT).show()
                        }

                    })

            } catch (e: Exception) {
                Log.e("UpdateImage", "Error: ${e.message}", e)
            }
        }
    }

    private fun getRealPathFromURI(uri: Uri): String {
        var realPath = ""
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            realPath = cursor.getString(column_index)
            cursor.close()
        }
        return realPath
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_LARAVEL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}