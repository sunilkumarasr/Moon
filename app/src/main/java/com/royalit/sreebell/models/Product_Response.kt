package com.royalit.sreebell.models

import com.google.gson.annotations.SerializedName

data class Product_Response(

    @SerializedName("products_id") val products_id: Int,
    @SerializedName("product_num") val product_num: String,
    @SerializedName("categories_id") val categories_id: String,
    @SerializedName("district_id") val district_id: String,
    @SerializedName("product_name") val product_name: String,
    @SerializedName("product_title") val product_title: String,
    @SerializedName("product_information") val product_information: String,
    @SerializedName("product_image") val product_image: String,
    @SerializedName("stock") val stock: String,
    @SerializedName("quantity") val quantity: String,
    @SerializedName("sales_price") val sales_price: String,
    @SerializedName("offer_price") val offer_price: String,
    @SerializedName("final_amount") val final_amount: String,
    @SerializedName("max_order_quantity") val max_order_quantity: String?,
    @SerializedName("category_2_price") val category_2_price: String?,
    @SerializedName("pack_size") val pack_size: String?,
    @SerializedName("unit_of_measurement") val unit_of_measurement: String?,
    @SerializedName("bag_contain_units") val bag_contain_units: String?,
    @SerializedName("bags_for_quintals") val bags_for_quintals: String?,
    @SerializedName("master_packing_size") val master_packing_size: String?,
    @SerializedName("prices") val prices: List<PriceList>,
    @SerializedName("cart"                ) var cart               : ArrayList<ProductCart> = arrayListOf(),
    var current_quitals: String?="",
    var current_bags: String?="",
    )

data class ProductCart(
    @SerializedName("id"          ) var id         : String? = null,
    @SerializedName("customer_id" ) var customerId : String? = null,
    @SerializedName("product_id"  ) var productId  : String? = null,
    @SerializedName("quantity"    ) var quantity   : String? = null,
    @SerializedName("kgs"         ) var kgs        : String? = null,
    @SerializedName("quintals"    ) var quintals   : String? = null
)
