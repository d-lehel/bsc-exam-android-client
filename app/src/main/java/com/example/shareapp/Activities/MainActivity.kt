package com.example.shareapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.shareapp.Helpers.Token
import com.example.shareapp.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //no dark

        if(Token.getAccessToken(this)!=null && Token.isRememberMeChecked(this)) {
            startActivity(Intent(this, HomeActivity::class.java))
        }else{
            Token.setAccessToken(this, null, false)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_button_login.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
        main_button_register.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
    override fun onBackPressed() {
        // do nothing, if not go back to profile..
    }
}