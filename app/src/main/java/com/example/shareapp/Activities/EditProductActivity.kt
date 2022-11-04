package com.example.shareapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.shareapp.APIClient
import com.example.shareapp.APIService
import com.example.shareapp.Helpers.Token
import com.example.shareapp.R
import com.example.shareapp.ResponseModels.Message
import com.example.shareapp.ResponseModels.Product
import com.example.shareapp.ResponseModels.Register
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_edit_product.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

        val token = Token.getAccessToken(this)

        // todo eliminate null check
        val id: String? = intent.getStringExtra("id")
        val name: String? = intent.getStringExtra("name")
        val description: String? = intent.getStringExtra("description")
        val amount: String? = intent.getStringExtra("amount")
        val expiration: String? = intent.getStringExtra("expiration")
        val adress: String? = intent.getStringExtra("adress")
        val image: String? = intent.getStringExtra("image")

        edit_product_name.setText(name)
        edit_product_description.setText(description)
        edit_product_amount.setText(amount)
        edit_product_expiration.setText(expiration)
        edit_product_adress.setText(adress)

        Glide.with(this)
            .load(image)
            .centerCrop()
            .error(R.drawable.no_image)
            .into(edit_product_image);

        edit_product_update.setOnClickListener {
            val apiInterface = APIClient().getClient()?.create(APIService::class.java)
            if (id != null) {
                apiInterface!!.updateProduct(
                    "Bearer " + token,
                    id.toInt(),
                    edit_product_name.text.toString(),
                    edit_product_description.text.toString(),
                    edit_product_amount.text.toString(),
                    edit_product_expiration.text.toString(),
                    edit_product_adress.text.toString(),
                ).enqueue(
                    object : Callback<Register> {
                        override fun onResponse(call: Call<Register>, response: Response<Register>) {
                            if (response.isSuccessful) {
                                val register: Register? = response.body()

                                Toast.makeText(
                                    applicationContext,
                                    "Termék sikeresen frissítve!",
                                    Toast.LENGTH_LONG
                                ).show()

                                startActivity(Intent(applicationContext, MyProductsActivity::class.java))
                            } else {
                                val gson = Gson()
                                val message: Register = gson.fromJson(
                                    response.errorBody()!!.charStream(),
                                    Register::class.java
                                )
                                Toast.makeText(
                                    applicationContext,
                                    message.getMessage(),
                                    Toast.LENGTH_LONG
                                ).show();
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

        // delete product api call
        edit_product_delete.setOnClickListener {
            val apiInterface = APIClient().getClient()?.create(APIService::class.java)
            if (id != null) {
                apiInterface!!.deleteProduct(
                    "Bearer " + Token.getAccessToken(this),
                    id.toInt()
                )?.enqueue(object : Callback<Message?> {

                    // if api call was succesfull
                    override fun onResponse(
                        call: Call<Message?>,
                        response: Response<Message?>
                    ) {
                        if (response.code() == 200) {
                            Toast.makeText(applicationContext, "Termék sikeresen törölve!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(applicationContext, MyProductsActivity::class.java))
                        } else {
                            Toast.makeText(applicationContext, "Törlés sikertelen!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    // if api call failed
                    override fun onFailure(call: Call<Message?>, t: Throwable) {
                        Toast.makeText(applicationContext, "fail: " + t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
    override fun onBackPressed() {
        startActivity(Intent(applicationContext, MyProductsActivity::class.java))
    }
}