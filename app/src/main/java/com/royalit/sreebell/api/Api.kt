package com.erepairs.app.api

import com.erepairs.app.models.AppMaintananceResponseModel
import com.erepairs.app.models.Areas_ListResponse
import com.erepairs.app.models.Common_Response
import com.erepairs.app.models.Districts_ListResponse
import com.royalit.sreebell.models.*
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface Api {


    @FormUrlEncoded
    @POST("get_areas_list_list")
    fun getAreasList(
        @Field("api_key") api_key: String
    ): Call<Areas_ListResponse>


    @FormUrlEncoded
    @POST("districts")
    fun getDistrictsList(
        @Field("api_key") api_key: String
    ): Call<Districts_ListResponse>


    @FormUrlEncoded
    @POST("user_resistration")
    fun user_registation(
        @Field("api_key") api_key: String,
        @Field("full_name") full_name: String,
        @Field("mobile_number") mobile_number: String,
        @Field("email_id") email_id: String,
        @Field("address") address: String,
        @Field("state") state: String,
        @Field("city") city: String,
        @Field("pswrd") pswrd: String,
    ): Call<SignupList_Response>

    @FormUrlEncoded
    @POST("user_login")
    fun postlogin(
        @Field("api_key") api_key: String,
        @Field("username") username: String,
        @Field("pswrd") pswrd: String,
        @Field("device_token") device_token: String
    ): Call<LoginList_Response>

    @FormUrlEncoded
    @POST("user_get_profile")
    fun getprofile(
        @Field("api_key") api_key: String,
        @Field("customer_id") customer_id: String
    ): Call<LoginList_Response>
    @FormUrlEncoded
    @POST("manual_order_payment_list")
    fun manualOrderPaymentList(
        @Field("api_key") api_key: String,
        @Field("customer_id") customer_id: String
    ): Call<ManualOrderPaymentList>

    //categories list

    @FormUrlEncoded
    @POST("categories_list")
    fun getcategoruesList(
        @Field("api_key") api_key: String
    ): Call<Category_ListResponse>

    @FormUrlEncoded
    @POST("category_wise_products_list")
    fun getsubcategoruesList(
        @Field("api_key") api_key: String,
        @Field("categories_id") categories_id: String
    ): Call<Category_subListResponse>

    //product list

    @FormUrlEncoded
    @POST("all_products_list")
    fun getproductsList(
        @Field("api_key") api_key: String,
        @Field("customer_id") customer_id: String
    ): Call<Product_ListResponse>

    @FormUrlEncoded
    @POST("manual_order_payment")
    fun manualOrderPayment(
        @Field("api_key") api_key: String,
        @Field("customer_name") customer_name: String,
        @Field("invoice_number") invoice_number: String,
        @Field("invoice_date") invoice_date: String,
        @Field("amount") amount: String,
        @Field("customer_id") customer_id: String,
        @Field("deposit_date") deposit_date: String,
        @Field("utr_number") utr_number: String,
    ): Call<PaymentDetailsSubmit_Response>
    //orders list

    @FormUrlEncoded
    @POST("get_orders_list")
    fun getordershisList(
        @Field("api_key") api_key: String,
        @Field("customer_id") customer_id: String
    ): Call<OrderHis_ListResponse>

    @FormUrlEncoded
    @POST("place_order_save")
    fun getplaceordersave(
        @Field("api_key") api_key: String,
        @Field("customer_id") customer_id: String,
        @Field("product_ids") product_ids: String,
        @Field("product_qtys") product_qtys: String,
        @Field("order_notes") order_notes: String,
        @Field("full_name") full_name: String,
        @Field("lat") lat: String,
        @Field("lng") lng: String,
        @Field("full_address") full_address: String,
        @Field("amount") orderamount: String,
        @Field("customer_category") customer_category: String,
        @Field("district_id") district_id: String,
        @Field("product_bags") productQtyStr: String,
        @Field("product_quintals") product_quintals: String,

    ): Call<Placeorder_ListResponse>

    @FormUrlEncoded
    @POST("product_wise_indetails_info")
    fun getindetails(
        @Field("api_key") api_key: String,
        @Field("products_id") products_id: String,
        @Field("district_id") district_id: String,
        @Field("customer_id") customer_id: String
    ): Call<Product_inDetailsListResponse>


    @FormUrlEncoded
    @POST("search_products_list")
    fun getsearch(
        @Field("api_key") api_key: String,
        @Field("search_word") search_word: String,
        @Field("customer_id") customer_id: String
    ): Call<Search_ListResponse>

    @FormUrlEncoded
    @POST("user_forget_password")
    fun userforgot_passwrd(
        @Field("api_key") api_key: String,
        @Field("email_id") email_id: String
    ): Call<Common_Response>

    @FormUrlEncoded
    @POST("api/sliders")
    fun getSliders(
        @Field("api_key") apiKey: String,
        @Field("value") value: String,
        @Field("type")type :String
    ): Call<Sliderlist_Response>

    @FormUrlEncoded
    @POST("api/filter_products_by_price")
     fun getProductsByOfferPrice(
        @Field("api_key") apiKey: String,
        @Field("price") value: String,
    ): Call<Product_ListResponse>


    @FormUrlEncoded
    @POST("search_products_list")
    fun getsearchProducts(
        @Field("api_key") api_key: String,
        @Field("search_word") search_word: String
    ): Call<Product_ListResponse>


    @FormUrlEncoded
    @POST("api/notifications_list")
    fun getNotificationList(
        @Field("api_key") api_key: String,
    ): Call<NotificationListResponse>

    @FormUrlEncoded
    @POST("api/add_cart")
    fun addToCart(
        @Field("api_key") api_key: String,
        @Field("customer_id") customer_id: String,
        @Field("product_id") product_id: String,
        @Field("quantity") quantity: String,
    ): Call<AddtoCartResponse>

    @FormUrlEncoded
    @POST("api/direct_cart")
    fun directCart(
        @Field("api_key") api_key: String,
        @Field("customer_id") customer_id: String,
        @Field("product_id") product_id: String,
        @Field("quantity") quantity: String,
        @Field("kgs") kgs: String,
        @Field("quintals") quintals: String,
        @Field("cart_id") cart_id: String,
    ): Call<DirectCartAddResponse>

    @FormUrlEncoded
    @POST("api/remove_cart")
    fun removeFromCart(
        @Field("api_key") api_key: String,
        @Field("customer_id") customer_id: String,
        @Field("product_id") product_id: String,
        @Field("cart_id") cart_id: String,
    ): Call<DeleteCartResponse>

    @FormUrlEncoded
    @POST("api/update_cart")
    fun updateCart(
        @Field("api_key") api_key: String,
        @Field("customer_id") customer_id: String,
        @Field("quantity") quantity: String,
        @Field("product_id") product_id: String,
    ): Call<UpdateCartResponse>

    @FormUrlEncoded
    @POST("api/cart_list")
    fun getCartList(
        @Field("api_key") api_key: String,
        @Field("customer_id") customer_id: String
    ): Call<CartListResponse>
    @FormUrlEncoded
    @POST("api/settings")
    fun checkAppMaintanance(
        @Field("api_key") api_key: String,
    ): Call<AppMaintananceResponseModel>

    @FormUrlEncoded
    @POST("api/user_update_profile")
    fun updateProfile(
        @Field("api_key") api_key: String,
        @Field("customer_id") customer_id: String,
        @Field("full_name") full_name: String,
        @Field("email_id") email_id: String,
        @Field("mobile_number") mobile_number: String,
        @Field("state") state: String,
        @Field("city") city: String,
        @Field("address") address: String
    ): Call<SignupList_Response>


}
