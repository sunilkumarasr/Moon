package com.royalit.sreebell.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.erepairs.app.api.Api
import com.erepairs.app.models.Search_Response
import com.royalit.sreebell.HomeScreen
import com.royalit.sreebell.R
import com.royalit.sreebell.adapter.Search_Adapter
import com.royalit.sreebell.adapter.SubProductList_Adapter
import com.royalit.sreebell.api.APIClient
import com.royalit.sreebell.databinding.SearchScreenBinding
import com.royalit.sreebell.models.AddtoCartResponse
import com.royalit.sreebell.models.CartListResponse
import com.royalit.sreebell.models.Category_subResponse
import com.royalit.sreebell.models.DirectCartAddResponse
import com.royalit.sreebell.models.Search_ListResponse
import com.royalit.sreebell.models.UpdateCartResponse
import com.royalit.sreebell.roomdb.AppConstants
import com.royalit.sreebell.roomdb.CartItems
import com.royalit.sreebell.roomdb.CartViewModel
import com.royalit.sreebell.roomdb.MotherChoiceDB
import com.royalit.sreebell.utils.NetWorkConection
import com.royalit.sreebell.utils.Utilities
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Search_Fragment : Fragment(), Search_Adapter.SearchItemClick {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: SearchScreenBinding? = null
    lateinit var search_adapter: Search_Adapter
    var cartData: List<com.royalit.sreebell.roomdb.CartItems> = ArrayList<com.royalit.sreebell.roomdb.CartItems>()
    lateinit var motherChoiceDB: com.royalit.sreebell.roomdb.MotherChoiceDB
    var viewModel: com.royalit.sreebell.roomdb.CartViewModel? = null
    var cartItemsList: List<com.royalit.sreebell.roomdb.CartItems> = ArrayList()
    lateinit var root: View

    lateinit var productIDStr: String
    lateinit var productQtyStr: String

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var sharedPreferences: SharedPreferences
    lateinit var category_id: String
    lateinit var customerid: String
    var productIDSB: StringBuilder? = null
    var quantitySB: StringBuilder? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(HomeViewModel::class.java)

        _binding = SearchScreenBinding.inflate(inflater, container, false)
        root = binding.root
        sharedPreferences =
            requireContext().getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )

        category_id = sharedPreferences.getString("categoryid", "")!!
        customerid = sharedPreferences.getString("userid", "").toString()
        getCart()
        Log.e("cust", customerid)
        motherChoiceDB =
            Room.databaseBuilder(activity as Activity, com.royalit.sreebell.roomdb.MotherChoiceDB::class.java, "mother-choice")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build()
//        motherChoiceDB = MotherChoiceDB.getInstance(activity as Activity)



        binding.searchETID.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.toString().length > 2) {
                    binding.loadingPB.visibility = View.VISIBLE

                    searchApi(s.toString())
                    getCart()
                }else
                {
                    search_key=""
                }
            }
        })
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding
    }

