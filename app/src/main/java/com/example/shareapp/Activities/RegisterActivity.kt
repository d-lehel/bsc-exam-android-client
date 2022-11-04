package com.example.shareapp.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.shareapp.APIClient
import com.example.shareapp.APIService
import com.example.shareapp.R
import com.example.shareapp.ResponseModels.Register
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //no dark
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button.setOnClickListener {
            val name = register_input_name.text.toString().trim()
            val email = register_input_email.text.toString().trim()
            val password = register_input_password.text.toString().trim()
            val re_password = register_input_re_password.text.toString().trim()

            if (name.isEmpty()) {
                register_input_name.error = "Kérem adja meg egy felhasználó nevet!"
                register_input_name.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                register_input_email.error = "Kérem adja meg egy email címet!"
                register_input_email.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                register_input_password.error = "Kérem adjon meg egy jelszót!"
                register_input_password.requestFocus()
                return@setOnClickListener
            }
            if (re_password.isEmpty()) {
                register_input_re_password.error = "Kérem erősítse meg a jelszót!"
                register_input_re_password.requestFocus()
                return@setOnClickListener
            }

            val apiInterface = APIClient().getClient()?.create(APIService::class.java)
            apiInterface!!.createUser(name, email, password, re_password).enqueue(
                object : Callback<Register> {
                    override fun onResponse(call: Call<Register>, response: Response<Register>) {
                        if(response.isSuccessful) {
                            val register: Register? = response.body()
                            Toast.makeText(applicationContext, register?.getMessage(), Toast.LENGTH_LONG).show()
                            startActivity(Intent(applicationContext, LoginActivity::class.java)) // atiranyirok a loginra
                        }
                        else
                        { //todo hibakzeles specifikus
                            val gson = Gson()
                            val message: Register = gson.fromJson(response.errorBody()!!.charStream(), Register::class.java)

                            val name_errors = message.getErrors()?.getName().toString()
                            val email_errors = message.getErrors()?.getEmail().toString()
                            val password_errors = message.getErrors()?.getPassword().toString()

                            if(name_errors != "null"){
                                register_input_name.error = name_errors
                                register_input_name.requestFocus()
                            }else if(email_errors != "null"){
                                register_input_email.error = email_errors
                                register_input_email.requestFocus()
                            }else if(password_errors != "null"){
                                register_input_password.error = password_errors
                                register_input_password.requestFocus()
                            }

                        }
                    }

                    override fun onFailure(call: Call<Register>, t: Throwable) { // sikertelen api hivas
                       Toast.makeText(applicationContext, "Szerver hiba, kérem lépjen kapcsolatba a fejlesztővel!", Toast.LENGTH_LONG).show();
                    }
                })
        }
    }
}