package com.royalit.sreebell.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.Target
import com.royalit.sreebell.R
import com.royalit.sreebell.databinding.HomeproductsAdapterBinding
import com.royalit.sreebell.databinding.HomeproductsItemsListBinding
import com.royalit.sreebell.models.Product_Response
import com.royalit.sreebell.roomdb.CartItems
import com.royalit.sreebell.utils.Constants
import com.royalit.sreebell.utils.NetWorkConection
import com.royalit.sreebell.utils.Utilities
import java.util.*
import kotlin.math.roundToInt


class ProductList_Adapter(val context: Activity,
                          var languageList: ArrayList<Product_Response>,
                          var cartData: List<CartItems>?,
                          var click: ProductItemClick,
                          var cust_category: Int,
                          var dist_id: String) : RecyclerView.Adapter<ProductList_Adapter.ViewHolder>() {

    inner class ViewHolder(val binding: HomeproductsItemsListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomeproductsItemsListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    companion object;

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       // if(languageList.get(position).district_id == district_id)

//        Glide.with(context).load(languageList.get(position).product_image)
//            .error(R.drawable.placeholder_image)
//            .diskCacheStrategy(DiskCacheStrategy.ALL) // Don't cache the image
//            .priority(Priority.HIGH)
//            .into(holder.binding.productImage)
//
//        holder.binding.sProductText.text = "" + languageList.get(position).product_name
//
//        //price
//        val actualPrice = "\u20B9" + languageList.get(position).sales_price
//        val spannableActualPrice = SpannableString(actualPrice).apply {
//            setSpan(StrikethroughSpan(), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        }
//        holder.binding.ActulePrice.text = spannableActualPrice
//        holder.binding.OfferPrice.text ="\u20B9" + languageList.get(position).offer_price
//
//        holder.binding.txtAddtocart.setOnClickListener {
//            click?.addToCartQuantity(
//                languageList.get(position).products_id.toString(),
//                 "1",
//                "1",
//                languageList.get(position).cart.get(0).id
//            )
//        }

        with(holder) {
            holder.binding.editBags.setTag(position.toString())
            holder.binding.editQuantity.setTag(position.toString())
            holder.binding.txtAddtocart.setTag(position.toString())
            holder.binding.lnrEditOptions.visibility=View.VISIBLE

            with(languageList.get(position))
            {
                Log.e("OnCart Items Size","OnCart Items  Size ${cart.size}")
                if(cart.size>0)
                {
                    Log.e("OnCart Items","OnCart Items prices ${cart.get(0).kgs?.toInt()!!} -- ${pack_size?.toInt()!!}")
                    if(current_bags==null||current_bags?.isEmpty()!!)
                        holder.binding.editBags.setText("${cart.get(0).kgs?.toInt()!! / pack_size?.toInt()!!}")
                    else
                        holder.binding.editBags.setText("${current_bags}")
                    if(current_quitals==null||current_quitals?.isEmpty()!!)
                        holder.binding.editQuantity.setText("${cart.get(0).quintals}")
                    else holder.binding.editQuantity.setText("${current_quitals}")
                }
                else{
                    if(current_bags==null)
                        current_bags=""
                    if(current_quitals==null)
                        current_quitals=""
                    holder.binding.editBags.setText("${current_bags}")
                    holder.binding.editQuantity.setText("${current_quitals}")
                }
            }

            with(languageList[position]) {

                Glide.with(context).load(languageList.get(position).product_image)
                    .error(R.drawable.placeholder_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Don't cache the image
                    .priority(Priority.HIGH)
                    .into(holder.binding.productImage)

                binding.sProductText.text = "" + languageList.get(position).product_name

                //price
                val actualPrice = "\u20B9" + languageList.get(position).sales_price
                val spannableActualPrice = SpannableString(actualPrice).apply {
                    setSpan(StrikethroughSpan(), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                binding.ActulePrice.text = spannableActualPrice
                binding.OfferPrice.text ="\u20B9" + languageList.get(position).offer_price

                var finalDisplayPrice: String = ""
                if(languageList.get(position).prices!=null)
                    languageList.get(position).prices.forEach { price ->
                        if (price.district_id == dist_id) {
                            if (cust_category == 1) {
                                finalDisplayPrice = price.distributor_price
                            } else if (cust_category == 2) {
                                finalDisplayPrice = price.general_trade_price
                            }
                            return@forEach
                        }
                    }


                Log.e("category_image", "" + languageList.get(position).product_image)

                if (languageList.get(position).stock?.toInt() == 0) {
                    binding.outofstockBtn.visibility = View.VISIBLE

                } else {
                    binding.outofstockBtn.visibility = View.GONE

                }
                holder.binding.productImage.setOnClickListener {
                    if (languageList.get(position).stock?.toInt() == 0) {

                        Toast.makeText(
                            context,
                            "Out Of Stack",
                            Toast.LENGTH_LONG
                        ).show()
                        return@setOnClickListener
                    }
                    val sharedPreferences =
                        context.getSharedPreferences(
                            "loginprefs",
                            Context.MODE_PRIVATE
                        )
                    val navController = Navigation.findNavController(
                        context as Activity,
                        R.id.nav_host_fragment_content_home_screen
                    )
                    var finalDisplayPrice: String = ""
                    languageList.get(position).prices.forEach { price ->
                        if (price.district_id == dist_id) {
                            if (cust_category == 1) {
                                finalDisplayPrice = price.distributor_price
                            } else if (cust_category==2) {
                                finalDisplayPrice = price.general_trade_price
                            }
                            return@forEach
                        }
                    }
                    Constants.OFFER_PRICE = finalDisplayPrice
                    navController.navigate(R.id.nav_product_details)


                    val editor = sharedPreferences.edit()
                    editor.putString("subcatid", languageList.get(position).products_id.toString())
                    editor.commit()
                }
                holder.binding.sProductText.setOnClickListener {
                    if (languageList.get(position).stock.toInt() == 0) {
                        Toast.makeText(
                            context,
                            "Out Of Stack",
                            Toast.LENGTH_LONG
                        ).show()
                        return@setOnClickListener
                    }
                    val sharedPreferences =
                        context.getSharedPreferences(
                            "loginprefs",
                            Context.MODE_PRIVATE
                        )
                    val navController = Navigation.findNavController(
                        context as Activity,
                        R.id.nav_host_fragment_content_home_screen
                    )
                    var finalDisplayPrice: String = ""
                    languageList.get(position).prices.forEach { price ->
                        if (price.district_id == dist_id) {
                            if (cust_category == 1) {
                                finalDisplayPrice = price.distributor_price
                            } else if (cust_category==2) {
                                finalDisplayPrice = price.general_trade_price
                            }
                            return@forEach
                        }
                    }
                    Constants.OFFER_PRICE = finalDisplayPrice
                    navController.navigate(R.id.nav_product_details)


                    val editor = sharedPreferences.edit()
                    editor.putString("subcatid", languageList.get(position).products_id.toString())
                    editor.commit()
                }
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
                    holder.binding.editBags.setText("1")
                    holder.binding.editQuantity.setText("1")
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
                                     "1",
                                     "1",
                                    cart.get(0).id
                                )
                            }else
                            {
                                click?.addToCartQuantity(
                                    product.products_id.toString(),
                                    "1",
                                    "1",
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
            binding.txtAddtocart.visibility=View.VISIBLE

        }


    }

    interface ProductItemClick {
        fun onProductItemClick(languageList: Product_Response?)
        fun onAddToCartClicked(languageList: Product_Response?, cartQty: String?,isAdd:Int)
        fun addToCartQuantity(product_id: String, kgs: String, quintals: String?, cart_id: String?)
    }

    // return the size of languageList
    override fun getItemCount(): Int {
//        if (languageList.size > 10) {
//            return 10
//        }
//        return languageList.size
        return 4
    }


}