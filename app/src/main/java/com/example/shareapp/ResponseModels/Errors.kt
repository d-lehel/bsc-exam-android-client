package com.example.shareapp.ResponseModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Errors {
    @SerializedName("password")
    @Expose
    private var password: List<String?>? = null

    @SerializedName("email")
    @Expose
    private var email: List<String?>? = null

    @SerializedName("name")
    @Expose
    private var name: List<String?>? = null

    fun Errors(password: List<String?>,name: List<String?>,email: List<String?>) {
        this.password = password
        this.name = name
        this.email = email
    }

    fun getPassword(): List<String?>? {
        return password
    }
    fun getName(): List<String?>? {
        return name
    }
    fun getEmail(): List<String?>? {
        return email
    }
}