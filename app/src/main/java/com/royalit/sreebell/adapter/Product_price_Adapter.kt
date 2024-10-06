package com.royalit.sreebell.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.royalit.sreebell.R
import com.royalit.sreebell.databinding.ProductPriceItemBinding
import com.royalit.sreebell.models.Product_priceresponse
//import com.royalit.sreebell.ui.Products_price_search
import java.util.ArrayList

//class Product_price_Adapter(
//    private val context: Context,
//    private var PriceList: ArrayList<Product_priceresponse>
//) : RecyclerView.Adapter<Product_price_Adapter.ViewHolder>() {
//
//    inner class ViewHolder(val binding: ProductPriceItemBinding) : RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val binding = ProductPriceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ViewHolder(binding)
//    }
//
//    override fun getItemCount(): Int {
//        return PriceList.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val product = PriceList[position]
//
//        // Bind product data to the views
//        // ...
//
//        holder.binding.productImage.setOnClickListener {
//            val sharedPreferences = context.getSharedPreferences("loginprefs", Context.MODE_PRIVATE)
//            val navController = Navigation.findNavController(context as Activity, R.id.nav_host_fragment_content_home_screen)
//            navController.navigate(R.id.nav_product_details)
//
//            val editor = sharedPreferences.edit()
//            editor.putString("subcatid", product.products_id.toString())
//            editor.apply()
//        }
//
//        holder.binding.productText.setOnClickListener {
//            val sharedPreferences = context.getSharedPreferences("loginprefs", Context.MODE_PRIVATE)
//            val navController = Navigation.findNavController(context as Activity, R.id.nav_host_fragment_content_home_screen)
//            navController.navigate(R.id.nav_product_details)
//
//            val editor = sharedPreferences.edit()
//            editor.putString("subcatid", product.products_id.toString())
//            editor.apply()
//        }
//    }
//
//    fun updateData(newProducts: List<Product_priceresponse>) {
//        PriceList.clear()
//        PriceList.addAll(newProducts)
//        notifyDataSetChanged()
//    }
//}