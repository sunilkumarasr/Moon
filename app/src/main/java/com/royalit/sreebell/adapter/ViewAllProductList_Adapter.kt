package com.royalit.sreebell.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Paint
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
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.royalit.sreebell.R
import com.royalit.sreebell.databinding.HomeproductsAdapterBinding
import com.royalit.sreebell.models.Product_Response
import com.royalit.sreebell.roomdb.CartItems
import com.royalit.sreebell.utils.Constants
import com.royalit.sreebell.utils.NetWorkConection
import com.royalit.sreebell.utils.Utilities
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class ViewAllProductList_Adapter(
    val context: Activity,
    var languageList: ArrayList<Product_Response>,
    var cartData: ArrayList<com.royalit.sreebell.roomdb.CartItems>,
    var click: ProductItemClick?,
    var cust_category: Int,
    var dist_id: String
) : RecyclerView.Adapter<ViewAllProductList_Adapter.ViewHolder>() {

init {
    languageList= ArrayList()
}
    // create an inner class with name ViewHolder
    //It takes a view argument, in which pass the generated class of single_item.xml
    // ie SingleItemBinding and in the RecyclerView.ViewHolder(binding.root) pass it like this
    inner class ViewHolder(val binding: HomeproductsAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

    // inside the onCreateViewHolder inflate the view of SingleItemBinding
    // and return new ViewHolder object containing this layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomeproductsAdapterBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return languageList.size
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
            holder.binding.salePrice.visibility=View.VISIBLE
            holder.binding.offerPrice.visibility=View.VISIBLE
            holder.binding.lnrEditOptions.visibility=View.VISIBLE
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

                Glide.with(context).load(languageList.get(position).product_image)
                    .error(R.drawable.placeholder_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                    .into(holder.binding.productImage)

                binding.sProductText.text = "" + languageList.get(position).product_name

                binding.offerPrice.text = "\u20B9 " + offer_price
                //sale price
                val actualPrice = "\u20B9" + languageList.get(position).sales_price
                val spannableActualPrice = SpannableString(actualPrice).apply {
                    setSpan(StrikethroughSpan(), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                binding.salePrice.text = spannableActualPrice


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
            binding.txtAddtocart.visibility=View.VISIBLE

        }


    }


    interface ProductItemClick {
        fun onProductItemClick(itemsData: Product_Response?)
        fun onAddToCartClicked(itemsData: Product_Response, cartQty: String?, isAdd: Int)
        fun addToCartQuantity(product_id: String, kgs: String, quintals: String?, cart_id: String?)

    }

    fun sortByOfferPrice(ascending: Boolean) {
        if(languageList==null||languageList.size<=0)
        {
            return//for filter button
        }
        val district_id=languageList.get(0).district_id

        if (ascending) {
            if (district_id == dist_id) {
                if (cust_category == 1) {

                    languageList.sortBy {
                        (it.prices!=null&&it.prices.size>0)
                        it.prices.get(0).distributor_price?.toInt()
                    }


                } else if (cust_category == 2) {
                    languageList.sortBy {
                        (it.prices!=null&&it.prices.size>0)
                        it.prices.get(0).general_trade_price?.toInt()
                    }

                }

            }else
            {
                languageList.sortBy {
                    it.sales_price?.toInt()
                }
            }

        } else {
            if (district_id == dist_id) {
                if (cust_category == 1) {

                    languageList.sortByDescending {
                        (it.prices!=null&&it.prices.size>0)
                        it.prices.get(0).distributor_price?.toInt()
                    }


                } else if (cust_category == 2) {
                    languageList.sortByDescending {
                        (it.prices!=null&&it.prices.size>0)
                        it.prices.get(0).general_trade_price?.toInt()
                    }

                }

            }else
            {
                languageList.sortByDescending {
                    it.sales_price?.toInt()
                }
            }

        }
        notifyDataSetChanged()
    }

    // Filter the list by offer price
    fun filterItemsByPrice(query: String) {
        val filteredList = ArrayList<Product_Response>()

        if (query.isEmpty()) {
            filteredList.addAll(languageList)
        } else {
            val filterPattern = query.toLowerCase(Locale.ROOT).trim()
            filteredList.addAll(languageList.filter { item ->
                item.offer_price.toString().contains(filterPattern)
            })
        }

        languageList = filteredList
        notifyDataSetChanged()
    }

    fun resetData(newData: List<Product_Response>) {
        Log.d("API Response", "reset called")

        if(languageList==null)
            languageList=ArrayList()
        languageList.clear()
        languageList.addAll(newData)
        notifyDataSetChanged()
    }

    fun updateData(newData: List<Product_Response>) {
        languageList.clear()
        languageList.addAll(newData)
        notifyDataSetChanged()
    }

    fun addProductsLIst(
        languageLists: ArrayList<Product_Response>,
        cartDatas: ArrayList<com.royalit.sreebell.roomdb.CartItems>
    ) {
        Log.d("API Response cartData ", "Addproduct list 1")

        if (languageList != null) {
            languageList.clear()
            languageList.addAll(languageLists)

        }else
        {
            languageList= ArrayList()
            languageList.addAll(languageLists)
        }
        if (cartData != null) {
            cartData.clear()
            cartData.addAll(cartDatas)

        }else
        {
            cartData= ArrayList()
            cartData.addAll(cartDatas)
        }
        Log.d("API Response cartData ", "Addproduct list 2")
        notifyDataSetChanged()
    }

    fun addCartData(cartDatas: ArrayList<com.royalit.sreebell.roomdb.CartItems>) {

    }
}