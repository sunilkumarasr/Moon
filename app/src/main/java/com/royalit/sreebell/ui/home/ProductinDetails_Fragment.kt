package com.royalit.sreebell.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.erepairs.app.api.Api
import com.erepairs.app.models.AppMaintananceResponseModel
import com.google.gson.Gson
import com.royalit.sreebell.AppMaintananceActivity
import com.royalit.sreebell.HomeScreen
import com.royalit.sreebell.R
import com.royalit.sreebell.api.APIClient
import com.royalit.sreebell.databinding.ProductdetailsScreenBinding
import com.royalit.sreebell.models.AddtoCartResponse
import com.royalit.sreebell.models.CartListResponse
import com.royalit.sreebell.models.Category_subResponse
import com.royalit.sreebell.models.DirectCartAddResponse
import com.royalit.sreebell.models.Product_inDetailsListResponse
import com.royalit.sreebell.models.Product_inDetailsResponse
import com.royalit.sreebell.models.UpdateCartResponse
import com.royalit.sreebell.roomdb.AppConstants
import com.royalit.sreebell.roomdb.CartItems
import com.royalit.sreebell.roomdb.CartViewModel
import com.royalit.sreebell.roomdb.MotherChoiceDB
import com.royalit.sreebell.utils.Constants
import com.royalit.sreebell.utils.NetWorkConection
import com.royalit.sreebell.utils.Utilities
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt


class ProductinDetails_Fragment : Fragment() {
    var productID: String? = null
    lateinit var cartQty: IntArray
     var detailsDataResponse: Product_inDetailsResponse?=null


    var bannersData: ArrayList<String> = ArrayList()
    var pic: String? = null
//    lateinit var cartqtty: String

    private var _binding: ProductdetailsScreenBinding? = null
    lateinit var root: View
   // lateinit var motherChoiceDB: com.royalit.sreebell.roomdb.MotherChoiceDB
    var viewModel: com.royalit.sreebell.roomdb.CartViewModel? = null
    var cartItemsList: List<com.royalit.sreebell.roomdb.CartItems> = ArrayList()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var sharedPreferences: SharedPreferences
    lateinit var category_id: String
    lateinit var customerid: String
    lateinit var dist_id: String
     var customer_category: Int=0

