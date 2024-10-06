package com.royalit.sreebell.models

import com.google.gson.annotations.SerializedName

data class ManualOrderPaymentResponse(
    @SerializedName("id") val id : Int,
    @SerializedName("customer_name") val customer_name : String,
    @SerializedName("invoice_number") val invoice_number : String,
    @SerializedName("invoice_date") val invoice_date : String,
    @SerializedName("amount") val amount : String,
    @SerializedName("deposit_date") val deposit_date : String,
    @SerializedName("utr_number") val utr_number : String,
)
