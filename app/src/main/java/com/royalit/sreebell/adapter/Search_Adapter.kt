package com.royalit.sreebell.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.erepairs.app.models.Search_Response
import com.royalit.sreebell.R
import com.royalit.sreebell.databinding.SearchproductsAdapterBinding
import com.royalit.sreebell.roomdb.CartItems
import com.royalit.sreebell.utils.NetWorkConection
import com.royalit.sreebell.utils.Utilities
import kotlin.math.roundToInt

class Search_Adapter(
    val context: Activity,
    var languageList: ArrayList<Search_Response>,
    var cartData: List<com.royalit.sreebell.roomdb.CartItems>?,
    var click: SearchItemClick,
    var cust_category: Int,
    var dist_id: String

) : RecyclerView.Adapter<Search_Adapter.ViewHolder>() {


    // create an inner class with name ViewHolder
    //It takes a view argument, in which pass the generated class of single_item.xml
    // ie SingleItemBinding and in the RecyclerView.ViewHolder(binding.root) pass it like this
    inner class ViewHolder(val binding: SearchproductsAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

    // inside the onCreateViewHolder inflate the view of SingleItemBinding
    // and return new ViewHolder object containing this layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SearchproductsAdapterBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }


    companion object;

    // bind the items with each item of the list languageList which than will be
    // shown in recycler view
    // to keep it simple we are not setting any image data to view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {
            holder.binding.editBags.setTag(position.toString())
            holder.binding.editQuantity.setTag(position.toString())
            holder.binding.txtAddtocart.setTag(position.toString())
            with(languageList.get(position))
            {
                Log.e("OnCart Items Size","OnCart Items  Size ${cart.size}")
                if(cart!=null&&cart.size>0)
                {
                    Log.e("OnCart Items","OnCart Items prices ${cart.get(0).kgs?.toInt()!!} -- ${pack_size?.toInt()!!}")
                    if(current_bags==null||current_bags?.isEmpty()!!)
                        holder.binding.editBags.setText("${cart.get(0).kgs?.toInt()!! / pack_size?.toInt()!!}")
                    else
                        holder.binding.editBags.setText("${current_bags}")
                    if(current_quitals==null||current_quitals?.isEmpty()!!)
                        holder.binding.editQuantity.setText("${cart.get(0).quintals}")
                    else holder.binding.editQuantity.setText("${current_quitals}")
                }else
                {
                    if(current_bags==null)
                        current_bags=""
                    if(current_quitals==null)
                        current_quitals=""
                    holder.binding.editBags.setText("${current_bags}")
                    holder.binding.editQuantity.setText("${current_quitals}")
                }
            }
            with(languageList[position]) {
                // set name of the language from the list
//                binding.brndsTitleText.text = languageList.get(position).prodcut_name
//                binding.brandNameText.text = languageList.get(position).prodcut_desc



                Glide.with(context).load(languageList.get(position).product_image)
                    .error(R.drawable.ic_launcher_background)
                    .transform(CenterCrop(), RoundedCorners(10))
                    .into(holder.binding.sProductImage)

                binding.sProductText.text = "" + languageList.get(position).product_name

                var finalDisplayPrice: String = ""
                languageList.get(position).prices.forEach { price ->

                    Log.d("selectedserviceslist ","selectedserviceslist with price   -- ${cust_category} dist ${dist_id} -- ${price.distributor_price} --- ${price.general_trade_price} ")


                    if (price.district_id == dist_id) {
                        if (cust_category == 1) {
                            finalDisplayPrice = price.distributor_price
                            return@forEach
                        } else if (cust_category == 2) {
                            finalDisplayPrice = price.general_trade_price
                            return@forEach
                        }

                    }
                }
                if(finalDisplayPrice.isEmpty()||finalDisplayPrice=="0")
                {
                    binding.offerPrice.text =
                        "No purchase option"
                    binding.minusimgeBtn.visibility=View.INVISIBLE
                    binding.plusImage.visibility=View.INVISIBLE
                    binding.quntyText.visibility=View.INVISIBLE
                }
                else
                {
                    binding.quntyText.visibility=View.VISIBLE
                    binding.minusimgeBtn.visibility=View.VISIBLE
                    binding.quntyText.visibility=View.VISIBLE
                    binding.offerPrice.text =
                        " Per Quintal : "+ "\u20B9 " + finalDisplayPrice+""
                }
                binding.salePrice.text = "" + languageList.get(position).sales_price


               /* binding.salePrice.paintFlags =
                    binding.salePrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG*/




                Log.e("category_image", "" + languageList.get(position).sales_price)
                if (languageList.get(position).stock.toInt() == 0) {
                    binding.outofstockBtn.visibility = View.VISIBLE
                    binding.addLayout.visibility = View.GONE
                } else {
                    binding.outofstockBtn.visibility = View.GONE
                    binding.addLayout.visibility = View.VISIBLE
                }

                holder.binding.sProductImage.setOnClickListener { v ->
                    click.SearchItemClick(
                        languageList.get(
                            position))
                }
                holder.binding.sProductText.setOnClickListener { v ->
                    click.SearchItemClick(
                        languageList.get(
                            position))
                }

                val cartQty = intArrayOf(holder.binding.quntyText.text.toString().toInt())
                Log.e("item_cart_qty==>", "" + cartQty[0])

                binding.salePrice.paintFlags =
                    binding.salePrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                binding.editQuantity.addTextChangedListener(object: TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        if(binding.editQuantity.text.toString().trim().length>0&&binding.editBags.text.toString().trim().length>0)
                        {
                            binding.txtAddtocart.visibility=View.VISIBLE
                        }else
                        {
                            binding.txtAddtocart.visibility=View.GONE
                        }
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {

                    }

                    override fun afterTextChanged(s: Editable?) {
                        val pos=binding.editQuantity.getTag().toString().toInt()
                        if(binding.editQuantity.hasFocus())
                        {

                            if(s.toString().length>0) {
                                if(languageList[pos].pack_size==null||languageList[pos].bags_for_quintals==null|| languageList[pos].pack_size?.isEmpty() == true)
                                {
                                    binding.editQuantity.setText("")
                                    binding.editBags.setText("")

                                    return
                                }
                                val quital = s.toString().toFloat()
                                val bag=(quital*(languageList.get(pos).bags_for_quintals?.toFloat()!!)*(languageList.get(pos).bag_contain_units?.toInt()!!)).roundToInt()
                                binding.editBags.setText(bag.toString())
                                languageList.get(pos).current_quitals=s.toString()
                                languageList.get(pos).current_bags=bag.toString()
                                // notifyDataSetChanged()
                            }else
                            {
                                binding.editBags.setText("")
                            }
                            if(binding.editQuantity.text.toString().trim().length>0&&binding.editBags.text.toString().trim().length>0)
                            {
                                binding.txtAddtocart.visibility=View.VISIBLE
                            }else
                            {
                                binding.txtAddtocart.visibility=View.GONE
                            }
                        }

                    }

                })


                if(binding.editBags.hasFocus()||binding.editQuantity.hasFocus())
                {
                    if(binding.editQuantity.text.toString().trim().length>0&&binding.editBags.text.toString().trim().length>0)
                    {
                        binding.txtAddtocart.visibility=View.VISIBLE
                    }else
                    {
                        binding.txtAddtocart.visibility=View.GONE
                    }
                }
                binding.editBags.addTextChangedListener(object: TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        if(binding.editQuantity.text.toString().trim().length>0&&binding.editBags.text.toString().trim().length>0)
                        {
                            binding.txtAddtocart.visibility=View.VISIBLE
                        }else
                        {
                            binding.txtAddtocart.visibility=View.GONE
                        }
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {

                    }

                    override fun afterTextChanged(s: Editable?) {
                        if(binding.editBags.hasFocus())
                        {

                            if(s.toString().length>0) {
                                val pos=binding.editBags.getTag().toString().toInt()
                                if(languageList[pos].pack_size==null||languageList[pos].bags_for_quintals==null|| languageList[pos].pack_size?.isEmpty() == true)
                                {
                                    binding.editQuantity.setText("")
                                    binding.editBags.setText("")
                                    return
                                }
                                val bags = s.toString().toInt()
                                val bag_in_quintals=
                                    languageList[pos].pack_size?.toFloat()
                                        ?.times(bags)
                                Log.e("Quintal Added ","Quintal Added  $bag_in_quintals - $bags  - - ${languageList[pos].pack_size}")
                                binding.editQuantity.setText((bag_in_quintals!!/100).toString())
                                languageList.get(pos).current_bags=s.toString()
                                languageList.get(pos).current_quitals=(bag_in_quintals!!/100).toString()
                                //notifyDataSetChanged()
                            }else
                            {
                                binding.editQuantity.setText("")
                            }

                            if(binding.editQuantity.text.toString().trim().length>0&&binding.editBags.text.toString().trim().length>0)
                            {
                                binding.txtAddtocart.visibility=View.VISIBLE
                            }else
                            {
                                binding.txtAddtocart.visibility=View.GONE
                            }
                        }
                    }

                })

                holder.binding.txtAddtocart.setOnClickListener {
                    val carstQty: String = holder.binding.editBags.text.toString()
                    var carstQuintalsQty: String = holder.binding.editQuantity.text.toString()

                    if (carstQty == "0"||carstQty.isEmpty()) {
                        Toast.makeText(context, "Select quantity..", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        if(carstQuintalsQty.toFloat()<(carstQty.toInt() * (languageList.get(position).pack_size!!.toInt())))
                        {
                            binding.editQuantity.setText("${((carstQty.toInt() * (languageList.get(position).pack_size!!.toFloat()))/100).toFloat()}")
                            carstQuintalsQty = holder.binding.editQuantity.text.toString()

                        }
                        if (NetWorkConection.isNEtworkConnected(context)) {
                            val product = languageList.get(position)
                            if(cart!=null&&cart.size>0) {


                                click?.addToCartQuantity(
                                    product.products_id.toString(),
                                    product.pack_size?.toInt()?.times(carstQty.toInt()).toString() ?: "",
                                    carstQuintalsQty,
                                    cart.get(0).id
                                )
                            }else
                            {
                                click?.addToCartQuantity(
                                    product.products_id.toString(),
                                    product.pack_size?.toInt()?.times(carstQty.toInt()).toString() ?: "",
                                    carstQuintalsQty,
                                    "0"
                                )
                            }
                        }else
                        {
                            Toast.makeText(context,
                                "Please Check your internet",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        Log.e("data", "" + languageList.get(position))
                        Log.e("carstQty", carstQty)
                    }
                }

            }
            binding.txtAddtocart.visibility=View.GONE
        }
    }

    interface SearchItemClick {
        fun SearchItemClick(languageList: Search_Response?)
        fun onAddToCartClicked(languageList: Search_Response, cartQty: String?,isAss:Int)
        fun addToCartQuantity(product_id: String, kgs: String, quintals: String?, cart_id: String?)

    }


    // return the size of languageList
    override fun getItemCount(): Int {

        return languageList.size
        //return languageList.size
    }
}