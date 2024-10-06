package com.royalit.sreebell.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.erepairs.app.models.OrderHisProductDe_Response
import com.royalit.sreebell.R
import com.royalit.sreebell.databinding.MyOrderListItemBinding
import java.lang.Exception
import java.util.*

class OrdersFood(
    activity: Activity,
    itemsData: List<OrderHisProductDe_Response>
) :
    RecyclerView.Adapter<OrdersFood.ProductsVH?>() {
    var itemsData: List<OrderHisProductDe_Response> = ArrayList<OrderHisProductDe_Response>()
    var activity: Activity

    // inside the onCreateViewHolder inflate the view of SingleItemBinding
    // and return new ViewHolder object containing this layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersFood.ProductsVH {
        val binding = MyOrderListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return OrdersFood.ProductsVH(binding)
    }


    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ProductsVH, position: Int) {
        val data: OrderHisProductDe_Response = itemsData[position]
        holder.binding.nameTVID.text = data.product_name
        holder.binding.quantityTVID.text = "Bags : " + data.product_bags+"\n"+"Quintals : ${data.product_quintals}"
        holder.binding.priceTVID.text = "Per Quintal : "+"\u20b9 " + data.price
        try {
            holder.binding.txtPreiceTotal.text =
                "Total : " + "\u20b9 " + (data.price.toFloat() * data.product_quintals.toFloat()).toInt()
        }catch (e:Exception)
        {

        }
//        holder.binding.createdDateTVID.setText(data.)
//        holder.binding.deliveryStatusTVID.setText(data)
        //holder.binding.priceTVID.visibility=View.GONE
        Log.e("binding.imageI","binding.imageI ${data.image}")
        Glide.with(activity).load(data.image).error(R.drawable.logo)
            .placeholder(R.drawable.logo)
            .into(holder.binding.imageID)
        if (position == itemsData.size - 1) {
            holder.binding.viewID.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return itemsData.size
    }

    class ProductsVH(binding: MyOrderListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: MyOrderListItemBinding = binding

    }


    init {
        this.itemsData = itemsData
        this.activity = activity
    }
}