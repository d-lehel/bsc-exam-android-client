package com.example.shareapp.ResponseModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Message {
    @SerializedName("sender_id")
    @Expose
    private var sender_id: Int? = null

    @SerializedName("sender_name")
    @Expose
    private var sender_name: String? = null

    @SerializedName("recipient_id")
    @Expose
    private var recipient_id: Int? = null

    @SerializedName("product_id")
    @Expose
    private var product_id: Int? = null

    @SerializedName("is_accepted")
    @Expose
    private var is_accepted: Int? = null

    @SerializedName("subject")
    @Expose
    private var subject: String? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null

    fun Message(sender_name: String, subject: String) {
        this.sender_name = sender_name
        this.subject = subject
    }

    fun getSender_name(): String? {
        return sender_name
    }

    fun getSenderId(): Int? {
        return sender_id
    }

    fun getSubject(): String? {
        return subject
    }

    fun getRecipient_id(): Int? {
        return recipient_id
    }

    fun getProduct_id(): Int? {
        return product_id
    }

    fun isAccepted(): Int? {
        return is_accepted
    }

    fun getMessage(): String? {
        return message;
    }
}