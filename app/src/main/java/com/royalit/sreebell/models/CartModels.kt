package com.royalit.sreebell.models

import com.erepairs.app.models.Areas_Response
import com.google.gson.annotations.SerializedName
import com.royalit.sreebell.roomdb.CartItems

data class CartListResponse(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val cartList: List<com.royalit.sreebell.roomdb.CartItems>?,
    @SerializedName("code") val code: Int

)

data class UpdateCartResponse(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("code") val code: Int
)

data class AddtoCartResponse(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val cartList: CartAddresponse?,
    @SerializedName("code") val code: Int
)

data class CartAddresponse(
    @SerializedName("customer_id") val customer_id: String,
    @SerializedName("product_id") val product_id: String,
    @SerializedName("cart_id") val cart_id: String,
    @SerializedName("quantity") val quantity: String,
)

data class DeleteCartResponse(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("code") val code: Int
)


data class DirectCartAddResponse(
    @SerializedName("Status"   ) var Status   : Boolean?  = null,
    @SerializedName("Message"  ) var Message  : String?   = null,
    @SerializedName("Response" ) var Response : Response? = Response(),
    @SerializedName("code"     ) var code     : Int?      = null
)

data class Response (

    @SerializedName("customer_id" ) var customerId : String? = null,
    @SerializedName("product_id"  ) var productId  : String? = null,
    @SerializedName("quantity"    ) var quantity   : String? = null,
    @SerializedName("kgs"         ) var kgs        : String? = null,
    @SerializedName("quintals"    ) var quintals   : String? = null,
    @SerializedName("id"          ) var id         : Int?    = null

)