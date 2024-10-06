package com.royalit.sreebell.models

import com.erepairs.app.models.OrderHisProductDe_Response
import com.google.gson.annotations.SerializedName

data class PaymentDetailsSubmitResponse(


    @SerializedName("customer_id") val customer_id : String,
    @SerializedName("order_id") val order_id : String,
    @SerializedName("amount") val amount : String,
    @SerializedName("utr_number") val utr_number : String,
    @SerializedName("payment_date") val payment_date : String,

)