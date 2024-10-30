package com.royalit.sreebell.adapter

import android.app.Activity
import android.graphics.Paint
import android.text.Editable
import android.text.InputFilter
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.royalit.sreebell.R
import com.royalit.sreebell.databinding.SubproductAdapterBinding
import com.royalit.sreebell.models.Product_Response
import com.royalit.sreebell.utils.NetWorkConection
import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.roundToInt


class SubProductList_Adapter(
    val context: Activity,
    var languageList: ArrayList<Product_Response>,
    var cartData: ArrayList<com.royalit.sreebell.roomdb.CartItems>?,
    var click: ProductItemClick?,
    var cust_category: Int,
    var dist_id: String

) : RecyclerView.Adapter<SubProductList_Adapter.ViewHolder>() {
    inner class ViewHolder(val binding: SubproductAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SubproductAdapterBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)


        return ViewHolder(binding)
    }


    companion object;

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(context)
            .load(languageList[position].product_image)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.rice)
            .transform(CenterCrop(), RoundedCorners(10))
            .into(holder.binding.subproductImage)

        holder.binding.subproductName.text = "" + languageList.get(position).product_name

        //offer price
        holder.binding.offerPrice.text = "\u20B9 " + languageList.get(position).offer_price
        //sale price
        val actualPrice = "\u20B9" + languageList.get(position).sales_price
        val spannableActualPrice = SpannableString(actualPrice).apply {
            setSpan(StrikethroughSpan(), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        holder.binding.salePrice.text = spannableActualPrice


        holder.binding.editBags.setTag(position.toString())
        holder.binding.editQuantity.setTag(position.toString())
        with(languageList.get(position))
        {
            Log.e("OnCart Items Size","OnCart Items  Size ${cart.size}")
            if(cart!=null&&cart.size>0&&cart.get(0).kgs!=null&&pack_size!=null)
            {
                //Log.e("OnCart Items","OnCart Items prices ${cart.get(0).kgs?.toInt()!!} -- ${pack_size?.toInt()!!}")
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
        with(holder) {
            with(languageList[position]) {
                // set name of the language from the list
//                binding.brndsTitleText.text = languageList.get(position).prodcut_name
//                binding.brandNameText.text = languageList.get(position).prodcut_desc

//                var data: Category_subResponse = languageList.get(position)
                //check user cart qty
               // binding.editBags.filters= arrayOf(DecimalDigitsInputFilter())

                binding.editQuantity.addTextChangedListener(object:TextWatcher{
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
                                if(languageList[pos].bag_contain_units==null||languageList[pos].pack_size==null|| languageList[pos].bag_contain_units?.isEmpty() == true)
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
                binding.editBags.addTextChangedListener(object:TextWatcher{
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
                                if(languageList[pos].pack_size==null||languageList[pos].pack_size==null|| languageList[pos].pack_size?.isEmpty() == true)
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


               /* for (j in cartData!!.indices) {
                    if (cartData!!.get(j).getProduct_id()
                            .toInt() == (languageList.get(position).products_id.toInt())
                    ) {
                        Log.e("Product CArt data", "" + languageList.get(position).products_id.toInt()+" : "+cartData!!.get(j).getProduct_id().toInt()+" : "+cartData!!.get(j).getCartQty())


                        var qnty="0"
                        if(cartData!![j].getCartQty()!=null)
                            qnty= cartData!![j].getCartQty()
                        holder.binding.qtyBtn.text = "" + qnty

                        holder.binding.addtocartBtn.text = "Update"
                        //Log.d("Added Cart Item","Added Cart Item inside adapter"+languageList[position].cart_id)
                        //languageList[position].cart_id=cartData!!.get(j).cartID.toString()

                        binding.editQuantity.setText( "" + qnty)
                    } else {
                        holder.binding.qtyBtn.text = "0"
                        holder.binding.addtocartBtn.text = "Add"
                        binding.editQuantity.setText("" )
                    }
                }*/

//
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


               /* binding.salePrice.paintFlags =
                    binding.salePrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG*/


                if (languageList.get(position).stock?.toInt() == 0) {
                    binding.outofstockBtn.visibility = View.VISIBLE
                    binding.editQuantity.isEnabled=false
                    binding.editBags.isEnabled=false
                    binding.addtocartBtn.visibility=View.GONE

                } else {
                    binding.outofstockBtn.visibility = View.GONE
                    binding.editQuantity.isEnabled=true
                    binding.editBags.isEnabled=true
                    /*if(binding.editQuantity.text.toString().trim().length>0&&binding.editBags.text.toString().trim().length>0)
                    {
                        binding.txtAddtocart.visibility=View.VISIBLE
                    }else
                    {
                        binding.txtAddtocart.visibility=View.GONE
                    }*/


                }
                binding.txtAddtocart.visibility=View.GONE
                Log.e("category_image", "" + languageList.get(position).product_image)
                holder.binding.subproductImage.setOnClickListener { v ->
                    click!!.onProductItemClick(
                        languageList.get(
                            position
                        )
                    )
                }
                holder.binding.subproductName.setOnClickListener { v ->
                    click!!.onProductItemClick(
                        languageList.get(
                            position
                        )
                    )
                }



                holder.binding.addtocartBtn.setOnClickListener {
                    try {


                        val carstQty: String = holder.binding.qtyBtn.text.toString()

                        if (carstQty == "0") {
                            Toast.makeText(context, "Select quantity..", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            if (NetWorkConection.isNEtworkConnected(context)) {
                                click?.onAddToCartClicked(languageList.get(position), carstQty,0)
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
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }
                }


                holder.binding.txtAddtocart.setOnClickListener {
                    holder.binding.editBags.setText("1")
                    holder.binding.editQuantity.setText("1")
                    val carstQty: String = holder.binding.editBags.text.toString()
                    var carstQuintalsQty: String = holder.binding.editQuantity.text.toString()

                    if (carstQty == "0"||carstQty.isEmpty()) {
                        Toast.makeText(context, "Select quantity..", Toast.LENGTH_LONG)
                            .show()
                    } else {

//                        if(carstQuintalsQty.toFloat()<(carstQty.toInt() * (languageList.get(position).pack_size!!.toInt())))
//                        {
//                            binding.editQuantity.setText("${((carstQty.toInt() * (languageList.get(position).pack_size!!.toFloat()))/100).toFloat()}")
//                             carstQuintalsQty = holder.binding.editQuantity.text.toString()
//
//                        }

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
        }

        holder.binding.txtAddtocart.visibility=View.VISIBLE

    }

    interface ProductItemClick {
        fun onProductItemClick(languageList: Product_Response?)
        fun onAddToCartClicked(languageList: Product_Response?, cartQty: String?,isAdd:Int)
        fun addToCartQuantity(product_id: String, kgs: String, quintals: String?, cart_id: String?)
        fun onItemVisibility(visibility:Boolean)
    }

    // return the size of languageList
    override fun getItemCount(): Int {
        return languageList.size
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


    fun resetData(newData: List<Product_Response>) {
        languageList.clear()
        languageList.addAll(newData)
        notifyDataSetChanged()
    }
    fun resetCartData(cartDatas:ArrayList<com.royalit.sreebell.roomdb.CartItems>) {
        cartData?.clear()
        cartData?.addAll(cartDatas)
        notifyDataSetChanged()
    }

    fun filterProducts(query: String) {
        val filteredList = ArrayList<Product_Response>()

        if (query.isEmpty()) {
            // If the search query is empty, show the original data
            filteredList.addAll(languageList)
        } else {
            // Customize this filter logic based on your criteria
            // Here, we are filtering by offer price
            val filterPattern = query.toLowerCase(Locale.ROOT).trim()
            filteredList.addAll(languageList.filter { item ->
                item.offer_price.contains(filterPattern, ignoreCase = true)
            })
        }

        // Update the adapter with the filtered products
        resetData(filteredList)
    }

    fun setProductsList(languageLists:ArrayList<Product_Response>)
    {
        try {
            languageList.clear()
            languageList.addAll(languageLists)

            Log.e("languageLists","languageLists ${languageLists.size}}")
            notifyDataSetChanged()
           // click.onItemVisibility(languageList.size>0)
        }catch (e:Exception)
        {

        }

    }
    fun setCartList(cartDatas:ArrayList<com.royalit.sreebell.roomdb.CartItems>)
    {
        try {
            cartData?.clear()
            cartData?.addAll(cartDatas)
            notifyDataSetChanged()
        }catch (e:Exception)
        {

        }
    }


    class DecimalDigitsInputFilter() :
        InputFilter {
        private val mPattern: Pattern

        init {
            mPattern =
                Pattern.compile("[0-9]{0," + (5) + "}+((\\.[0-9]{0," + (2) + "})?)||(\\.)?")
        }

        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dstart: Int,
            dend: Int
        ): CharSequence {
            val matcher: Matcher = mPattern.matcher(dest)
            return if (!matcher.matches()) "" else ""
        }
    }

    // ...
}


