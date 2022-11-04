package com.example.shareapp.Activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.shareapp.APIClient
import com.example.shareapp.APIService
import com.example.shareapp.Helpers.Resizer
import com.example.shareapp.Helpers.Token
import com.example.shareapp.R
import com.example.shareapp.ResponseModels.Register
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

private const val REQUEST_CODE = 42
private const val FILE_NAME = "photo.jpg"
private lateinit var photoFile: File

class AddProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        val token = Token.getAccessToken(this)

        take_picture.setOnClickListener {
            photoFile = getPhotoFile(FILE_NAME)

            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val fileProvider = FileProvider.getUriForFile(this, "com.example.shareapp.fileprovider", photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            } else {
                Toast.makeText(this, "Kamera megnyitása sikertelen!", Toast.LENGTH_SHORT).show()
            }

        }
        add_product_button.setOnClickListener {

            val product_name = add_product_name.text.toString().trim()
            val description = add_description.text.toString().trim()
            val amount = add_amount.text.toString().trim()
            val expiration = add_expiration.text.toString().trim()
            val pickup_adress = add_pickup_adress.text.toString().trim()

            if (product_name.isEmpty()) {
                add_product_name.error = "Kérem adja meg a termék nevét!"
                add_product_name.requestFocus()
                return@setOnClickListener
            }
            if (description.isEmpty()) {
                add_description.error = "Kérem adja meg a termék leírását!"
                add_description.requestFocus()
                return@setOnClickListener
            }
            if (amount.isEmpty()) {
                add_amount.error = "Kérem adja meg a termék darabszámát!"
                add_amount.requestFocus()
                return@setOnClickListener
            }
            if (expiration.isEmpty()) {
                add_expiration.error = "Kérem adja meg a termék lejáratát!"
                add_expiration.requestFocus()
                return@setOnClickListener
            }
            if (pickup_adress.isEmpty()) {
                add_pickup_adress.error = "Kérem adja meg az átvételi helyszínt!"
                add_pickup_adress.requestFocus()
                return@setOnClickListener
            }

            // image compression with helper class
            Resizer.resizeImage(photoFile)

            val image_name_1 = photoFile.name

            val apiInterface = APIClient().getClient()?.create(APIService::class.java)

            apiInterface!!.createProduct(
                "Bearer " + token,
                product_name.toRequestBody("text/plain".toMediaType()),
                description.toRequestBody("text/plain".toMediaType()),
                amount.toRequestBody("text/plain".toMediaType()),
                expiration.toRequestBody("text/plain".toMediaType()),
                pickup_adress.toRequestBody("text/plain".toMediaType()),
                image_name_1.toRequestBody("text/plain".toMediaType()),
                MultipartBody.Part.createFormData(
                    "image_1",
                    photoFile.name,
                    photoFile.asRequestBody("image/*".toMediaType())
                )
            ).enqueue(
                object : Callback<Register> {
                    override fun onResponse(call: Call<Register>, response: Response<Register>) {
                        if (response.isSuccessful) {
                            val register: Register? = response.body()

                            Toast.makeText(
                                applicationContext,
                                register?.getMessage(),
                                Toast.LENGTH_LONG
                            ).show()

                            startActivity(Intent(applicationContext, HomeActivity::class.java))
                        } else {
                            val gson = Gson()
                            val message: Register = gson.fromJson(response.errorBody()!!.charStream(), Register::class.java)
                            Toast.makeText(applicationContext, message.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    override fun onFailure(
                        call: Call<Register>,
                        t: Throwable
                    ) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show();
                    }
                })
        }
    }

    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            Glide.with(this)
                .load(photoFile.absolutePath)
                .centerCrop()
                .into(taken_image_view);
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    override fun onBackPressed() {
        startActivity(Intent(applicationContext, HomeActivity::class.java))
    }
}