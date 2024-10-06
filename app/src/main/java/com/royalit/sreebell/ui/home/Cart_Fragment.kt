package com.royalit.sreebell.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.room.Room
import com.erepairs.app.adapter.Cart_Adapter
import com.erepairs.app.api.Api
import com.erepairs.app.models.AppMaintananceResponseModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Task
import com.royalit.sreebell.AppMaintananceActivity
import com.royalit.sreebell.HomeScreen
import com.royalit.sreebell.R
import com.royalit.sreebell.api.APIClient
import com.royalit.sreebell.databinding.CartScreenBinding
import com.royalit.sreebell.models.AddtoCartResponse
import com.royalit.sreebell.models.CartListResponse
import com.royalit.sreebell.models.DeleteCartResponse
import com.royalit.sreebell.models.DirectCartAddResponse
import com.royalit.sreebell.models.Placeorder_ListResponse
import com.royalit.sreebell.models.UpdateCartResponse
import com.royalit.sreebell.utils.LocationUtils
import com.royalit.sreebell.utils.NetWorkConection
import com.vanillaplacepicker.presentation.builder.VanillaPlacePicker
import com.vanillaplacepicker.utils.MapType
import com.vanillaplacepicker.utils.PickerLanguage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable


class Cart_Fragment : Fragment(), Cart_Adapter.ProductItemClick,
    Cart_Adapter.CartItemQuantityChangeListener {
    private lateinit var fullAddress: String
    lateinit var usersubcategory_id_: String

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: CartScreenBinding? = null
    lateinit var cart_adapter: Cart_Adapter
    var cartData: List<com.royalit.sreebell.roomdb.CartItems> =
        ArrayList<com.royalit.sreebell.roomdb.CartItems>()
    lateinit var motherChoiceDB: com.royalit.sreebell.roomdb.MotherChoiceDB
    var viewModel: com.royalit.sreebell.roomdb.CartViewModel? = null
    var cartItemsList: List<com.royalit.sreebell.roomdb.CartItems> = ArrayList()
    lateinit var root: View
    lateinit var cartItemListener: Cart_Adapter.CartItemQuantityChangeListener

    lateinit var productIDStr: String
    lateinit var productQtyStr: String
    lateinit var product_quintals: String
    lateinit var product_bags: String
    lateinit var priceIds: String
    lateinit var address: String
    lateinit var username: String

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var sharedPreferences: SharedPreferences
    lateinit var category_id: String
    lateinit var customerid: String
    var productIDSB: StringBuilder? = null
    var quantitySB: StringBuilder? = null
    val cartVM = null
    var adapter: Cart_Adapter? = null
    lateinit var productItemClick: Cart_Adapter.ProductItemClick
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var currentLat: Double = 0.0
    private var currentLng: Double = 0.0
    private lateinit var locationUtils: LocationUtils
    private var selectedLat = "0.0";
    private var selectedLng = "0.0";
    var cust_category=0
    var dist_id=""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkAppMaintanance()
        val  sharedPreferences =
            requireContext().getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )

        cust_category=  sharedPreferences.getInt("customer_category", 0)!!
        dist_id=sharedPreferences.getString("district_id", "")!!
    }

    override fun onDetach() {
        super.onDetach()
        stopLocationUpdates()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cartItemListener = this
        homeViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(HomeViewModel::class.java)

        _binding = CartScreenBinding.inflate(inflater, container, false)
        root = binding.root
        fullAddress = ""

        locationUtils = LocationUtils(requireContext())

        sharedPreferences =
            requireContext().getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )
        productItemClick = this
        category_id = sharedPreferences.getString("categoryid", "")!!
        customerid = sharedPreferences.getString("userid", "").toString()
        address = sharedPreferences.getString("address", "").toString()
        username = sharedPreferences.getString("username", "").toString()
        fullAddress=address
        Log.e("cust", customerid)
        Log.e("address", address)
        motherChoiceDB =
            Room.databaseBuilder(
                activity as Activity,
                com.royalit.sreebell.roomdb.MotherChoiceDB::class.java,
                "mother-choice"
            )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build()
