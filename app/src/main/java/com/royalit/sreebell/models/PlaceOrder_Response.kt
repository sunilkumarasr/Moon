package com.royalit.sreebell.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PlaceOrder_Response(


    @SerializedName("order_id") val order_id: Int,
    @SerializedName("amount") val amount: Int,
    @SerializedName("order_details") val order_details: List<PlaceOrderDetails>
) : Serializable