    var productIDSB: StringBuilder? = null
    var quantitySB: StringBuilder? = null
    val imageList = ArrayList<SlideModel>() // Create image list
var cart_id=0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = ProductdetailsScreenBinding.inflate(inflater, container, false)
        root = binding.root
        sharedPreferences =
            requireContext().getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )

        productID = sharedPreferences.getString("subcatid", "")!!
        customerid = sharedPreferences.getString("userid", "").toString()
        cartQty = intArrayOf(binding.cartQty.text.toString().toInt())
         dist_id = sharedPreferences.getString("district_id", "")!!
        customer_category = sharedPreferences.getInt("customer_category", 0)!!


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

                if(binding.editQuantity.hasFocus())
                {

                    if(s.toString().length>0&&detailsDataResponse!=null) {
                        if(detailsDataResponse?.pack_size==null||detailsDataResponse?.bags_for_quintals==null|| detailsDataResponse?.pack_size?.isEmpty() == true)
                        {
                            binding.editQuantity.setText("")
                            binding.editBags.setText("")

                            return
                        }
                        val quital = s.toString().toFloat()
                        //val bag=(quital*(detailsDataResponse!!.bags_for_quintals?.toFloat()!!)).roundToInt()
                        val bag=(quital*(detailsDataResponse?.bags_for_quintals?.toFloat()!!)*(detailsDataResponse?.bag_contain_units?.toInt()!!)).roundToInt()
                        binding.editBags.setText(bag.toString())

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

                    if(s.toString().length>0&&detailsDataResponse!=null) {

                        if(detailsDataResponse?.pack_size==null||detailsDataResponse?.bags_for_quintals==null|| detailsDataResponse?.pack_size?.isEmpty() == true)
                        {
                            binding.editQuantity.setText("")
                            binding.editBags.setText("")
                            return
                        }
                        val bags = s.toString().toInt()
                        val bag_in_quintals=
                            detailsDataResponse?.pack_size?.toFloat()
                                ?.times(bags)
                        Log.e("Quintal Added ","Quintal Added  $bag_in_quintals - $bags  - - ${detailsDataResponse?.pack_size}")
                        binding.editQuantity.setText((bag_in_quintals!!/100).toString())

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

        getCategories()
        checkItemAddedInCart()
        return root
    }
    private fun addToCart() {

        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {
            val apiServices = APIClient.client.create(Api::class.java)
            var call:Call<AddtoCartResponse>?=null
            call = apiServices.addToCart(getString(R.string.api_key), customer_id =customerid, product_id = detailsDataResponse?.products_id.toString(), quantity = "1" )

            Log.e("cat", "Add to cart products_id" + detailsDataResponse?.products_id)
            Log.e("cat", "Add to cart customerid " + customerid)
            call.enqueue(object : Callback<AddtoCartResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(call: Call<AddtoCartResponse>, response: Response<AddtoCartResponse>) {
                    Log.e(ContentValues.TAG, response.toString())


                    var cart_id=0
                    response.body()?.cartList?.cart_id?.toInt()

                    if (response.isSuccessful) {
                        try {
                            if(response.body()?.cartList?.cart_id!=null)
                                cart_id=    response.body()?.cartList?.cart_id!!.toInt()
                            getCart()



                        } catch (e: java.lang.NullPointerException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<AddtoCartResponse>, t: Throwable) {
                    Log.e(ContentValues.TAG, t.toString())

                }
            })
        } else {
            Toast.makeText(
                activity as Activity,
                "Please Check your internet",
                Toast.LENGTH_LONG
            ).show()
        }


    }
    private fun updateCart( cartQty:String?) {

        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {
            val apiServices = APIClient.client.create(Api::class.java)
            var call=  apiServices.updateCart(getString(R.string.api_key),customer_id =customerid, quantity = cartQty!!, product_id = detailsDataResponse?.products_id.toString()!! )

            Log.e("cat", "quantity cartQty" + cartQty+" - "+detailsDataResponse?.products_id)

            call?.enqueue(object : Callback<UpdateCartResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(call: Call<UpdateCartResponse>, response: Response<UpdateCartResponse>) {
                    Log.e(ContentValues.TAG, response.toString())
                    getCart()
                    if (response.isSuccessful) {

                        try {

                        } catch (e: java.lang.NullPointerException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<UpdateCartResponse>, t: Throwable) {
                    Log.e(ContentValues.TAG, t.toString())
                }
            })
        } else {
            getCart()
            Toast.makeText(
                activity as Activity,
                "Please Check your internet",
                Toast.LENGTH_LONG
            ).show()
        }


    }


    private fun checkItemAddedInCart() {
       /* val cartVM = CartViewModel(activity, false)
        cartVM.getProductCartDetails(productID, activity)
        cartVM.productDetails.observe(
            requireActivity()
        ) { cartItems: List<CartItems> ->
            Log.e("data==>", Gson().toJson(cartItems))
            if (cartItems.isNotEmpty()) {

                binding.cartQty.text = "" + cartItems[0].getCartQty()
                Log.e("fff", "" + cartItems[0].getCartQty())


            } else {
                binding.cartQty.text = "0"
            }
        }*/
    }

    /* */
    fun getData(
        id: String?,
        typeOfItem: Boolean
    ): ProductinDetails_Fragment? {
        val fragment: ProductinDetails_Fragment =
            ProductinDetails_Fragment()
        val bundle = Bundle()
        bundle.putString("single_product_data", id)
        fragment.arguments = bundle
        return fragment
    }


    private fun addToBag(quantity: String) {
        val items = com.royalit.sreebell.roomdb.CartItems(
            0,
            cart_id,
            com.royalit.sreebell.utils.Utilities.customerid,
            com.royalit.sreebell.utils.Utilities.getDeviceID(activity),
            com.royalit.sreebell.roomdb.AppConstants.PRODUCTS_CART,
            detailsDataResponse?.product_name,
            detailsDataResponse?.product_image,
            quantity,

            java.lang.String.valueOf(detailsDataResponse?.sales_price),
            java.lang.String.valueOf(detailsDataResponse?.offer_price),
            java.lang.String.valueOf(detailsDataResponse?.products_id),
            detailsDataResponse?.stock,
            detailsDataResponse?.max_order_quantity,
            detailsDataResponse?.category_2_price,
            detailsDataResponse?.product_name,
            detailsDataResponse?.product_title,
            detailsDataResponse?.quantity,
            "0",
            "0"
        )
        val viewModel = com.royalit.sreebell.roomdb.CartViewModel(activity)
        viewModel.insert(items)
        Toast.makeText(context, "Item added to cart successfully", Toast.LENGTH_LONG).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding
    }


    private fun getCategories() {

        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)

            val call = productID?.let { apiServices.getindetails(getString(R.string.api_key), it, sharedPreferences.getString("district_id", "")!!,Utilities.customerid) }

            binding.loadingPB.visibility = View.VISIBLE
            call?.enqueue(object : Callback<Product_inDetailsListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<Product_inDetailsListResponse>,
                    response: Response<Product_inDetailsListResponse>
                ) {

                    Log.e(ContentValues.TAG, response.body().toString())
                    binding.loadingPB.visibility = View.GONE

                    if (response.isSuccessful) {

                        try {
                            Log.e("djs", "" + response.body()?.response?.stock)


                            detailsDataResponse = response.body()!!.response

                            binding.priceTVID.text =
                                "\u20B9 " + detailsDataResponse?.sales_price
//                            binding.mrpTVID.text =
//                                "\u20B9 " + Constants.OFFER_PRICE

                            binding.mrpTVID.text =
                                "\u20B9 " + detailsDataResponse?.offer_price


                            var finalDisplayPrice: String = ""
                            detailsDataResponse!!.prices.forEach { price ->



                                if (price.district_id == dist_id) {
                                    if (customer_category == 1) {
                                        finalDisplayPrice = price.distributor_price
                                        return@forEach
                                    } else if (customer_category == 2) {
                                        finalDisplayPrice = price.general_trade_price
                                        return@forEach
                                    }

                                }
                            }
                            if(finalDisplayPrice.isEmpty()||finalDisplayPrice=="0")
                            {
                                binding.mrpTVID.text =
                                    "No purchase option"
                                binding.cartDecBtn.visibility=View.INVISIBLE
                                binding.cartIncBtn.visibility=View.INVISIBLE
                                binding.quantityTVID.visibility=View.INVISIBLE
                            }
                            else
                            {
                                binding.cartDecBtn.visibility=View.VISIBLE
                                binding.quantityTVID.visibility=View.VISIBLE
                                binding.cartIncBtn.visibility=View.VISIBLE
                                binding.mrpTVID.text =
                                    "\u20B9" + finalDisplayPrice
                            }
                            binding.priceTVID.paintFlags =
                                binding.priceTVID.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                            if (detailsDataResponse?.stock?.toInt()==0) {
                                binding.outofstockBtn.visibility = View.VISIBLE
                                binding.txtAddtocart.visibility = View.VISIBLE
                              //  binding.addLayout.visibility = View.GONE
                            } else {
                                binding.outofstockBtn.visibility = View.GONE
                                //binding.addLayout.visibility = View.VISIBLE
                            }

                            val cart=detailsDataResponse?.cart
                            if(cart!=null&&cart.size>0&&cart.get(0).kgs!=null&&detailsDataResponse?.pack_size!=null)
                            {
                                //Log.e("OnCart Items","OnCart Items prices ${cart.get(0).kgs?.toInt()!!} -- ${master_packing_size?.toInt()!!}")

                               binding.editBags.setText("${cart.get(0).kgs?.toInt()!! / detailsDataResponse?.pack_size?.toInt()!!}")

                               binding.editQuantity.setText("${cart.get(0).quintals}")

                            }else
                            {
                               binding.editBags.setText("")
                               binding.editQuantity.setText("")
                            }
                           /* if(binding.editQuantity.text.toString().trim().length>0&&binding.editBags.text.toString().trim().length>0)
                            {
                                binding.txtAddtocart.visibility=View.VISIBLE
                            }else
                            {*/
                            binding.txtAddtocart.visibility = View.VISIBLE
                            // }

                            binding.txtAddtocart.setOnClickListener {
                                binding.editBags.setText("1")
                                binding.editQuantity.setText("1")
                                val carstQty: String = binding.editBags.text.toString()
                                val carstQuintalsQty: String =binding.editQuantity.text.toString()

                                if (carstQty == "0"||carstQty.isEmpty()) {
                                    Toast.makeText(context, "Select quantity..", Toast.LENGTH_LONG)
                                        .show()
                                } else {

                                    if (NetWorkConection.isNEtworkConnected(requireActivity())) {
                                        val product = detailsDataResponse
                                        if(cart!=null&&cart.size>0) {

                                            addToCartQuantity(
                                                product?.products_id.toString(),
                                                product?.pack_size?.toInt()?.times(carstQty.toInt()).toString() ?: "",
                                                carstQuintalsQty,
                                                cart.get(0).id
                                            )
                                        }else
                                        {
                                            addToCartQuantity(
                                                product?.products_id.toString(),
                                                product?.pack_size?.toInt()?.times(carstQty.toInt()).toString() ?: "",
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

                                    Log.e("carstQty", carstQty)
                                }
                            }



//                            if (detailsDataResponse.offer_price != null && !detailsDataResponse.offer_price.equals(
//                                    ""
//                                )
//                            ) {
//                                binding.offTVID.text = "" + detailsDataResponse.offer_price + "%"
//                                binding.offTVID.visibility = View.VISIBLE
//                            } else {
//                                binding.offTVID.visibility = View.GONE
//                            }


                           /* Glide.with(binding.imageSlider.context)
                                .load(detailsDataResponse?.product_image)
                                .placeholder(R.drawable.logo)
                                .into(binding.imageSlider)*/

                            detailsDataResponse?.productImage?.forEach(
                                {
                                    Log.e("Profile Image","Profile Image ${it.fullPath}}")
                                    imageList.add(SlideModel(it.fullPath, ""))

                                }
                            )

                            if(imageList.isEmpty())
                                imageList.add(SlideModel(detailsDataResponse!!.product_image, ""))

                           // imageList.add(SlideModel(detailsDataResponse?.product_image, ""))

                            binding.imageSliderA.setImageList(imageList, scaleType = ScaleTypes.CENTER_CROP)


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                binding.descTVID.text = Html.fromHtml(
                                    detailsDataResponse?.product_information,
                                    Html.FROM_HTML_MODE_COMPACT
                                )
                            } else {
                                binding.descTVID.text =
                                    Html.fromHtml(detailsDataResponse?.product_title)
                            }

                            //Set the Adapter to the RecyclerView//


                            binding.productNameTVID.text = detailsDataResponse?.product_name
                            binding.quantityTVID.text = detailsDataResponse?.quantity + " "


                        } catch (e: java.lang.NullPointerException) {
                            e.printStackTrace()
                        }

                    }


                }

                override fun onFailure(call: Call<Product_inDetailsListResponse>, t: Throwable) {
                    Log.e(ContentValues.TAG, t.toString())
                    binding.loadingPB.visibility = View.GONE

                }

            })


        } else {

            Toast.makeText(
                activity as Activity,
                "Please Check your internet",
                Toast.LENGTH_LONG
            ).show()
        }

    }
     fun addToCartQuantity(product_id: String, kgs: String, quintals: String?, cart_id: String?) {

        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {
            val apiServices = APIClient.client.create(Api::class.java)
            var call: Call<DirectCartAddResponse>? = null
            call = apiServices.directCart(
                getString(R.string.api_key),
                customer_id = customerid,
                product_id = product_id!!,
                kgs = kgs!!,
                quintals = quintals!!,
                cart_id = cart_id!!,
            )

            Log.e("cat", "Add to cart products_id" + product_id)
            Log.e("cat", "Add to cart customerid " + customerid)
            binding.loadingPB.visibility = View.VISIBLE

            call.enqueue(object : Callback<DirectCartAddResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<DirectCartAddResponse>,
                    response: Response<DirectCartAddResponse>
                ) {
                    Log.e(ContentValues.TAG, response.toString())
                    binding.loadingPB.visibility = View.GONE

                    var cart_id = 0


                    if (response.isSuccessful) {
                        try {
                            if(response.body()?.Status == true)
                            {
                                Toast.makeText(requireContext(),"Cart Updated",Toast.LENGTH_SHORT).show()
                            }


                            getCategories()
                            getCart()

                        } catch (e: java.lang.NullPointerException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<DirectCartAddResponse>, t: Throwable) {
                    Log.e(ContentValues.TAG, t.toString())
                    binding.loadingPB.visibility = View.GONE

                }
            })
        } else {
            Toast.makeText(
                activity as Activity,
                "Please Check your internet",
                Toast.LENGTH_LONG
            ).show()
        }


    }
    private fun getCart() {

        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {
            try{
                ( requireActivity() as HomeScreen).updateCartCount()
            }catch (e: java.lang.Exception)
            {

            }
            val apiServices = APIClient.client.create(Api::class.java)
            var call=  apiServices.getCartList(getString(R.string.api_key),customer_id =customerid)



            call?.enqueue(object : Callback<CartListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(call: Call<CartListResponse>, response: Response<CartListResponse>) {
                    Log.e(ContentValues.TAG, response.toString())


                    if (response.isSuccessful) {
                        try {

                            if (response.body()?.cartList?.size!! > 0) {
                                binding.cartQty.text = "0"

                                for (j in response.body()?.cartList!!.indices) {


                                    if (response.body()?.cartList!!.get(j).getProduct_id()
                                            .toInt() == (detailsDataResponse?.products_id)
                                    ) {
                                        binding.cartQty.text=response.body()?.cartList!!.get(j).cartQty
                                        cartQty[0]=response.body()?.cartList!!.get(j).cartQty.toInt()
                                    }
                                }

                                } else {
                                binding.cartQty.text = "0"
                                 }



                        } catch (e: java.lang.NullPointerException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<CartListResponse>, t: Throwable) {
                    Log.e(ContentValues.TAG, t.toString())
                }
            })
        } else {
            Toast.makeText(
                activity as Activity,
                "Please Check your internet",
                Toast.LENGTH_LONG
            ).show()
        }


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkAppMaintanance()
    }
    private fun checkAppMaintanance() {

        if (NetWorkConection.isNEtworkConnected(requireActivity())) {

            val apiServices = APIClient.client.create(Api::class.java)
            var call=  apiServices.checkAppMaintanance(getString(R.string.api_key))


            call?.enqueue(object : Callback<AppMaintananceResponseModel> {
                @SuppressLint("WrongConstant")
                override fun onResponse(call: Call<AppMaintananceResponseModel>, response: Response<AppMaintananceResponseModel>) {
                    Log.e(ContentValues.TAG, response.toString())

                    var isActive=true;
                    var message=""
                    if (response.isSuccessful) {
                        try {
                            val list= response.body()?.response
                            if(list!=null&&list.size>0)
                            {
                                if(list.get(0).message!="online")
                                {
                                    isActive=false
                                    message=list.get(0).reason.toString()

                                }

                            }

                            if(isActive)
                            {

                            }else
                            {
                                val intent = Intent(requireActivity(), AppMaintananceActivity::class.java)
                                intent.putExtra("message",message)
                                startActivity(intent)
                                requireActivity().finish()
                            }


                        } catch (e: java.lang.NullPointerException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<AppMaintananceResponseModel>, t: Throwable) {
                    Log.e(ContentValues.TAG, t.toString())


                }
            })
        } else {
            Toast.makeText(
                requireActivity(),
                "Please Check your internet",
                Toast.LENGTH_LONG
            ).show()
        }


    }
}

