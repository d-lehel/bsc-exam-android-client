package com.example.shareapp.ResponseModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Register {
    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("errors")
    @Expose
    private var errors: Errors? = null

    fun Register(message: String, errors: Errors) {
        this.message = message
        this.errors = errors
    }

    fun getMessage(): String? {
        return message
    }

    fun getErrors(): Errors? {
        return errors
    }
}