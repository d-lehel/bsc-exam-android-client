package com.example.shareapp.Helpers

import android.content.Context
import android.content.SharedPreferences

class Token {
    // static functions
    companion object {
        fun setAccessToken(context: Context, token: String?, remember_me: Boolean) {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("ACCESSTOKEN", token)
            editor.putBoolean("remember_me", remember_me)
            editor.apply()
        }
        fun getAccessToken(context: Context): String? {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
            return sharedPreferences.getString("ACCESSTOKEN", null)
        }
        fun isRememberMeChecked(context: Context): Boolean {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean("remember_me",false)
        }
    }
}