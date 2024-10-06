package com.royalit.sreebell.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.viewpager.widget.ViewPager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.erepairs.app.api.Api
import com.erepairs.app.models.AppMaintananceResponseModel
import com.royalit.sreebell.AppMaintananceActivity
import com.royalit.sreebell.HomeScreen
import com.royalit.sreebell.R
import com.royalit.sreebell.adapter.CategoryList_Adapter
import com.royalit.sreebell.adapter.ProductList_Adapter
import com.royalit.sreebell.adapter.SliderPagerAdapter
import com.royalit.sreebell.api.APIClient
import com.royalit.sreebell.databinding.FragmentHomeBinding
import com.royalit.sreebell.models.AddtoCartResponse
import com.royalit.sreebell.models.CartListResponse
import com.royalit.sreebell.models.Category_ListResponse
import com.royalit.sreebell.models.Category_Response
import com.royalit.sreebell.models.DirectCartAddResponse
import com.royalit.sreebell.models.Product_ListResponse
import com.royalit.sreebell.models.Product_Response
import com.royalit.sreebell.models.Slider_Response
import com.royalit.sreebell.models.Sliderlist_Response
import com.royalit.sreebell.models.UpdateCartResponse
import com.royalit.sreebell.utils.NetWorkConection
import com.royalit.sreebell.utils.Utilities
import com.royalit.sreebell.utils.Utilities.customerid
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class HomeFragment : Fragment(), ProductList_Adapter.ProductItemClick {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    lateinit var categoryadapter: CategoryList_Adapter
    lateinit var productadapter: ProductList_Adapter
    lateinit var root: View
    var cartData: List<com.royalit.sreebell.roomdb.CartItems> =
        ArrayList<com.royalit.sreebell.roomdb.CartItems>()
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewPager: ViewPager
    private lateinit var sliderAdapter: SliderPagerAdapter
    private val viewPagerHandler = Handler()
    private var sliderDataList: List<Slider_Response> = emptyList()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding
    lateinit var motherChoiceDB: com.royalit.sreebell.roomdb.MotherChoiceDB
    val imageList = ArrayList<SlideModel>() // Create image list


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

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        root = binding?.root!!

        sharedPreferences =
            requireContext().getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )

        customerid = sharedPreferences.getString("userid", "")!!

        motherChoiceDB =
            Room.databaseBuilder(
                activity as Activity,
                com.royalit.sreebell.roomdb.MotherChoiceDB::class.java,
                "mother-choice"
            )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build()
