package com.example.shareapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shareapp.APIClient
import com.example.shareapp.APIService
import com.example.shareapp.Helpers.Token
import com.example.shareapp.R
import com.example.shareapp.ResponseModels.FeedbackMessage
import com.example.shareapp.ResponseModels.Profile
import kotlinx.android.synthetic.main.activity_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val token = Token.getAccessToken(this)

        val apiInterface = APIClient().getClient()?.create(APIService::class.java)

        apiInterface!!.viewProfile("Bearer " + token).enqueue(
            object : Callback<Profile> {
                override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                    if (response.isSuccessful) {
                        profile_name.setText(response.body()?.getName())
                        profile_email.setText(response.body()?.getEmail())
                    } else {
                        Toast.makeText(applicationContext, "Szerver hiba!", Toast.LENGTH_LONG).show();
                    }
                }

                override fun onFailure(call: Call<Profile>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show();
                }
            })

            set_location.setOnClickListener {
                // todo eltavolit
                val token2: String? = token
                val intent = Intent(applicationContext, SetLocationActivity::class.java);
                intent.putExtra("Token",token2)
                startActivity(intent);
            }

            show_myProducts.setOnClickListener{
                startActivity(Intent(applicationContext, MyProductsActivity::class.java))
            }


            logout_button.setOnClickListener {
                val apiInterface = APIClient().getClient()?.create(APIService::class.java)

                apiInterface!!.userLogout("Bearer " + token).enqueue(object : Callback<FeedbackMessage> {
                    override fun onResponse(call: Call<FeedbackMessage>, response: Response<FeedbackMessage>) {
                        if(response.isSuccessful) {

                            if (response.code() == 200) {
                                Token.setAccessToken(applicationContext,null,false)
                                Toast.makeText(applicationContext, "Sikeresen kijelentkezés!", Toast.LENGTH_LONG).show()
                                startActivity(Intent(applicationContext, MainActivity::class.java))
                            } else {
                                Toast.makeText(applicationContext, "Error!", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(applicationContext, response.toString(), Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<FeedbackMessage>, t: Throwable) { // sikertelen api hivas
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show();
                    }
                })
            }
    }
    override fun onBackPressed() {
        startActivity(Intent(applicationContext, HomeActivity::class.java))
    }
}