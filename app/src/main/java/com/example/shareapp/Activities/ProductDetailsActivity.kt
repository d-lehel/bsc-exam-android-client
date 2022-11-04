package com.example.shareapp.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.shareapp.APIClient
import com.example.shareapp.APIService
import com.example.shareapp.Helpers.Token
import com.example.shareapp.R
import com.example.shareapp.ResponseModels.Message
import kotlinx.android.synthetic.main.activity_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        // get token from local storage
        val token: String? = Token.getAccessToken(this)

        val donator: String? = intent.getStringExtra("donator")
        val product: String? = intent.getStringExtra("product")
        val product_id: String? = intent.getStringExtra("id")
        val user_id: String? = intent.getStringExtra("user_id")
        val distance: String? = intent.getStringExtra("distance")
        val image: String? = intent.getStringExtra("image")
        val description: String? = intent.getStringExtra("description")
        val amount: String? = intent.getStringExtra("amount")
        val expiration: String? = intent.getStringExtra("expiration")
        val adress: String? = intent.getStringExtra("adress")

        sender_name_details.setText(donator)
        distance_details.setText(distance)
        subject_details.setText(product)
        description_details.setText(description)
        amount_details.setText(amount)
        expiration_details.setText(expiration)
        location_details.setText(adress)

        Glide.with(this)
            .load(image)
            .centerCrop()
            .error(R.drawable.no_image)
            .into(details_image);

        send_request.setOnClickListener{
            request_message.clearFocus()
            // elegansabban
            val message = request_message.text.toString().trim()

            // todo id?, product_id?, subject?
            val apiInterface = APIClient().getClient()?.create(APIService::class.java)
            apiInterface!!.sendRequest("Bearer $token",user_id.toString(),product_id.toString(),"Kérés",message).enqueue(
                object : Callback<Message> {
                    override fun onResponse(call: Call<Message>, response: Response<Message>) {

                        if(response.isSuccessful) {
                            if (response.code() == 200) {
                                Toast.makeText(applicationContext, "Üzenet elküldve!", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(applicationContext, "Error!", Toast.LENGTH_LONG).show()
                            }

                        } else { //todo hibakzeles specifikus
                            Toast.makeText(applicationContext, response.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                    override fun onFailure(call: Call<Message>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show();
                    }
                })
        }
    }
    override fun onBackPressed() {
        startActivity(Intent(applicationContext, HomeActivity::class.java))
    }
}