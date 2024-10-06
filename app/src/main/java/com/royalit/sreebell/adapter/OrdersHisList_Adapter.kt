package com.royalit.sreebell.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.royalit.sreebell.databinding.OrderhisAdapterBinding
import com.royalit.sreebell.models.OrderHis_Response


class OrdersHisList_Adapter(
    val context: Context,
    private var languageList: List<OrderHis_Response>

) : RecyclerView.Adapter<OrdersHisList_Adapter.ViewHolder>() {


    // create an inner class with name ViewHolder
    //It takes a view argument, in which pass the generated class of single_item.xml
    // ie SingleItemBinding and in the RecyclerView.ViewHolder(binding.root) pass it like this
    inner class ViewHolder(val binding: OrderhisAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

    // inside the onCreateViewHolder inflate the view of SingleItemBinding
    // and return new ViewHolder object containing this layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OrderhisAdapterBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    companion object;

    // bind the items with each item of the list languageList which than will be
    // shown in recycler view
    // to keep it simple we are not setting any image data to view
    var mScrollTouchListener: OnItemTouchListener? = object : OnItemTouchListener {
        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            val action = e.action
            when (action) {
                MotionEvent.ACTION_MOVE -> rv.parent.requestDisallowInterceptTouchEvent(true)
            }
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {
            with(languageList[position]) {
                // set name of the language from the list
//                binding.brndsTitleText.text = languageList.get(position).prodcut_name
//                binding.brandNameText.text = languageList.get(position).prodcut_desc


                val pd=languageList.get(position).product_details as ArrayList
                Log.e("Order details list","Order details list ${pd.size}}")
                val ad = OrdersFood(context as Activity, pd)
                holder.binding.dataRCID.adapter = ad
              //  mScrollTouchListener?.let { holder.binding.dataRCID.addOnItemTouchListener(it) };

                val orer = languageList.get(position).product_details.map {

//                    Glide.with(context).load(it.image)
//                        .error(R.drawable.ic_launcher_background)
//                        .transform(CenterCrop(), RoundedCorners(10))
//
//                        .into(holder.binding.orderhisImage)
//                    binding.orderhisTitle.text = "" + it.product_name
//                    binding.orderhisTotalamt.text = "\u20A8 :" + it.price
//                    binding.orderhisWait.text = "Weight :" + it.pqty
//                    binding.x.text = "\u20A8 " + it.pqty

                }

                binding.orderNumberTVID.text = "" + languageList.get(position).order_number
                binding.orderAmountTVID.text = "₹ " + languageList.get(position).grand_total
                binding.orderDate.text = "" + languageList.get(position).created_date

                if(languageList.get(position).delivery_status.equals("5")){
                    binding.cancelBtn.text = "" + languageList.get(position).order_status
                    binding.cancelBtn.setTextColor(Color.RED)

                }else if(languageList.get(position).delivery_status.equals("2")){
                    binding.cancelBtn.text = "" + languageList.get(position).order_status
                    binding.cancelBtn.setTextColor(Color.MAGENTA)

                }else if(languageList.get(position).delivery_status.equals("1")){
                    binding.cancelBtn.text = "" + languageList.get(position).order_status
                    binding.cancelBtn.setTextColor(Color.BLUE)

                }else{
                    binding.cancelBtn.text = "" + languageList.get(position).order_status
                    binding.cancelBtn.setTextColor(Color.parseColor("#01a652"))
                }

            }
        }
    }


    // return the size of languageList
    override fun getItemCount(): Int {
        return languageList.size
    }
}