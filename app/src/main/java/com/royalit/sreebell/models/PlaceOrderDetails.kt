package com.royalit.sreebell.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PlaceOrderDetails(
    @SerializedName("order_id") val order_id: String,
    @SerializedName("order_from") val order_from: String,
    @SerializedName("order_number") val order_number: String,
    @SerializedName("customer_id") val customer_id: String,
    @SerializedName("full_name") val full_name: String,
    @SerializedName("bill_name") val bill_name: String,
    @SerializedName("email") val email: String,
    @SerializedName("mobile") val mobile: String,
    @SerializedName("product_details") val product_details: List<PlaceOrderProductDetails>
): Serializable