var search_key=""
    private fun searchApi(search: String) {
            search_key=search
        try {


            if (NetWorkConection.isNEtworkConnected(context as Activity)) {

                //Set the Adapter to the RecyclerView//
                var apiServices = APIClient.client.create(Api::class.java)

                val call =
                    apiServices.getsearch(
                        getString(R.string.api_key),
                        search,Utilities.customerid
                    )
                binding.loadingPB.visibility = View.VISIBLE

                call.enqueue(object : Callback<Search_ListResponse> {
                    override fun onResponse(
                        call: Call<Search_ListResponse>,
                        response: Response<Search_ListResponse>
                    ) {

                        Log.e(ContentValues.TAG, response.toString())
                        binding.loadingPB.visibility = View.GONE

                        try {
                            if (response.isSuccessful) {

                                //Set the Adapter to the RecyclerView//

                                val selectedserviceslist =
                                    response.body()?.response!!

                                search_adapter =
                                    Search_Adapter(
                                        activity as Activity,
                                        selectedserviceslist as java.util.ArrayList<Search_Response>,
                                        cartData, this@Search_Fragment,

                                        sharedPreferences.getInt("customer_category", 0)!!,
                                        sharedPreferences.getString("district_id", "")!!
                                    )
                                binding.searchRCID.adapter = search_adapter

                                search_adapter.notifyDataSetChanged()

                                val layoutManager = LinearLayoutManager(
                                    activity as Activity,
                                    RecyclerView.VERTICAL,
                                    false)
                                binding.searchRCID.layoutManager = layoutManager
                                binding.searchRCID.setItemViewCacheSize(selectedserviceslist.size)

                            }
                        } catch (e: NullPointerException) {
                            e.printStackTrace()
                        } catch (e: TypeCastException) {
                            e.printStackTrace()
                        }


                    }


                    override fun onFailure(call: Call<Search_ListResponse>, t: Throwable) {
                        Log.e(ContentValues.TAG, t.toString())
                        Toast.makeText(
                            context,
                            "Something went wrong",
                            Toast.LENGTH_LONG
                        ).show()

                        binding.loadingPB.visibility = View.GONE

                    }
                })


            } else {
                Toast.makeText(context, "Please Check your internet", Toast.LENGTH_LONG).show()

            }
        } catch (e: UninitializedPropertyAccessException) {
            e.printStackTrace()
        }
    }

    override fun SearchItemClick(itemsData: Search_Response?) {

        ProductinDetails_Fragment().getData(itemsData?.products_id.toString(), true)!!

        val navController =
            Navigation.findNavController(
                context as Activity,
                R.id.nav_host_fragment_content_home_screen
            )
        navController.navigate(R.id.nav_product_details)

        val editor = sharedPreferences.edit()
        editor.putString("subcatid", itemsData?.products_id.toString())
        editor.commit()
    }
    override fun addToCartQuantity(product_id: String, kgs: String, quintals: String?, cart_id: String?) {

        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {
            val apiServices = APIClient.client.create(Api::class.java)
            var call: Call<DirectCartAddResponse>? = null
            call = apiServices.directCart(
                getString(R.string.api_key),
                customer_id = Utilities.customerid,
                product_id = product_id!!,
                quantity = "1",
                kgs = "1",
                quintals = "1",
                cart_id = cart_id!!,
            )

            Log.e("cat", "Add to cart products_id" + product_id)
            Log.e("cat", "Add to cart customerid " + Utilities.customerid)
            //binding.subproductprogress.visibility = View.VISIBLE

            call.enqueue(object : Callback<DirectCartAddResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<DirectCartAddResponse>,
                    response: Response<DirectCartAddResponse>
                ) {
                    Log.e(ContentValues.TAG, response.toString())
                   // binding.subproductprogress.visibility = View.GONE

                    var cart_id = 0


                    if (response.isSuccessful) {
                        try {
                            if(response.body()?.Status == true)
                            {
                                Toast.makeText(requireContext(),"Cart Updated",Toast.LENGTH_SHORT).show()
                               // getviewallProductsList()
                                searchApi(search_key)
                                getCart()
                            }


                        } catch (e: java.lang.NullPointerException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<DirectCartAddResponse>, t: Throwable) {
                    Log.e(ContentValues.TAG, t.toString())
                    //binding.subproductprogress.visibility = View.GONE

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
    override fun onAddToCartClicked(itemsData: Search_Response, cartQty: String?,isAdd:Int) {
        if(isAdd==0) {
            addToCart(itemsData, cartQty)
        }
        else
            updateCart(itemsData, cartQty)
       /* val items = CartItems(
            0,
            0,
            Utilities.customerid,
            Utilities.getDeviceID(activity),
            AppConstants.PRODUCTS_CART,
            itemsData.product_name,
            itemsData.product_image,
            cartQty,
            java.lang.String.valueOf(itemsData.sales_price),
            java.lang.String.valueOf(itemsData.offer_price),
            java.lang.String.valueOf(itemsData.products_id),
            itemsData.stock,
            itemsData.max_order_quantity,
            itemsData.category_2_price,
            itemsData.product_name,
            itemsData.product_title,
            itemsData.quantity
        )
        val viewModel = CartViewModel(activity)
        viewModel.insert(items)
        Toast.makeText(activity, "Item added to cart successfully", Toast.LENGTH_LONG).show()
  */  }
    private fun addToCart(itemsData: Search_Response?,cartQty:String?) {

        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {
            val apiServices = APIClient.client.create(Api::class.java)
            var call:Call<AddtoCartResponse>?=null
            call = apiServices.addToCart(getString(R.string.api_key), customer_id =customerid, product_id = itemsData?.products_id.toString(), quantity = "1" )

            Log.e("cat", "Add to cart products_id" + itemsData?.products_id)
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
                            /* val items = CartItems(
                                 0,
                                 cart_id,
                                 Utilities.customerid,
                                 Utilities.getDeviceID(activity),
                                 AppConstants.PRODUCTS_CART,
                                 itemsData!!.product_name,
                                 itemsData.product_image,
                                 cartQty,
                                 java.lang.String.valueOf(itemsData.sales_price),
                                 java.lang.String.valueOf(itemsData.offer_price),
                                 java.lang.String.valueOf(itemsData.products_id),
                                 itemsData.stock,
                                 itemsData.max_order_quantity,
                                 itemsData.category_2_price,
                                 itemsData.product_name,
                                 itemsData.product_title,
                                 itemsData.quantity
                             )
                             Log.e("stock", "" + itemsData.stock)
                             val viewModel = CartViewModel(activity)
                             viewModel.deleteSingle(items)
                             viewModel.insert(items)*/


                        } catch (e: java.lang.NullPointerException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<AddtoCartResponse>, t: Throwable) {
                    Log.e(ContentValues.TAG, t.toString())

                    getCart()
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
    private fun updateCart(itemsData: Search_Response?,cartQty:String?) {
        try {
            (requireActivity() as HomeScreen).updateCartCount()
        } catch (e: Exception) {

        }
        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {
            val apiServices = APIClient.client.create(Api::class.java)
            var call=  apiServices.updateCart(getString(R.string.api_key),customer_id =customerid, quantity = cartQty!!, product_id = itemsData?.products_id.toString()!! )

            Log.e("cat", "quantity cartQty" + cartQty+" - "+itemsData?.products_id)


            call?.enqueue(object : Callback<UpdateCartResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(call: Call<UpdateCartResponse>, response: Response<UpdateCartResponse>) {
                    Log.e(ContentValues.TAG, response.toString())

                    getCart()
                    if (response.isSuccessful) {

                        try {
                            /*  val items = CartItems(
                                  0,
                                  0,
                                  Utilities.customerid,
                                  Utilities.getDeviceID(activity),
                                  AppConstants.PRODUCTS_CART,
                                  itemsData!!.product_name,
                                  itemsData.product_image,
                                  cartQty,
                                  java.lang.String.valueOf(itemsData.sales_price),
                                  java.lang.String.valueOf(itemsData.offer_price),
                                  java.lang.String.valueOf(itemsData.products_id),
                                  itemsData.stock,
                                  itemsData.max_order_quantity,
                                  itemsData.category_2_price,
                                  itemsData.product_name,
                                  itemsData.product_title,
                                  itemsData.quantity
                              )
                              Log.e("stock", "" + itemsData.stock)
                              val viewModel = CartViewModel(activity)
                              viewModel.insert(items)
  */

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

    private fun getCart() {
        try {
            (requireActivity() as HomeScreen).updateCartCount()
        } catch (e: Exception) {

        }
        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {
            val apiServices = APIClient.client.create(Api::class.java)
            var call=  apiServices.getCartList(getString(R.string.api_key),customer_id =customerid)

            Log.e("cat", " customerid " + customerid)


            call?.enqueue(object : Callback<CartListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(call: Call<CartListResponse>, response: Response<CartListResponse>) {
                    Log.e(ContentValues.TAG, response.toString())


                    if (response.isSuccessful) {
                        try {

                           cartData= response.body()?.cartList!!

                            if (response.body()?.cartList?.size!! > 0) {

                                Log.d("Added Cart Item","Added Cart Item 1 "+response.body()?.cartList?.size!!)

                            } else {
                                Log.d("Added Cart Item","Added Cart Item 0 "+response.body()?.cartList?.size!!)

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

}