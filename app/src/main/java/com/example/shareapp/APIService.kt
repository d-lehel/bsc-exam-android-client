package com.example.shareapp

import com.example.shareapp.ResponseModels.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface APIService {

    // ### REGISTRATIOM ###
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("register")
    fun createUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("password_confirmation") password_confirmation: String
    ): Call<Register>


    // ##### LOGIN #####
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("login")
    fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("remember_me") remember_me: Boolean,
        @Field("grant_type") grant_type: String,
        @Field("client_id") client_id: Int,
        @Field("client_secret") client_secret: String
    ): Call<Login>

    // ##### LOGOUT #####
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("logout")
    fun userLogout(@Header("Authorization") auth: String?): Call<FeedbackMessage>

    // ##### VIEW PROFILE #####
    @Headers("Accept: application/json;charset=UTF-8")
    @GET("users/profile")
    fun viewProfile(@Header("Authorization") auth: String?): Call<Profile>

    // ##### GET ALL PRODUCT #####
    @FormUrlEncoded
    @Headers("Accept: application/json;charset=UTF-8")
    @POST("products/filtered")
    fun allProduct(
        @Header("Authorization") auth: String?,
        @Field("search_keyword") search_keyword: String, // if there search
        @Field("filter_1") filter_1: String,     // own / others / all
        @Field("filter_2") filter_2: String,      // upload_time / distance
        @Field("filter_3") filter_3: String,      // ASC / DESC
        @Field("max_distance") max_distance: Int,    // max distance
    ): Call<List<Product>>


    // ##### ADD PRODUCT #####
    @Multipart
    @POST("products")
    fun createProduct(
        @Header("Authorization") auth: String,
        @Part("product_name") product_name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("amount") amount: RequestBody,
        @Part("expiration") expiration: RequestBody,
        @Part("pickup_adress") pickup_adress: RequestBody,
        @Part("image_name_1") image_name_1: RequestBody,
        @Part imageFile: MultipartBody.Part
    ): Call<Register>

    // ##### UPDATE PRODUCT #####
    @FormUrlEncoded
    @Headers("Accept: application/json; charset=UTF-8")
    @PUT("products/{id}")
    fun updateProduct(
        @Header("Authorization") auth: String,
        @Path("id") id: Int,
        @Field("name") name: String,
        @Field("description") description: String,
        @Field("amount") amount: String,
        @Field("expiration") expiration: String,
        @Field("pickup_adress") pickup_adress: String,
    ): Call<Register>

    // ##### DELETE PRODUCT #####
    @DELETE("products/{id}")
    fun deleteProduct(
        @Header("Authorization") auth: String?,
        @Path("id") id: Int
    ): Call<Message?>?

    // ##### SEND REQUEST #####
    @FormUrlEncoded
    @Headers("Accept: application/json; charset=UTF-8")
    @POST("messages/request")
    fun sendRequest(
        @Header("Authorization") auth: String?,
        @Field("recipient_id") recipient_id: String, // for who
        @Field("product_id") product_id: String,
        @Field("subject") subject: String,
        @Field("message") message: String
    ): Call<Message>

    // ##### SEND RESPONSE #####
    @FormUrlEncoded
    @Headers("Accept: application/json; charset=UTF-8")
    @POST("messages/response")
    fun sendResponse(
        @Header("Authorization") auth: String?,
        @Field("recipient_id") recipient_id: String, // for who
        @Field("product_id") product_id: String,
        @Field("subject") subject: String,
        @Field("message") message: String,
        @Field("is_accepted") is_accepted: String
    ): Call<Message>


    // ##### GET ALL INBOX MESSAGES #####
    @Headers("Accept: application/json; charset=UTF-8")
    @GET("messages/inbox")
    fun allInboxMessages(@Header("Authorization") auth: String?): Call<List<Message>>

    // ##### SET COORDINATES #####
    @FormUrlEncoded
    @Headers("Accept: application/json;charset=UTF-8")
    @POST("users/coordinate")
    fun setCoordinates(
        @Header("Authorization") auth: String,
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double,
    ): Call<Message>
}