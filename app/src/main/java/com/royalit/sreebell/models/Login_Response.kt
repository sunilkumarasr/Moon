package com.royalit.sreebell.models

import com.google.gson.annotations.SerializedName

data class Login_Response(

    @SerializedName("customer_id") val customer_id: Int,
    @SerializedName("customer_category") val customer_category: Int,
    @SerializedName("full_name") val full_name: String,
    @SerializedName("mobile_number") val mobile_number: String,
    @SerializedName("address") val address: String,
    @SerializedName("email_id") val email_id: String,
    @SerializedName("shop_name") val shop_name: String,
    @SerializedName("city") val city: String,
    @SerializedName("district_id") val district_id: String
)