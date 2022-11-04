package com.example.shareapp.ResponseModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

class Product {
    @SerializedName("user_name")
    @Expose
    private var donator: String? = null

    // todo, not getting from api
    @SerializedName("rating")
    @Expose
    private var rating = 0

    @SerializedName("product_name")
    @Expose
    private var product: String? = null

    @SerializedName("description")
    @Expose
    private var description: String? = null

    @SerializedName("pickup_adress")
    @Expose
    private var pickup_adress: String? = null

    @SerializedName("expiration")
    @Expose
    private var expiration: String? = null


    @SerializedName("distance")
    @Expose
    private var distance = 0.00f

    @SerializedName("image_1")
    @Expose
    private var image: String? = null

    @SerializedName("user_id")
    @Expose
    private var user_id: Int? = null

    @SerializedName("id")
    @Expose
    private var id: Int? = null

    @SerializedName("amount")
    @Expose
    private var amount: Int? = null


    fun getDonator(): String? {
        return donator
    }

    fun getUser_id(): Int? {
        return user_id
    }

    fun getImage(): String? {
        return image
    }

    fun getRating(): Int {
        return rating
    }

    fun getAmount(): Int? {
        return amount
    }

    fun getId(): Int? {
        return id
    }

    fun getProduct(): String? {
        return product
    }

    fun getDescription(): String? {
        return description
    }

    fun getAdress(): String? {
        return pickup_adress
    }

    fun getExpiration(): String? {
        return expiration
    }

    fun getDistance(): Float {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        return df.format(distance).toFloat()
    }

    fun Product(
        donator: String?,
        expiration: String?,
        pickup_adress: String?,
        rating: Int,
        amount: Int?,
        id: Int?,
        product: String?,
        distance: Float,
        description: String?
    ) {
        this.donator = donator
        this.rating = rating
        this.product = product
        this.amount = amount
        this.id = id
        this.description = description
        this.pickup_adress = pickup_adress
        this.expiration = expiration
        this.distance = distance
    }
}