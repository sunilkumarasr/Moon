package com.erepairs.app.adapter

import android.annotation.SuppressLint
import android.app.Activity


import android.content.Context
import android.content.DialogInterface
import android.text.Editable
import android.text.TextWatcher
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

    // create an inner class with name ViewHolder
    //It takes a view argument, in which pass the generated class of single_item.xml
    // ie SingleItemBinding and in the RecyclerView.ViewHolder(binding.root) pass it like this
    inner class ViewHolder(val binding: CartListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    // inside the onCreateViewHolder inflate the view of SingleItemBinding
    // and return new ViewHolder object containing this layout
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

    // bind the items with each item of the list languageList which than will be
    // shown in recycler view
    // to keep it simple we are not setting any image data to view
    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {
            with(itemsData[position]) {
                // set name of the language from the list
//                binding.brndsTitleText.text = languageList.get(position).prodcut_name
//                binding.brandNameText.text = languageList.get(position).prodcut_desc

                val data: com.royalit.sreebell.roomdb.CartItems = itemsData.get(position)

                if(kgs!=null&&kgs.isNotEmpty())
                {
                    Log.e("OnCart Items","OnCart Items prices ${kgs?.toInt()!!} -- ${pack_size?.toInt()!!}")
                    if(current_bags==null||current_bags?.isEmpty()!!)
                        holder.binding.editBags.setText("${kgs?.toInt()!! / pack_size?.toInt()!!}")
                    else
                        holder.binding.editBags.setText("${current_bags}")
                    if(current_quitals==null||current_quitals?.isEmpty()!!)
                        holder.binding.editQuantity.setText("${quintals}")
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


                var qnty="1"
                var kgs=1
                if(data.kgs!=null&&data.kgs.isNotEmpty())
                kgs=  data.kgs.toInt()
                val pkgSize=data.pack_size.toInt()
                qnty=(kgs.div(pkgSize)).toString()

               /* if(data.getCartQty()!=null)
                    qnty= data.getCartQty()*/
                holder.binding.cartQty.text = "" + qnty
                binding.editBags.setText("${kgs?.toInt()!! / pack_size?.toInt()!!}")
                binding.editQuantity.setText("${data.quintals}")

                val cartQty = intArrayOf(holder.binding.cartQty.text.toString().toInt())
                Log.e("item_cart_qty==>", "" + cartQty[0])
                holder.binding.root.setOnClickListener { v ->
                    click!!.onProductItemClick(
                        itemsData.get(
                            position
                        )
                    )
                }
                holder.binding.nameTVID.text = data.product_name
                holder.binding.qtyTVID.text = data.cartQty.toString() + ""
//        holder.binding.priceTVID.setText("Total : \u20b9 " + data.getFinal_price());

                //        holder.binding.priceTVID.setText("Total : \u20b9 " + data.getFinal_price());
                try {
                    var final_amt=0
                        if (cust_category == 1) {
                            final_amt =( data.distributor_price.toInt()* data.quintals.toFloat()).toInt()
                            holder.binding.offerPrice.text= " Per Quintal : "+"₹ ${data.distributor_price}"
                        } else if (cust_category == 2) {
                            final_amt =( data.general_trade_price.toInt()* data.quintals.toFloat()).toInt()
                            holder.binding.offerPrice.text=""
                            holder.binding.offerPrice.text= " Per Quintal : "+"₹ ${data.general_trade_price}"
                        }

                    holder.binding.priceTVID.text = "Total : \u20b9 $final_amt"
                    Log.e("item_price", "" + final_amt)
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
                holder.binding.weightVID.text = "" + data.weight


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

                Log.e("data.getItemImage()","data.getItemImage() ${data.getItemImage()}")
                Glide.with(holder.binding.imageID.context)
                    .load(BASE_URL_PHOTOS+data.getItemImage())
                    .placeholder(R.drawable.logo)
                    .into(holder.binding.imageID)

               /* holder.binding.cartIncBtn.setOnClickListener { v ->


                    val carstQty = binding.cartQty.text.toString()
                    if ((data.max_order_quantity!=null)&& (data.max_order_quantity.toInt()<carstQty.toInt())){
                        Toast.makeText(context, "Can't add Max Quantity for this Product", Toast.LENGTH_LONG).show()

                        return@setOnClickListener
                    }
                    if (data.stock == carstQty) {

                        Toast.makeText(
                            context,
                            "Stock Limit only " + data.stock,
                            Toast.LENGTH_LONG
                        ).show()
                    }else {
                        Log.e("item_cart_==>", "" + carstQty[0])

                        cartQty[0]++
                         if ((data.max_order_quantity!=null)&& (data.max_order_quantity.toInt()<carstQty.toInt())){
                             Toast.makeText(context, "Can't add Max Quantity for this Product", Toast.LENGTH_LONG).show()

                             return@setOnClickListener
                        }
                        holder.binding.cartQty.text = cartQty[0].toString()

                        // Notify the quantity change to the listener
                        quantityChangeListener?.onQuantityChanged(data, cartQty[0])
                        holder.binding.cartQty.text = "" + cartQty.get(0)
                        holder.binding.addToCartBtn.performClick()
                    }


                }

                holder.binding.addToCartBtn.text = "Update"

                holder.binding.cartDecBtn.setOnClickListener { v ->
                    if (cartQty[0] > 1) {
                        cartQty[0]--
                        holder.binding.cartQty.text = "" + cartQty[0]
                        holder.binding.cartQty.text = cartQty[0].toString()

                        // Notify the quantity change to the listener
                       *//* if(cartQty[0]==1)
                        quantityChangeListener?.onQuantityChanged(data, 0)
                        else
                        quantityChangeListener?.onQuantityChanged(data, cartQty[0])
*//*
                        val carstQty = holder.binding.cartQty.text.toString()

                        if(cartQty[0]==1)
                        click!!.onAddToCartClicked(data, carstQty,1)
                        else
                        click!!.onAddToCartClicked(data, carstQty,1)
                       // holder.binding.addToCartBtn.performClick()
                    }

                }

                holder.binding.addToCartBtn.setOnClickListener { v ->
                    val carstQty = holder.binding.cartQty.text.toString()
                    if (carstQty == "0") {

                        Toast.makeText(context, "Select quantity..", Toast.LENGTH_LONG).show()

                    }else {
                        if(carstQty == "1")
                        click!!.onAddToCartClicked(data, carstQty,0)
                        else
                        click!!.onAddToCartClicked(data, carstQty,1)
                    }
                }*/

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
                                if(itemsData[pos].pack_size  ==null||itemsData[pos].bags_for_quintals==null|| itemsData[pos].pack_size?.isEmpty() == true)
                                {
                                    binding.editQuantity.setText("")
                                    binding.editBags.setText("")

                                    return
                                }
                                val quital = s.toString().toFloat()
                                val bag=(quital*(itemsData[pos].bags_for_quintals?.toFloat()!!)*(itemsData[pos].bag_contain_units?.toInt()!!)).roundToInt()
                                //val bag=(quital*(itemsData[pos].bags_for_quintals?.toFloat()!!)).roundToInt()
                                binding.editBags.setText(bag.toString())
                                itemsData[pos].current_quitals=s.toString()
                                itemsData[pos].current_bags=bag.toString()
                                // notifyDataSetChanged()
                            }else
                            {
                                binding.editBags.setText("")
                            }
                            if(binding.editQuantity.text.toString().trim().length>0&&binding.editBags.text.toString().trim().length>0)
                            {
                                binding.txtAddtocart.visibility= View.VISIBLE
                            }else
                            {
                                binding.txtAddtocart.visibility= View.GONE
                            }
                        }

                    }

                })


                if(binding.editBags.hasFocus()||binding.editQuantity.hasFocus())
                {
                   /* if(binding.editQuantity.text.toString().trim().length>0&&binding.editBags.text.toString().trim().length>0)
                    {
                        binding.txtAddtocart.visibility= View.VISIBLE
                    }else
                    {
                        binding.txtAddtocart.visibility= View.GONE
                    }*/
                }
                binding.txtAddtocart.visibility= View.GONE
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
                                if(itemsData[pos].pack_size==null||itemsData[pos].bags_for_quintals==null|| itemsData[pos].pack_size?.isEmpty() == true)
                                {
                                    binding.editQuantity.setText("")
                                    binding.editBags.setText("")
                                    return
                                }
                                val bags = s.toString().toInt()
                                val bag_in_quintals=
                                    itemsData[pos].pack_size?.toFloat()
                                        ?.times(bags)
                                Log.e("Quintal Added ","Quintal Added  $bag_in_quintals - $bags  - - ${itemsData[pos].pack_size}")
                                binding.editQuantity.setText((bag_in_quintals!!/100).toString())
                                itemsData[pos].current_bags=s.toString()
                                itemsData[pos].current_quitals=(bag_in_quintals!!/100).toString()
                                //notifyDataSetChanged()
                            }else
                            {
                                binding.editQuantity.setText("")
                            }

                            if(binding.editQuantity.text.toString().trim().length>0&&binding.editBags.text.toString().trim().length>0)
                            {
                                binding.txtAddtocart.visibility= View.VISIBLE
                            }else
                            {
                                binding.txtAddtocart.visibility= View.GONE
                            }
                        }
                    }

                })

                holder.binding.txtAddtocart.setTag(position)
                holder.binding.editQuantity.setTag(position)
                holder.binding.editBags.setTag(position)
                holder.binding.txtAddtocart.setOnClickListener {
                    val pos=it.getTag().toString().toInt()
                    val carstQty: String = holder.binding.editBags.text.toString()
                    var carstQuintalsQty: String = holder.binding.editQuantity.text.toString()

                    if (carstQty == "0"||carstQty.isEmpty()) {
                        Toast.makeText(context, "Select quantity..", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        if(carstQuintalsQty.toFloat()<(carstQty.toInt() * (itemsData.get(position).pack_size!!.toInt())))
                        {
                            binding.editQuantity.setText("${((carstQty.toInt() * (itemsData.get(position).pack_size!!.toFloat()))/100).toFloat()}")
                            carstQuintalsQty = holder.binding.editQuantity.text.toString()

                        }
                        if (NetWorkConection.isNEtworkConnected(context)) {
                            val product = itemsData.get(position)

                            quantityChangeListener?.addToCartQuantity(
                                product.products_id.toString(),
                                product.pack_size?.toInt()?.times(carstQty.toInt()).toString() ?: "",
                                carstQuintalsQty,
                                itemsData.get(position).cartID.toString()
                            )
                        }else
                        {
                            Toast.makeText(context,
                                "Please Check your internet",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        Log.e("carstQty", carstQty)
                    }
                }
            }
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