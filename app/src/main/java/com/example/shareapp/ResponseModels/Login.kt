package com.example.shareapp.ResponseModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Login {
    @SerializedName("access_token")
    @Expose
    private var access_token: String? = null

    @SerializedName("expires_at")
    @Expose
    private var expires_at: String? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("errors")
    @Expose
    private var errors: Errors? = null

    fun Login(message: String, errors: Errors) {
        this.message = message
        this.errors = errors
        this.access_token = access_token
        this.expires_at = expires_at
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }

    fun getErrors(): Errors? {
        return errors
    }

    fun setErrors(Errors: Errors?) {
        this.errors = errors
    }

    fun getAccess_token(): String? {
        return access_token
    }

    fun setAccess_token(access_token: String?) {
        this.access_token = access_token
    }

    fun getExpires_at(): String? {
        return expires_at
    }

    fun setExpires_at(Expires_at: String?) {
        this.expires_at = expires_at
    }
}