package com.royalit.sreebell.models

import com.erepairs.app.models.OrderHisProductDe_Response
import com.google.gson.annotations.SerializedName

data class OrderHis_Response(


    @SerializedName("order_id") val order_id : String,
    @SerializedName("order_from") val order_from : String,
    @SerializedName("order_number") val order_number : String,
    @SerializedName("agent_id") val agent_id : String,
    @SerializedName("customer_id") val customer_id : String,
    @SerializedName("full_name") val full_name : String,
    @SerializedName("email") val email : String,
    @SerializedName("mobile") val mobile : String,
    @SerializedName("product_details") val product_details : List<OrderHisProductDe_Response>,
    @SerializedName("billing_address") val billing_address : String,
    @SerializedName("area_name") val area_name : String,
    @SerializedName("grand_total") val grand_total : String,
    @SerializedName("order_notes") val order_notes : String,
    @SerializedName("delivery_status") val delivery_status : String,
    @SerializedName("completed_date") val completed_date : String,
    @SerializedName("created_date") val created_date : String,
    @SerializedName("order_status") val order_status : String,
    @SerializedName("updated_date") val updated_date : String
)