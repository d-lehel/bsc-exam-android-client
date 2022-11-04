package com.example.shareapp.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.shareapp.APIClient
import com.example.shareapp.APIService
import com.example.shareapp.Helpers.Token
import com.example.shareapp.R
import com.example.shareapp.ResponseModels.Login
import com.example.shareapp.ResponseModels.Register
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //no dark
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        zalan_login.setOnClickListener {
            login_input_email.setText("kiss_zalan@test.com")
        }
        hanna_login.setOnClickListener {
            login_input_email.setText("nagy_hanna@test.com")
        }
        csilla_login.setOnClickListener {
            login_input_email.setText("hajdu_csilla@test.com")
        }
        bence_login.setOnClickListener {
            login_input_email.setText("molnar_bence@test.com")
        }
        zsofi_login.setOnClickListener {
            login_input_email.setText("gal_zsofi@test.com")
        }

        login_button.setOnClickListener {
            val email = login_input_email.text.toString().trim()
            val password = login_input_password.text.toString().trim()
            val remember_me = login_checkBox.isChecked;
            val grant_type = "password"
            val client_id = 1;
            val client_secret = "mANCtmWts1G2P5PF6g3TeotcfF1VRDjJIxIG0Oa6";


            if (email.isEmpty()) {
                login_input_email.error = "Kérem adja meg az email címét!"
                login_input_email.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                login_input_password.error = "Kérem adja meg a jelszavát!"
                login_input_password.requestFocus()
                return@setOnClickListener
            }

            val apiInterface = APIClient().getClient()?.create(APIService::class.java)
            apiInterface!!.userLogin(
                email,
                password,
                true,
                grant_type,
                client_id,
                client_secret
            ).enqueue(
                object : Callback<Login> {
                    override fun onResponse(call: Call<Login>, response: Response<Login>) {
                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                Token.setAccessToken(applicationContext, response.body()?.getAccess_token(), remember_me)
                                startActivity(Intent(applicationContext, HomeActivity::class.java))
                                Toast.makeText(applicationContext, "Sikeres bejelentkezés!", Toast.LENGTH_LONG).show()
                            } else {
                                //
                                Toast.makeText(applicationContext, "Hibás bejelentkezési adatok!", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            val gson = Gson()
                            val message: Register = gson.fromJson(
                                response.errorBody()!!.charStream(),
                                Register::class.java
                            )
                            Toast.makeText(applicationContext, message.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    override fun onFailure(call: Call<Login>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show();
                    }
                })
        }
    }
}