//        motherChoiceDB = MotherChoiceDB.getInstance(activity as Activity)


        viewModel = com.royalit.sreebell.roomdb.CartViewModel(activity as Activity)
        viewModel!!.cartData()

        adapter = Cart_Adapter(
            activity as Activity,
            cartItemsList as ArrayList<com.royalit.sreebell.roomdb.CartItems>,
            cartData,
            this@Cart_Fragment,
            cartItemListener
        )
        binding.cartRCID.adapter = adapter


        binding.addressTVID.text = "" + address
        binding.userNameTVID.text = "" + username
        /*************************************************************/
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationRequest = LocationRequest.Builder(5000)
            .setMinUpdateIntervalMillis(200000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMaxUpdates(2)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0 ?: return
                for (location in p0.locations) {
                    currentLat = location.latitude
                    currentLng = location.longitude
                    getAddress(currentLat, currentLng)
                    //val addressString = AppUtils.getAddressFromLatLong(mContext, latitude, longitude)

                }
            }
        }
        if(address.isEmpty())
        checkLocationSettings()


        /************************************************************/

        /********************************This is for MAP & Address & Location*/
        var placePickerResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    val vanillaAddress = VanillaPlacePicker.getPlaceResult(result.data)
                    Log.e("ADDRESSSSS---> ", vanillaAddress.toString())
                    //Constants.showToast(requireContext(), vanillaAddress.toString())
                    if (vanillaAddress != null) {
                        vanillaAddress.latitude?.let {
                            vanillaAddress.longitude?.let { it1 -> getAddress(it, it1) }
                        }
                    }



                }

            }



        binding.addressTVID.setOnClickListener {
            val intent = VanillaPlacePicker.Builder(requireContext())
                .setMapType(MapType.SATELLITE)
                .withLocation(currentLat, currentLng)
                .setPickerLanguage(PickerLanguage.ENGLISH)
                .enableShowMapAfterSearchResult(true)
                .build()
           // placePickerResultLauncher.launch(intent)
            stopLocationUpdates()
        }

        /*******************************************************************/

        getCart()
        binding.btnPlaceOrder.setOnClickListener {
            productIDStr = ""
            productQtyStr = ""
            product_quintals = ""
            priceIds = ""
            cartItemsList.forEach({

                if (productIDStr.isEmpty())
                    productIDStr = it.products_id.toString()
                else
                    productIDStr = productIDStr + "##" + it.products_id

                var qntity="1"

                if(it.kgs!=null&&it.kgs.isNotEmpty()&&it.pack_size!=null&&it.pack_size.isNotEmpty())
                {
                    qntity=   (it.kgs?.toInt()!! / it.pack_size?.toInt()!!).toString()
                }

                if(it.quintals!=null&&it.quintals.isNotEmpty())
                {
                    if (product_quintals.isEmpty())
                        product_quintals = it.quintals
                    else
                        product_quintals = product_quintals + "##" +  it.quintals
                }



                if (productQtyStr.isEmpty())
                    productQtyStr = qntity
                else
                    productQtyStr = productQtyStr + "##" + qntity


            })
            Log.e("Cart string","Cart string ${productQtyStr}")
            postOrder()
        }
        try {


            viewModel!!.getCartItems.observe(requireActivity()) { cartItems ->
                cartItemsList = cartItems
                if (cartItems.size > 0) {
                    binding.itemCountTVID.text = "" + cartItems.size.toString() + " Items"
                    getTotalPrice(cartItemsList)
                    binding.mainRLID.visibility = View.VISIBLE
                    binding.noDataTVID.visibility = View.GONE


                } else {
                    binding.mainRLID.visibility = View.GONE
                    binding.noDataTVID.visibility = View.VISIBLE
                }
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return root
    }

    override fun onQuantityChanged(
        cartItem: com.royalit.sreebell.roomdb.CartItems,
        newQuantity: Int
    ) {
        // Handle the quantity change here
        val index = cartItemsList.indexOfFirst { it.product_id == cartItem.product_id }
        if (index != -1) {
            cartItemsList[index].setCartQty(newQuantity.toString())
            getTotalPrice(cartItemsList) // Recalculate and update the total price
        }
    }
    override fun addToCartQuantity(product_id: String, kgs: String, quintals: String?, cart_id: String?) {

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
                                if(response.body()?.Status == true)
                                {
                                    Toast.makeText(requireContext(),"Cart Updated",Toast.LENGTH_SHORT).show()
                                }
                                getCart()
                            }


                        } catch (e: java.lang.NullPointerException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<DirectCartAddResponse>, t: Throwable) {
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
    override fun onDeleteCartItem(cartItem: com.royalit.sreebell.roomdb.CartItems) {

        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {
            val apiServices = APIClient.client.create(Api::class.java)
            var call = apiServices.removeFromCart(
                getString(R.string.api_key),
                customer_id = customerid,
                product_id = cartItem.product_id,
                cart_id = cartItem?.cartID.toString()!!
            )

            Log.e("cat", "" + category_id)
            Log.e("customer_id", "Cart Delete customer_id" + customerid)
            Log.e("product_id", "Cart Delete product_id" + cartItem.product_id)
            Log.e("product_id", "Cart Delete cart_id" + cartItem?.cartID)


            call?.enqueue(object : Callback<DeleteCartResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<DeleteCartResponse>,
                    response: Response<DeleteCartResponse>
                ) {
                    Log.e(ContentValues.TAG, response.toString())


                    getCart()
                    if (response.isSuccessful) {
                        try {


                        } catch (e: java.lang.NullPointerException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<DeleteCartResponse>, t: Throwable) {
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
    var sum = 0
    @SuppressLint("SetTextI18n")
    private fun getTotalPrice(cartItemsList: List<com.royalit.sreebell.roomdb.CartItems>) {
        try {
            sum=0

            for (i in cartItemsList) {
                try {
            var qnty=0;
                    var kgs=1
                    if(i.kgs!=null&&i.kgs.isNotEmpty())
                        kgs=  i.kgs.toInt()
                    val pkgSize=i.pack_size.toInt()
                    qnty=(kgs.div(pkgSize))

                    if (cust_category == 1) {
                        sum += ((i.distributor_price.toInt()) *  i.quintals.toFloat()).toInt()
                    } else if (cust_category == 2) {
                        sum += (i.general_trade_price.toInt() * i.quintals.toFloat()).toInt()
                    }

                   /* if (com.royalit.sreebell.utils.Utilities.customer_category == 2)
                        sum += cartItemsList[i].category_2_price.toDouble() * cartItemsList[i].getCartQty()
                            .toDouble()
                    else
                        sum += cartItemsList[i].getOffer_price()
                            .toDouble() * cartItemsList[i].getCartQty().toDouble()*/
                } catch (e: Exception) {

                }
            }

            binding.totalPriceTVID.text = "\u20b9 $sum"
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding
    }


    override fun onProductItemClick(itemsData: com.royalit.sreebell.roomdb.CartItems?) {

        ProductinDetails_Fragment().getData(itemsData?.product_id.toString(), true)!!

        val navController =
            Navigation.findNavController(
                context as Activity,
                R.id.nav_host_fragment_content_home_screen
            )
        navController.navigate(R.id.nav_product_details)

        val editor = sharedPreferences.edit()
        editor.putString("subcatid", itemsData?.product_id)
        editor.commit()

    }

    private fun addToCart(itemsData: com.royalit.sreebell.roomdb.CartItems?, cartQty: String?) {

        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {
            val apiServices = APIClient.client.create(Api::class.java)
            var call: Call<AddtoCartResponse>? = null
            call = apiServices.addToCart(
                getString(R.string.api_key),
                customer_id = com.royalit.sreebell.utils.Utilities.customerid,
                product_id = itemsData?.products_id.toString(),
                quantity = "1"
            )

            Log.e("cat", "Add to cart products_id" + itemsData?.products_id)
            Log.e(
                "cat",
                "Add to cart customerid " + com.royalit.sreebell.utils.Utilities.customerid
            )

            call.enqueue(object : Callback<AddtoCartResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<AddtoCartResponse>,
                    response: Response<AddtoCartResponse>
                ) {
                    Log.e(ContentValues.TAG, response.toString())

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

    private fun updateCart(itemsData: com.royalit.sreebell.roomdb.CartItems?, cartQty: String?) {

        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {
            val apiServices = APIClient.client.create(Api::class.java)
            var call = apiServices.updateCart(
                getString(R.string.api_key),
                customer_id = com.royalit.sreebell.utils.Utilities.customerid,
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

    override fun onAddToCartClicked(
        itemsData: com.royalit.sreebell.roomdb.CartItems?,
        cartQty: String?,
        isAdd: Int
    ) {
        if (isAdd == 0) {
            addToCart(itemsData, cartQty)
        } else
            updateCart(itemsData, cartQty)
        /*val items = CartItems(
            0,
            itemsData?.cartID!!,
            customerid,
            Utilities.getDeviceID(activity),
            AppConstants.PRODUCTS_CART,
            itemsData!!.itemName,
            itemsData.itemImage,
            cartQty,
            java.lang.String.valueOf(itemsData.sales_price),
            java.lang.String.valueOf(itemsData.offer_price),
            java.lang.String.valueOf(itemsData.product_id),
            itemsData.product_id.toString(), itemsData.stock,
            itemsData.max_order_quantity,
            itemsData.category_2_price,
            itemsData.product_name,
            itemsData.product_title
        )
        val viewModel = CartViewModel(activity)
        viewModel.insert(items)
        Toast.makeText(activity, "Item added to cart successfully", Toast.LENGTH_LONG).show()*/
    }

    private fun postOrder() {

        try {


            if (NetWorkConection.isNEtworkConnected(context as Activity)) {

                //Set the Adapter to the RecyclerView//

                val addnotes = binding.cartnotes.text.toString()

                var apiServices = APIClient.client.create(Api::class.java)

                Log.d("productIDStr", "productIDStr $productIDStr")
                Log.d("productIDStr", "productQtyStr $productQtyStr")
                val orderamount=sum.toString()
                var cust_category_str=""
                if(cust_category==2)
                    cust_category_str="general trade"
                    else if(cust_category==1)
                    cust_category_str="distributor"

                val call =
                    apiServices.getplaceordersave(
                        getString(R.string.api_key),
                        customerid, productIDStr, productQtyStr, addnotes,
                        username, selectedLat, selectedLng, fullAddress,orderamount,cust_category_str,dist_id,productQtyStr,product_quintals
                    )
                binding.progressbar.visibility = View.VISIBLE

                call.enqueue(object : Callback<Placeorder_ListResponse> {
                    override fun onResponse(
                        call: Call<Placeorder_ListResponse>,
                        response: Response<Placeorder_ListResponse>
                    ) {

                        Log.e(ContentValues.TAG, response.toString())
                        Log.e("Order Response", response.body()?.response?.amount.toString() + "")
                        Log.e("Order Response", response.body()?.response?.order_id.toString())
                        binding.progressbar.visibility = View.GONE

                        try {
                            if (response.isSuccessful) {

                                if (response.body()?.customer_status != "active") {
                                    Toast.makeText(
                                        requireActivity(),
                                        "User is Inactive, Order not placed",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    return
                                }
                                //Set the Adapter to the RecyclerView//

                                val navController =
                                    Navigation.findNavController(
                                        context as Activity,
                                        R.id.nav_host_fragment_content_home_screen
                                    )

                                val bundle = Bundle()
                                bundle.putSerializable(
                                    "place_order_details",
                                   response.body()?.response
                                )
                                navController.navigate(R.id.navigation_sucess, bundle)
                                val viewModel =
                                    com.royalit.sreebell.roomdb.CartViewModel(activity as Activity)
                                viewModel.delete(cartItemsList[0], true, true)

                                val prefs = sharedPreferences.edit()
                                prefs.putString(
                                    "Orderid",
                                    response.body()?.response?.order_id.toString()
                                )
                                prefs.putString(
                                    "orderamount",
                                    response.body()?.response?.amount.toString()
                                )

                                prefs.commit()

                                Toast.makeText(
                                    context,
                                    "Order Placed Sucessfully",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {


                            }
                        } catch (e: NullPointerException) {
                            e.printStackTrace()
                        } catch (e: TypeCastException) {
                            e.printStackTrace()
                        }


                    }


                    override fun onFailure(call: Call<Placeorder_ListResponse>, t: Throwable) {
                        Log.e(ContentValues.TAG, t.toString())
                        Toast.makeText(
                            context,
                            "Something went wrong",
                            Toast.LENGTH_LONG
                        ).show()

                        binding.progressbar.visibility = View.GONE

                    }
                })


            } else {
                Toast.makeText(context, "Please Check your internet", Toast.LENGTH_LONG).show()

            }
        } catch (e: UninitializedPropertyAccessException) {
            e.printStackTrace()
        }
    }

    private fun getCart() {

        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {
            try {
                (requireActivity() as HomeScreen).updateCartCount()
            } catch (e: Exception) {

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

                    binding.cartRCID.requestFocus()
                    if (response.isSuccessful) {
                        try {


                            cartItemsList = response.body()?.cartList!!

                            if (cartItemsList.size > 0) {
                                binding.itemCountTVID.text =
                                    "" + cartItemsList.size.toString() + " Items"
                                getTotalPrice(cartItemsList)
                                binding.mainRLID.visibility = View.VISIBLE
                                binding.noDataTVID.visibility = View.GONE

                                try {

                                    val adapter = Cart_Adapter(
                                        activity as Activity,
                                        cartItemsList as ArrayList<com.royalit.sreebell.roomdb.CartItems>,
                                        cartData,
                                        productItemClick,
                                        cartItemListener
                                    )
                                    binding.cartRCID.adapter = adapter
                                    productIDStr = ""
                                    productQtyStr = ""
                                    cartItemsList.forEach({
                                        if (productIDStr.isEmpty())
                                            productIDStr = it.cartID.toString()
                                        else
                                            productIDStr = productIDStr + "," + it.cartID

                                        var qntity="1"
                                        if(it.cartQty!=null)
                                            qntity=it.cartQty

                                        if (productQtyStr.isEmpty())
                                            productQtyStr = qntity
                                        else
                                            productQtyStr = productQtyStr + "," + qntity
                                    })
                                } catch (e: java.lang.NullPointerException) {
                                    e.printStackTrace()
                                }
                            } else {
                                binding.mainRLID.visibility = View.GONE
                                binding.noDataTVID.visibility = View.VISIBLE
                            }

                        } catch (e: java.lang.NullPointerException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<CartListResponse>, t: Throwable) {
                    Log.e(ContentValues.TAG, t.toString())
                     binding.subproductprogress.visibility = View.GONE
                    binding.cartRCID.requestFocus()
                }
            })
        } else {
            Toast.makeText(
                activity as Activity,
                "Please Check your internet",
                Toast.LENGTH_LONG
            ).show()
            binding.subproductprogress.visibility = View.GONE
        }


    }

    fun acceptsSerializable(s: Serializable) { }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            fusedLocationClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            )
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.isNotEmpty()) {
                startLocationUpdates()
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
    }

    private fun getAddress(latitude: Double, longitude: Double) {
        locationUtils.getAddressFromLocation(
            latitude,
            longitude,
            { address ->
                // Adres alımı başarılı

                try {
                    if (requireActivity() != null)
                        requireActivity().runOnUiThread {
                            _binding?.addressTVID?.text = address
                            fullAddress = address
                            selectedLat = latitude.toString()
                            selectedLng = longitude.toString()

                            val editor = sharedPreferences.edit()

                            editor.putString(
                                "address",
                                address
                            )
                            editor.commit()
                            //Toast.makeText(this, "Adres: $address", Toast.LENGTH_SHORT).show()
                        }

                }catch (e:Exception)
                {

                }
            },
            { errorMessage ->
                // Adres alımı başarısız
            }
        )
    }

    private fun checkLocationSettings() {
        val locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest).build()

        val settingClient = LocationServices.getSettingsClient(requireActivity())

        val task: Task<LocationSettingsResponse> =
            settingClient.checkLocationSettings(locationSettingsRequest)

        task.addOnSuccessListener {
            startLocationUpdates()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(
                        requireActivity(),
                        1001
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    sendEx.printStackTrace()
                }
            }
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

}
