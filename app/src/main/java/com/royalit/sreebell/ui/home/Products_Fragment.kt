package com.royalit.sreebell.ui.home


import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TableLayout
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.erepairs.app.api.Api
import com.erepairs.app.models.AppMaintananceResponseModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.royalit.sreebell.AppMaintananceActivity
import com.royalit.sreebell.HomeScreen
import com.royalit.sreebell.R
import com.royalit.sreebell.adapter.SubProductList_Adapter
import com.royalit.sreebell.api.APIClient
import com.royalit.sreebell.databinding.ProductsScreenBinding
import com.royalit.sreebell.models.AddtoCartResponse
import com.royalit.sreebell.models.CartListResponse
import com.royalit.sreebell.models.Category_Response
import com.royalit.sreebell.models.Category_subResponse
import com.royalit.sreebell.models.DirectCartAddResponse
import com.royalit.sreebell.models.Product_ListResponse
import com.royalit.sreebell.models.Product_Response
import com.royalit.sreebell.models.UpdateCartResponse
import com.royalit.sreebell.roomdb.CartItems
import com.royalit.sreebell.utils.Constants
import com.royalit.sreebell.utils.NetWorkConection
import com.royalit.sreebell.utils.Utilities
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Products_Fragment : Fragment(), SubProductList_Adapter.ProductItemClick {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: ProductsScreenBinding? = null
    lateinit var subproduct_adapter: SubProductList_Adapter
    var cartData: List<com.royalit.sreebell.roomdb.CartItems> =
        ArrayList<com.royalit.sreebell.roomdb.CartItems>()
    lateinit var motherChoiceDB: com.royalit.sreebell.roomdb.MotherChoiceDB
    lateinit var root: View
    private var isSortAscending = true
    private var selectedserviceslist: List<Product_Response> = ArrayList()
    private var lastVisibleItemPosition: Int = 0

    private val binding get() = _binding!!
    lateinit var sharedPreferences: SharedPreferences
    lateinit var category_id: String
     var customer_category: Int=0
    lateinit var district_id: String
    lateinit var value: String
    lateinit var customerid: String
    private var query = ""

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

        _binding = ProductsScreenBinding.inflate(inflater, container, false)
        root = binding.root
        sharedPreferences =
            requireContext().getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )
        customerid = sharedPreferences.getString("userid", "").toString()
        district_id = sharedPreferences.getString("district_id", "").toString()
        customer_category = sharedPreferences.getInt("customer_category", 0)


        val edittextSearch = binding.edittextSearch


        binding.edittextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not used
                val query = s.toString()


                if (query.length >=2) {


                    try {
                        val selectedserviceslistSearch= arrayListOf<Product_Response>()
                        selectedserviceslistSearch.addAll(selectedserviceslist as ArrayList<Product_Response>)
                        val filteredProducts=selectedserviceslistSearch.filter {
                            it.product_name.toLowerCase().contains(query.toLowerCase())
                        }
                        subproduct_adapter = SubProductList_Adapter(
                            activity as Activity,
                            filteredProducts as ArrayList<Product_Response>,
                            cartData as ArrayList<CartItems>,
                            this@Products_Fragment, sharedPreferences.getInt("customer_category", 0)!!,
                            sharedPreferences.getString("district_id", "")!!
                        )

                        Log.d("selectedserviceslist ","selectedserviceslist with name ${selectedserviceslist.size}")
                        binding.subproductlist.adapter = subproduct_adapter
                        subproduct_adapter.notifyDataSetChanged()
                     /* val d = query.toInt()
                        val selectedserviceslistSearch= arrayListOf<Product_Response>()
                        selectedserviceslistSearch.addAll(selectedserviceslist as ArrayList<Product_Response>)


                        val filteredProducts= arrayListOf<Product_Response>()
                        selectedserviceslist.forEach {


                            if(it.prices!=null&&it.prices.size>0) {


                                it.prices.forEach({price->

                                    val distId=price.district_id
                                    Log.d("selectedserviceslist ","selectedserviceslist with price ${it.prices.size}  -- ${category_id} dist ${distId} dist2 ${district_id} -- ${price.distributor_price} --- ${price.general_trade_price} -0- ${query}")


                                    if(distId!=null&&distId.isNotEmpty()&&distId==district_id)
                                    {
                                        if (category_id.toInt() == 1) {
                                            if (price.distributor_price.contains(query))
                                                filteredProducts.add(it)
                                        } else if (category_id.toInt() == 2) {
                                            if (price.general_trade_price.contains(query))
                                                filteredProducts.add(it)
                                        }
                                    }
                                })

                            }
                        }

                        Log.d("selectedserviceslist ","selectedserviceslist with price ${filteredProducts.size}  -- ${selectedserviceslistSearch.size}")

                        subproduct_adapter = SubProductList_Adapter(
                            activity as Activity,
                            filteredProducts as ArrayList<Product_Response>,
                            cartData as ArrayList<CartItems>,
                            this@Products_Fragment, sharedPreferences.getInt("customer_category", 0)!!,
                            sharedPreferences.getString("district_id", "")!!
                        )

                        binding.subproductlist.adapter = subproduct_adapter
                        subproduct_adapter.notifyDataSetChanged()*/
                    }
                    catch (e: Exception) {

                        val selectedserviceslistSearch= arrayListOf<Product_Response>()
                        selectedserviceslistSearch.addAll(selectedserviceslist as ArrayList<Product_Response>)
                        val filteredProducts=selectedserviceslistSearch.filter {
                            it.product_name.toLowerCase().contains(query.toLowerCase())
                        }
                        subproduct_adapter = SubProductList_Adapter(
                            activity as Activity,
                            filteredProducts as ArrayList<Product_Response>,
                            cartData as ArrayList<CartItems>,
                            this@Products_Fragment, sharedPreferences.getInt("customer_category", 0)!!,
                            sharedPreferences.getString("district_id", "")!!
                        )

                        Log.d("selectedserviceslist ","selectedserviceslist with name ${selectedserviceslist.size}")
                        binding.subproductlist.adapter = subproduct_adapter
                        subproduct_adapter.notifyDataSetChanged()

                    }
                    // Fetch products based on the offer price
                    // fetchProductsByOfferPrice(query)

                } else {
                    Log.d("selectedserviceslist ","selectedserviceslist ${selectedserviceslist.size}")
                    subproduct_adapter = SubProductList_Adapter(
                        activity as Activity,
                        selectedserviceslist as ArrayList<Product_Response>,
                        cartData as ArrayList<CartItems>,
                        this@Products_Fragment, sharedPreferences.getInt("customer_category", 0)!!,
                        sharedPreferences.getString("district_id", "")!!
                    )

                    binding.subproductlist.adapter = subproduct_adapter
                    subproduct_adapter.notifyDataSetChanged()
                }

               /* if (query.length >= 2) {


                    try {
                        val d = query.toInt()
                        val selectedserviceslistSearch = arrayListOf<Product_Response>()
                        selectedserviceslistSearch.addAll(selectedserviceslist as ArrayList<Product_Response>)
                        // selectedserviceslistSearch.filterTo()
                        val filteredProducts = selectedserviceslistSearch.filter {
                            Log.d(
                                "Price compare",
                                "Price compare ${it.offer_price.toInt()} ${query.toInt()} : ${it.offer_price.toInt() == query.toInt()}"
                            )
                            it.offer_price.toInt() == query.toInt()
                        }
                        subproduct_adapter = SubProductList_Adapter(
                            activity as Activity,
                            filteredProducts as ArrayList<Product_Response>,
                            cartData as ArrayList<com.royalit.sreebell.roomdb.CartItems>,
                            this@Products_Fragment,
                            sharedPreferences.getInt("customer_category", 0)!!,
                            sharedPreferences.getString("district_id", "")!!
                        )

                        binding.subproductlist.adapter = subproduct_adapter
                        subproduct_adapter.notifyDataSetChanged()
                    } catch (e: Exception) {

                        Log.d("Search Product", "Search with Product Name")
                        val selectedserviceslistSearch = arrayListOf<Product_Response>()
                        selectedserviceslistSearch.addAll(selectedserviceslist as ArrayList<Product_Response>)
                        val filteredProducts = selectedserviceslistSearch.filter {
                            Log.d(
                                "Search Product",
                                "Search with Product Name${it.product_name} ${it.product_title}"
                            )
                            it.product_name.contains(query)
                        }
                        subproduct_adapter = SubProductList_Adapter(
                            activity as Activity,
                            filteredProducts as ArrayList<Product_Response>,
                            cartData as ArrayList<com.royalit.sreebell.roomdb.CartItems>,
                            this@Products_Fragment,
                            sharedPreferences.getInt("customer_category", 0)!!,
                            sharedPreferences.getString("district_id", "")!!
                        )

                        binding.subproductlist.adapter = subproduct_adapter
                        subproduct_adapter.notifyDataSetChanged()

                    }
                    // Fetch products based on the offer price
                    // fetchProductsByOfferPrice(query)

                } else {
                    Log.d(
                        "selectedserviceslist ",
                        "selectedserviceslist ${selectedserviceslist.size}"
                    )
                    subproduct_adapter = SubProductList_Adapter(
                        activity as Activity,
                        selectedserviceslist as ArrayList<Product_Response>,
                        cartData as ArrayList<com.royalit.sreebell.roomdb.CartItems>,
                        this@Products_Fragment,
                        sharedPreferences.getInt("customer_category", 0)!!,
                        sharedPreferences.getString("district_id", "")!!
                    )

                    binding.subproductlist.adapter = subproduct_adapter
                    subproduct_adapter.notifyDataSetChanged()
                }*/


            }

            override fun afterTextChanged(s: Editable?) {

            }
        })



        binding.filterBtn.setOnClickListener { view ->
            val popupMenu = PopupMenu(requireContext(), view)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

            // Set a click listener for the items in the pop-up menu
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.sort_low_to_high -> {
                        // Sort products in ascending order (Low to High)
                        isSortAscending = true
                        sortProductsByOfferPrice()
                        return@setOnMenuItemClickListener true
                    }

                    R.id.sort_high_to_low -> {
                        // Sort products in ascending order (Low to High)
                        isSortAscending = false
                        sortProductsByOfferPrice()
                        return@setOnMenuItemClickListener true
                    }

                    else -> false
                }
            }

            // Show the pop-up menu
            popupMenu.show()
        }

        category_id = sharedPreferences.getString("categoryid", "")!!
        motherChoiceDB =
            Room.databaseBuilder(
                activity as Activity,
                com.royalit.sreebell.roomdb.MotherChoiceDB::class.java,
                "mother-choice"
            )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build()


        val recyclerView = binding.subproductlist
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        subproduct_adapter =
            SubProductList_Adapter(
                activity as Activity,
                ArrayList(),
                cartData as ArrayList<com.royalit.sreebell.roomdb.CartItems>,
                this,
                sharedPreferences.getInt("customer_category", 0)!!,
                sharedPreferences.getString("district_id", "")!!
            )
        recyclerView.adapter = subproduct_adapter

        getProducts()

        return root
    }


    override fun addToCartQuantity(product_id: String, kgs: String, quintals: String?, cart_id: String?) {

        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {
            val apiServices = APIClient.client.create(Api::class.java)
            var call: Call<DirectCartAddResponse>? = null
            call = apiServices.directCart(
                getString(R.string.api_key),
                customer_id = customerid,
                product_id = product_id!!,
                quantity = "1",
                kgs = "1",
                quintals = "1",
                cart_id = cart_id!!,
            )

            Log.e("cat", "Add to cart products_id" + product_id)
            Log.e("cat", "Add to cart customerid " + customerid)
            binding.subproductprogress.visibility = View.VISIBLE

            call.enqueue(object : Callback<DirectCartAddResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<DirectCartAddResponse>,
                    response: Response<DirectCartAddResponse>
                ) {
                    Log.e(ContentValues.TAG, response.toString())
                    binding.subproductprogress.visibility = View.GONE

                    var cart_id = 0


                    if (response.isSuccessful) {
                        try {
                            if(response.body()?.Status == true)
                            {
                                Toast.makeText(requireContext(),"Cart Updated",Toast.LENGTH_SHORT).show()
                                getProducts()
                            }


                        } catch (e: java.lang.NullPointerException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<DirectCartAddResponse>, t: Throwable) {
                    Log.e(ContentValues.TAG, t.toString())
                    binding.subproductprogress.visibility = View.GONE

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
    private fun addToCart(product_id: String?, cartQty: String?) {

        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {
            val apiServices = APIClient.client.create(Api::class.java)
            var call: Call<AddtoCartResponse>? = null
            call = apiServices.addToCart(
                getString(R.string.api_key),
                customer_id = customerid,
                product_id = product_id!!,
                quantity = "1"
            )

            Log.e("cat", "Add to cart products_id" + product_id)
            Log.e("cat", "Add to cart customerid " + customerid)
            binding.subproductprogress.visibility = View.VISIBLE

            call.enqueue(object : Callback<AddtoCartResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<AddtoCartResponse>,
                    response: Response<AddtoCartResponse>
                ) {
                    Log.e(ContentValues.TAG, response.toString())
                    binding.subproductprogress.visibility = View.GONE

                    var cart_id = 0
                    response.body()?.cartList?.cart_id?.toInt()

                    if (response.isSuccessful) {
                        try {
                            if (response.body()?.cartList?.cart_id != null)
                                cart_id = response.body()?.cartList?.cart_id!!.toInt()
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
                    binding.subproductprogress.visibility = View.GONE
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

    private fun updateCart(product_id: String?, cartQty: String?) {

        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {
            val apiServices = APIClient.client.create(Api::class.java)
            var call = apiServices.updateCart(
                getString(R.string.api_key),
                customer_id = customerid,
                quantity = cartQty!!,
                product_id = product_id!!
            )

            Log.e("cat", "quantity cartQty" + cartQty + " - " + product_id)
            binding.subproductprogress.visibility = View.VISIBLE

            call?.enqueue(object : Callback<UpdateCartResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<UpdateCartResponse>,
                    response: Response<UpdateCartResponse>
                ) {
                    Log.e(ContentValues.TAG, response.toString())
                    binding.subproductprogress.visibility = View.GONE
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
                    binding.subproductprogress.visibility = View.GONE
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

        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {
            try {
                (requireActivity() as HomeScreen).updateCartCount()
            } catch (e: java.lang.Exception) {

            }
            val apiServices = APIClient.client.create(Api::class.java)
            var call =
                apiServices.getCartList(getString(R.string.api_key), customer_id = customerid)

            Log.e("cat", " customerid " + customerid)
            binding.subproductprogress.visibility = View.VISIBLE

            call?.enqueue(object : Callback<CartListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<CartListResponse>,
                    response: Response<CartListResponse>
                ) {
                    Log.e(ContentValues.TAG, response.toString())
                    binding.subproductprogress.visibility = View.GONE

                    if (response.isSuccessful) {
                        try {

                            if (response.body()?.cartList?.size!! > 0) {
                                binding.cartcount.visibility = View.VISIBLE
                                //binding.cvOneLogin.visibility = View.VISIBLE
                                binding.carttext.visibility = View.VISIBLE
                                binding.rlOneLogin.visibility = View.VISIBLE
                                binding.cartcount.text = "" + response.body()?.cartList?.size!!

                                Log.d(
                                    "Added Cart Item",
                                    "Added Cart Item 1 " + response.body()?.cartList?.size!!
                                )

                            } else {
                                binding.cartcount.visibility = View.GONE
                                binding.cvOneLogin.visibility = View.GONE
                                binding.carttext.visibility = View.GONE
                                binding.rlOneLogin.visibility = View.GONE
                                binding.cartcount.text = ""
                                Log.d(
                                    "Added Cart Item",
                                    "Added Cart Item 0 " + response.body()?.cartList?.size!!
                                )

                            }
                            subproduct_adapter.setCartList(response.body()?.cartList!! as ArrayList<com.royalit.sreebell.roomdb.CartItems>)

                            /*subproduct_adapter =
                                SubProductList_Adapter(
                                    requireActivity() as Activity,
                                    selectedserviceslist as ArrayList<Category_subResponse>,
                                    response.body()?.cartList  as ArrayList<CartItems>, this@Products_Fragment
                                )
                            binding.subproductlist.adapter = subproduct_adapter
                          */
                            /*if(subproduct_adapter!=null) {
                                subproduct_adapter.cartData?.clear()
                                subproduct_adapter.cartData?.addAll(cartData)
                                subproduct_adapter.notifyDataSetChanged()
                            }*/


                            /*cartViewModel.getCartItems.observe(requireActivity()) {
                                    cartItems -> cartData = cartItems
                                Log.d("Cartview Items","Cartview Items 2 ${cartItems.size}")
                            }*/

                        } catch (e: java.lang.NullPointerException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<CartListResponse>, t: Throwable) {
                    Log.e(ContentValues.TAG, t.toString())
                    binding.subproductprogress.visibility = View.GONE
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


    private fun sortProductsByOfferPrice() {     //filtr btn
        subproduct_adapter.sortByOfferPrice(ascending = isSortAscending)
        subproduct_adapter.notifyDataSetChanged()

    }


    private fun filterAndSortProducts(
        products: List<Category_subResponse>,
        query: String
    ): List<Category_subResponse> {
        return products.filter { it.offer_price.contains(query, ignoreCase = true) }
            .sortedBy {
                it.offer_price.toDoubleOrNull() ?: 0.0
            }// Filter based on product name matching the query
    }


    private fun getProducts() {

        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)

            val call =
                apiServices.getproductsList(getString(R.string.api_key),customerid)

            Log.e("cat", "" + category_id)
            binding.subproductprogress.visibility = View.VISIBLE
            call.enqueue(object : Callback<Product_ListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<Product_ListResponse>,
                    response: Response<Product_ListResponse>
                ) {

                    Log.e(ContentValues.TAG, response.toString())
                    binding.subproductprogress.visibility = View.GONE
                    getCart()
                    if (response.isSuccessful) {

                        try {
                             selectedserviceslist =
                                response.body()?.response!!
                            val filterList: MutableList<Product_Response> = ArrayList()


                            selectedserviceslist.forEach { productItem ->
                                if (category_id == productItem.categories_id) {

                                    productItem.prices.forEach { price ->

                                        var canWeAddRecord: Boolean = false
                                        if (price.district_id == sharedPreferences.getString(
                                                "district_id",
                                                ""
                                            )!!
                                        ) {
                                            canWeAddRecord = true
                                        }



                                    }

                                        filterList.add(productItem)
                                }


                            }
                            subproduct_adapter.setProductsList(filterList as ArrayList<Product_Response>)
                            if(subproduct_adapter.itemCount>0)
                            {
                                binding.txtNoData.visibility=View.GONE
                            }else
                            {
                                binding.txtNoData.visibility=View.VISIBLE
                            }
                        } catch (e: java.lang.NullPointerException) {
                            e.printStackTrace()
                        }

                    }



                }

                override fun onFailure(call: Call<Product_ListResponse>, t: Throwable) {
                    Log.e(ContentValues.TAG, t.toString())
                    binding.subproductprogress.visibility = View.GONE
                    if(subproduct_adapter.itemCount>0)
                    {
                        binding.txtNoData.visibility=View.GONE
                    }else
                    {
                        binding.txtNoData.visibility=View.VISIBLE
                    }
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding
    }

    override fun onProductItemClick(itemsData: Product_Response?) {

        ProductinDetails_Fragment().getData(itemsData?.products_id.toString(), true)!!
        val navController =
            Navigation.findNavController(
                context as Activity,
                R.id.nav_host_fragment_content_home_screen
            )
        var cust_category = sharedPreferences.getInt("customer_category", 0)!!
        var dist_id = sharedPreferences.getString("district_id", "")!!
        var finalDisplayPrice: String = ""
        itemsData?.prices?.forEach { price ->
            if (price.district_id == dist_id) {
                if (cust_category == 1) {
                    finalDisplayPrice = price.distributor_price
                } else if (cust_category== 2) {
                    finalDisplayPrice = price.general_trade_price
                }
                return@forEach
            }
        }
        Constants.OFFER_PRICE = finalDisplayPrice
        navController.navigate(R.id.nav_product_details)


        val editor = sharedPreferences.edit()
        editor.putString("subcatid", itemsData?.products_id.toString())
        editor.commit()
    }

    override fun onAddToCartClicked(
        itemsData: Product_Response?,
        cartQty: String?,
        isAdd: Int
    ) {
        if (isAdd == 0) {
            addToCart(itemsData?.products_id.toString(), cartQty)
        } else
            updateCart(itemsData?.products_id.toString(), cartQty)
        // Toast.makeText(activity, "Item added to cart successfully", Toast.LENGTH_LONG).show()
    }

    override fun onItemVisibility(visibility: Boolean) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoryList=arguments?.getParcelableArrayList<Category_Response>("categories")
        Log.e("categoryList","categoryList ${categoryList}")
       val tablayout= binding.tablayout as TabLayout
        if (categoryList != null) {
            for (c in categoryList)
                tablayout.addTab(tablayout.newTab().setText("${c.category_name}"))
        }
        for(k in 0..categoryList?.size!!-1)
        {
            val id=categoryList.get(k).categories_id.toString()
            if(category_id==id)
            {
                tablayout.selectTab(tablayout.getTabAt(k))
            }
        }

        tablayout.setOnTabSelectedListener(object:OnTabSelectedListener{
            override fun onTabSelected(p0: TabLayout.Tab?) {
                if (p0 != null) {
                    sharedPreferences.edit().putString("categoryid",
                        categoryList?.get(p0.position)?.categories_id.toString()
                    ).commit()!!

                    category_id = sharedPreferences.getString("categoryid", "")!!
                }
                getProducts()
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

        })
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