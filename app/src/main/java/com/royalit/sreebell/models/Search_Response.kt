package com.erepairs.app.models

import com.google.gson.annotations.SerializedName
import com.royalit.sreebell.models.PriceList
import com.royalit.sreebell.models.ProductCart

data class Search_Response(

    @SerializedName("products_id") val products_id : Int,
    @SerializedName("categories_id") val categories_id : Int,
    @SerializedName("product_num") val product_num : String,
    @SerializedName("product_name") val product_name : String,
    @SerializedName("product_title") val product_title : String,
    @SerializedName("product_information") val product_information : String,
    @SerializedName("product_image") val product_image : String,
    @SerializedName("quantity") val quantity : String,
    @SerializedName("sales_price") val sales_price : String,
    @SerializedName("offer_price") val offer_price : String,
    @SerializedName("stock") val stock : String,
    @SerializedName("status") val status : String,
    @SerializedName("order_by") val order_by : String,
    @SerializedName("created_date") val created_date : String,
    @SerializedName("updated_date") val updated_date : String,
    @SerializedName("final_amount") val final_amount : String,
    @SerializedName("max_order_quantity") val max_order_quantity : String?,
    @SerializedName("category_2_price") val category_2_price : String?,
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