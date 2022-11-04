package com.example.shareapp.ResponseModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FeedbackMessage {
    @SerializedName("message")
    @Expose
    private var message: String? = null

    fun FeedbackMessage(message: String) {
        this.message = message
    }
    fun getMessage(): String? {
        return message
    }
}