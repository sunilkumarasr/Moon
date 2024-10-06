package com.royalit.sreebell.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.royalit.sreebell.R
import com.royalit.sreebell.databinding.CategoriesAdapterBinding
import com.royalit.sreebell.databinding.RowItemManualPaymentBinding
import com.royalit.sreebell.models.Category_ListResponse
import com.royalit.sreebell.models.Category_Response
import com.royalit.sreebell.models.Category_subResponse
import com.royalit.sreebell.models.ManualOrderPaymentResponse
import retrofit2.Callback
import java.lang.Exception

class ManualPaymentList_Adapter(
    val context: Context,
    private var languageList: List<ManualOrderPaymentResponse>,


    ) : RecyclerView.Adapter<ManualPaymentList_Adapter.ViewHolder>() {
   /* var dataList = emptyList<ManualOrderPaymentResponse>()
    internal fun setDataList(dataList: List<ManualOrderPaymentResponse>) {
        this.dataList = dataList
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvCustomerId: TextView
        var tvAmount: TextView
        var tvPaymentDate: TextView
        var tvOrderId: TextView
        var tvUTRNumber: TextView

        init {
            tvCustomerId = itemView.findViewById(R.id.tvCustomerId)
            tvOrderId = itemView.findViewById(R.id.tvOrderId)
            tvPaymentDate = itemView.findViewById(R.id.tvPaymentDate)
            tvAmount = itemView.findViewById(R.id.tvAmount)
            tvUTRNumber = itemView.findViewById(R.id.tvUTRNumber)

        }

    }

    //https://pasindulaksara.medium.com/recyclerview-with-grid-layout-in-kotlin-414d780c47ae
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManualPaymentList_Adapter.ViewHolder {

        // Inflate the custom layout
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item_manual_payment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ManualPaymentList_Adapter.ViewHolder, position: Int) {

        var data = dataList[position]
        holder.tvCustomerId.text = data.customer_id
        holder.tvAmount.text = data.amount
        holder.tvPaymentDate.text = data.payment_date
        holder.tvOrderId.text = data.order_id
        holder.tvUTRNumber.text = data.utr_number

    }
    override fun getItemCount() = dataList.size*/

    interface CategoryClickListener {
        fun onCategoryClick(categoryId: Int)
    }

    inner class ViewHolder(val binding: RowItemManualPaymentBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowItemManualPaymentBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

   // companion object;


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {
            with(languageList[position]) {

                binding.tvCustomerName.text = "" + languageList.get(position).customer_name
                binding.tvAmount.text = "" + languageList.get(position).amount
                binding.tvPaymentDate.text = "" + languageList.get(position).deposit_date
                binding.tvInvoiceDate.text = "" + languageList.get(position).invoice_date
                binding.tvInvoiceNumber.text = "" + languageList.get(position).invoice_number
                binding.tvUTRNumber.text = "" + languageList.get(position).utr_number



                holder.itemView.setOnClickListener {


                }

            }
        }
    }


    // return the size of languageList
    override fun getItemCount(): Int {
        return languageList.size
    }
}






