package com.example.shareapp.ResponseModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Profile {
    @SerializedName("name")
    @Expose
    private var name: String? = null
    @SerializedName("email")
    @Expose
    private var email: String? = null

    fun Profile(name: String, email: String) {
        this.name = name
        this.email = email
    }
    fun getName(): String? {
        return name
    }
    fun getEmail(): String? {
        return email
    }
}