//        motherChoiceDB = MotherChoiceDB.getInstance(activity as Activity)

        val cartVM = com.royalit.sreebell.roomdb.CartViewModel(activity, false)
        cartVM.cartData()

        val viewModel = com.royalit.sreebell.roomdb.CartViewModel(activity as Activity)
        viewModel.cartCount()

        getCart()

        getCategories()
        getProductsList()

        binding?.viewall!!.setOnClickListener {
            val navController =
                Navigation.findNavController(
                    context as Activity,
                    R.id.nav_host_fragment_content_home_screen
                )
            navController.navigate(R.id.navigation_viewallproducts)

        }

        _binding?.rlSearch!!.setOnClickListener {
            val navController =
                Navigation.findNavController(
                    context as Activity,
                    R.id.nav_host_fragment_content_home_screen
                )
            navController.navigate(R.id.nav_search)
        }

        try {

        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.viewpager) // Initialize viewPager

        // Start auto-scrolling after viewPager is initialized
        //viewPagerHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY.toLong())

        // Call fetchSliderData() to fetch data and set up the ViewPager
        fetchSliderData()
        getCart()
        checkAppMaintanance()
    }

    var nextItem = 0
    private val autoScrollRunnable = object : Runnable {
        val scrollDelay = 1000
        override fun run() {
            Log.d("SliderDebug", "Auto-scrolling to next item")
            var currentItem = viewPager.currentItem

            if (currentItem == (viewPager.adapter?.count?.minus(1))) {
                nextItem = 0
            } else {
                nextItem = currentItem + 1
            }
            // val nextItem = if (currentItem < viewPager.adapter?.count ?: 0 - 1) currentItem + 1 else 0
            Log.d("SliderDebug", "Current item: $currentItem, Next item: $nextItem")

            val startTime = System.currentTimeMillis() // Record the start time

            // Delay the setCurrentItem to allow time for the image to load
            /* viewPagerHandler.postDelayed({
                 val endTime = System.currentTimeMillis() // Record the end time after setting the item
                 val timeTaken = endTime - startTime // Calculate the time taken
                 Log.d("ViewPagerTiming", "Time taken to move to next item: $timeTaken ms")
             }, 1000)

             // Set the next item with a slight delay to allow time for the image to load
             viewPagerHandler.postDelayed({
                 viewPager.setCurrentItem(nextItem, true)
             }, 100) */// Adjust this delay as needed
            viewPager.setCurrentItem(nextItem, true)
            // Schedule the next auto-scroll
            //  viewPagerHandler.postDelayed(this, scrollDelay.toLong())
        }
    }


    private val AUTO_SCROLL_DELAY = 1000 // Adjust the delay as needed
    private fun fetchSliderData() {
        val apiService = APIClient.client.create(Api::class.java)

        val apiKey = "61d7193e52d6er4AuKwW0MjIU61d7193e52d6eYAqnHTf7" // Replace with your API key
        val value = "14" // Replace with your category ID
        val type = "text"

        val call = apiService.getSliders(apiKey, value, type)

        call.enqueue(object : Callback<Sliderlist_Response> {
            override fun onResponse(
                call: Call<Sliderlist_Response>,
                response: Response<Sliderlist_Response>
            ) {
                if (response.isSuccessful) {
                    val sliderResponse = response.body()

                    Log.d("Slider Response", "Slider Response" + sliderResponse.toString())
                    Log.d("Slider Response", "Slider Response" + sliderResponse)
                    if (sliderResponse?.Status == true) {
                        // API response status is true
                        val sliderDataList = sliderResponse.Response
                        // Initialize and set up the ViewPager adapter
                        /* val sliderAdapter = SliderPagerAdapter(requireContext(), sliderDataList)
                         val viewPager = binding!!.viewpager
                         viewPager.adapter = sliderAdapter*/
                        imageList.clear()
                        sliderDataList.forEach({
                            imageList.add(SlideModel(it.full_path, ""))

                        })
                        binding!!.imageSliderA.setImageList(imageList, scaleType = ScaleTypes.FIT)
                        binding!!.imageSliderA

                        // Start auto-scrolling
                        // viewPagerHandler.(autoScrollRunnable, AUTO_SCROLL_DELAY.toLong())
                        // setTimer()
                    } else {
                        // Handle the case when the API response status is not true
                        // You can add code here to handle the error case
                    }
                } else {
                    // Handle the case when the response is not successful
                    // You can add code here to handle the error case
                }
            }

            lateinit var timer: Timer
            val handler = Handler()
            fun setTimer() {
                timer = Timer() // This will create a new Thread

                timer.schedule(object : TimerTask() {
                    // task to be scheduled
                    override fun run() {
                        handler.post(autoScrollRunnable)
                    }
                }, 3000, 3000)
            }

            override fun onFailure(call: Call<Sliderlist_Response>, t: Throwable) {
                // Handle network or API call failure here
                // You can add code here to handle the failure case
            }
        })
    }

    private fun getCategories() {
        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {
            var apiServices = APIClient.client.create(Api::class.java)
            val call =
                apiServices.getcategoruesList(getString(R.string.api_key))
            binding?.homeprogress!!.visibility = View.VISIBLE
            call.enqueue(object : Callback<Category_ListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<Category_ListResponse>,
                    response: Response<Category_ListResponse>
                ) {

                    Log.e(ContentValues.TAG, response.toString())
                    binding?.homeprogress!!.visibility = View.GONE

                    if (response.isSuccessful) {

                        try {
                            val listOfcategories = response.body()?.response

                            //Set the Adapter to the RecyclerView//

                            val selectedserviceslist =
                                response.body()?.response!!

                            activity?.let {
                                categoryadapter =
                                    CategoryList_Adapter(
                                        activity as Activity,
                                        selectedserviceslist as ArrayList<Category_Response>
                                    )

                                binding?.categoryList!!.adapter =
                                    categoryadapter
                                categoryadapter.notifyDataSetChanged()
                                val layoutManager = LinearLayoutManager(
                                    activity as Activity,
                                    RecyclerView.HORIZONTAL, // Set to horizontal
                                    false
                                )
                                binding?.categoryList!!.layoutManager = layoutManager

                                binding?.categoryList!!.setItemViewCacheSize(selectedserviceslist.size)

                            }
                        } catch (e: java.lang.NullPointerException) {
                            e.printStackTrace()
                        }

                    }

                }

                override fun onFailure(call: Call<Category_ListResponse>, t: Throwable) {
                    Log.e(ContentValues.TAG, t.toString())
                    binding?.homeprogress!!.visibility = View.GONE
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


    private fun getProductsList() {
        try {


            if (NetWorkConection.isNEtworkConnected(activity as Activity)) {

                //Set the Adapter to the RecyclerView//

                var apiServices = APIClient.client.create(Api::class.java)

                val call =
                    apiServices.getproductsList(getString(R.string.api_key), customerid)

                binding?.homeprogress!!.visibility = View.VISIBLE
                call.enqueue(object : Callback<Product_ListResponse> {
                    @SuppressLint("WrongConstant")
                    override fun onResponse(
                        call: Call<Product_ListResponse>,
                        response: Response<Product_ListResponse>
                    ) {

                        Log.e(ContentValues.TAG, response.toString())
                        binding!!.homeprogress.visibility = View.GONE

                        if (response.isSuccessful) {

                            try {

                                val listOfcategories = response.body()?.response

                                //Set the Adapter to the RecyclerView//

                                val selectedserviceslist =
                                    response.body()?.response!!
                                //val filterList: List<Product_Response> = ArrayList<Product_Response>()
                                //val filterList: List<Product_Response> = ArrayList<Product_Response>()
                                val filterList: MutableList<Product_Response> = ArrayList()

                                selectedserviceslist.forEach { productItem ->

                                    productItem.prices.forEach { price ->

                                            var canWeAddRecord: Boolean = false
                                            if (price.district_id == sharedPreferences.getString(
                                                    "district_id",
                                                    ""
                                                )!!
                                            ) {
                                                canWeAddRecord = true
                                            }
                                            if (canWeAddRecord)
                                                filterList.add(productItem)
                                    }
                                }

                                productadapter =
                                    ProductList_Adapter(
                                        activity as Activity,
                                        filterList as ArrayList<Product_Response>,
                                        cartData, this@HomeFragment,
                                        sharedPreferences.getInt("customer_category", 0)!!,
                                        sharedPreferences.getString("district_id", "")!!
                                    )

                                binding?.productsList!!.layoutManager = GridLayoutManager(activity, 2)
                                binding?.productsList!!.adapter = productadapter
                                binding?.productsList!!.setHasFixedSize(true)
                                productadapter.notifyDataSetChanged()

                            } catch (e: java.lang.NullPointerException) {
                                e.printStackTrace()
                            }
                        }

                    }

                    override fun onFailure(call: Call<Product_ListResponse>, t: Throwable) {
                        Log.e(ContentValues.TAG, t.toString())
                        binding?.homeprogress!!.visibility = View.GONE

                    }


                })


            } else {

                Toast.makeText(
                    activity as Activity,
                    "Please Check your internet",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding
    }

    override fun onProductItemClick(itemsData: Product_Response?) {
    }
    private fun addToCart(itemsData: Product_Response?, cartQty: String?) {

        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {
            val apiServices = APIClient.client.create(Api::class.java)
            var call: Call<AddtoCartResponse>? = null
            call = apiServices.addToCart(
                getString(R.string.api_key),
                customer_id = customerid,
                product_id = itemsData?.products_id.toString(),
                quantity = "1"
            )

            Log.e("cat", "Add to cart products_id" + itemsData?.products_id)
            Log.e("cat", "Add to cart customerid " + customerid)

            call.enqueue(object : Callback<AddtoCartResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<AddtoCartResponse>,
                    response: Response<AddtoCartResponse>
                ) {
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {
                        try {
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

    private fun updateCart(itemsData: Product_Response?, cartQty: String?) {

        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {
            val apiServices = APIClient.client.create(Api::class.java)
            var call = apiServices.updateCart(
                getString(R.string.api_key),
                customer_id = customerid,
                quantity = cartQty!!,
                product_id = itemsData?.products_id.toString()!!
            )

            Log.e("cat", "quantity cartQty" + cartQty + " - " + itemsData?.products_id)

            call?.enqueue(object : Callback<UpdateCartResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<UpdateCartResponse>,
                    response: Response<UpdateCartResponse>
                ) {
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

    override fun onAddToCartClicked(itemsData: Product_Response?, cartQty: String?, isAdd: Int) {

        if (isAdd == 0) {
            addToCart(itemsData, cartQty)
        } else
            updateCart(itemsData, cartQty)
        /*val items = CartItems(
            0,
            itemsData?.cartID!!,
            Utilities.customerid,
            Utilities.getDeviceID(activity),
            AppConstants.PRODUCTS_CART,
            itemsData?.product_name,
            itemsData?.product_image,
            cartQty,
            java.lang.String.valueOf(itemsData?.sales_price),
            java.lang.String.valueOf(itemsData?.offer_price),
            java.lang.String.valueOf(itemsData?.products_id),
            itemsData?.stock,
            itemsData?.max_order_quantity,
            itemsData?.category_2_price,
            itemsData?.product_name,
            itemsData?.product_title,
            itemsData?.quantity
        )
        val viewModel = CartViewModel(activity)
        viewModel.insert(items)*/
        Toast.makeText(context, "Item added to cart successfully", Toast.LENGTH_LONG).show()
    }

    private fun getCart() {
        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {
            try {
                (requireActivity() as HomeScreen).updateCartCount()
            } catch (e: Exception) {

            }
            val apiServices = APIClient.client.create(Api::class.java)
            var call = apiServices.getCartList(
                getString(R.string.api_key),
                customer_id = com.royalit.sreebell.utils.Utilities.customerid
            )

            Log.e("cat", " customerid " + com.royalit.sreebell.utils.Utilities.customerid)

            call?.enqueue(object : Callback<CartListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<CartListResponse>,
                    response: Response<CartListResponse>
                ) {
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {
                        try {

                            val cartViewModel =
                                com.royalit.sreebell.roomdb.CartViewModel(activity as Activity)

                            cartData = response.body()?.cartList!!

                            try {
                                productadapter.notifyDataSetChanged()
                            } catch (e: Exception) {

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
    override fun addToCartQuantity(product_id: String, kgs: String, quintals: String?, cart_id: String?) {

        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {
            val apiServices = APIClient.client.create(Api::class.java)
            var call: Call<DirectCartAddResponse>? = null
            call = apiServices.directCart(
                getString(R.string.api_key),
                customer_id = Utilities.customerid,
                product_id = product_id!!,
                kgs = kgs!!,
                quintals = quintals!!,
                cart_id = cart_id!!,
            )

            Log.e("cat", "Add to cart products_id" + product_id)
            Log.e("cat", "Add to cart customerid " + Utilities.customerid)
            binding?.homeprogress?.visibility = View.VISIBLE

            call.enqueue(object : Callback<DirectCartAddResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<DirectCartAddResponse>,
                    response: Response<DirectCartAddResponse>
                ) {
                    Log.e(ContentValues.TAG, response.toString())
                    binding?.homeprogress?.visibility = View.GONE

                    var cart_id = 0


                    if (response.isSuccessful) {
                        try {
                            if(response.body()?.Status == true)
                            {
                                Toast.makeText(requireContext(),"Cart Updated",Toast.LENGTH_SHORT).show()
                                getProductsList()
                                getCart()
                            }


                        } catch (e: java.lang.NullPointerException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<DirectCartAddResponse>, t: Throwable) {
                    Log.e(ContentValues.TAG, t.toString())
                    binding?.homeprogress?.visibility = View.GONE

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


