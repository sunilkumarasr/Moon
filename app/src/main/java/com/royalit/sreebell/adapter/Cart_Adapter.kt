package com.erepairs.app.adapter

import android.annotation.SuppressLint
import android.app.Activity


import android.content.Context
import android.content.DialogInterface
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.royalit.sreebell.R
import com.royalit.sreebell.databinding.CartListItemBinding
import com.royalit.sreebell.utils.Constants.BASE_URL_PHOTOS
import com.royalit.sreebell.utils.NetWorkConection
import kotlin.math.roundToInt

class Cart_Adapter(
    val context: Activity,
    var itemsData: ArrayList<com.royalit.sreebell.roomdb.CartItems>,
    var cartData: List<com.royalit.sreebell.roomdb.CartItems>?,
    var click: ProductItemClick?,
    var quantityChangeListener: CartItemQuantityChangeListener?
) : RecyclerView.Adapter<Cart_Adapter.ViewHolder>() {
    var serviceidlist: MutableList<String> = ArrayList()
    lateinit var usersubcategory_id_: String

    inner class ViewHolder(val binding: CartListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CartListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    var dist_id="";
    var cust_category=0;

    init {
       val  sharedPreferences =
           context.getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )

        cust_category=  sharedPreferences.getInt("customer_category", 0)!!
        dist_id=sharedPreferences.getString("district_id", "")!!
    }
    companion object;

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data: com.royalit.sreebell.roomdb.CartItems = itemsData.get(position)
        holder.binding.nameTVID.text = data.product_name
            //price
        val actualPrice = "\u20B9" +  data.sales_price
        val spannableActualPrice = SpannableString(actualPrice).apply {
            setSpan(StrikethroughSpan(), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        holder.binding.priceTVID.text = spannableActualPrice
        holder.binding.offerPrice.text =
            "\u20B9" + data.offer_price
        Glide.with(holder.binding.imageID.context)
            .load(BASE_URL_PHOTOS+data.getItemImage())
            .placeholder(R.drawable.logo)
            .into(holder.binding.imageID)

        holder.binding.removeTVID.setOnClickListener { v ->
            val builder1 =
                AlertDialog.Builder(context)
            builder1.setCancelable(false)
            builder1.setTitle(null)
            builder1.setMessage("Are you sure \nDo you want to delete this item from cart")
            builder1.setPositiveButton(
                "Yes"
            ) {
                    dialogInterface: DialogInterface?, i1: Int ->
                quantityChangeListener?.onDeleteCartItem(data)

            }
            builder1.setNegativeButton("No", null)
            builder1.create().show()
        }

    }

    interface ProductItemClick {
        fun onProductItemClick(itemsData: com.royalit.sreebell.roomdb.CartItems?)
        fun onAddToCartClicked(itemsData: com.royalit.sreebell.roomdb.CartItems?, cartQty: String?, isAdd:Int)
    }

    interface CartItemQuantityChangeListener {
        fun onQuantityChanged(cartItem: com.royalit.sreebell.roomdb.CartItems, newQuantity: Int)
        fun onDeleteCartItem(cartItem: com.royalit.sreebell.roomdb.CartItems)
        fun addToCartQuantity(product_id: String, kgs: String, quintals: String?, cart_id: String?)
    }


    // return the size of languageList
    override fun getItemCount(): Int {
        return itemsData.size
    }
}