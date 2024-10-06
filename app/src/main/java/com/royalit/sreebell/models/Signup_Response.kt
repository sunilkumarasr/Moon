package com.royalit.sreebell.models

import com.google.gson.annotations.SerializedName

data class Signup_Response(


    @SerializedName("full_name") val full_name : String,
    @SerializedName("mobile_number") val mobile_number : String,
    @SerializedName("email_id") val email_id : String,
    @SerializedName("password") val password : String,
    @SerializedName("login_create_from") val login_create_from : String,
    @SerializedName(" area_id") val area_id : Int,
    @SerializedName("address") val address : String,
    @SerializedName("created_date") val created_date : String,
    @SerializedName("user_id") val user_id : Int
)