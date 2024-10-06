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
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.erepairs.app.api.Api
import com.erepairs.app.models.AppMaintananceResponseModel
import com.royalit.sreebell.AppMaintananceActivity
import com.royalit.sreebell.HomeScreen
import com.royalit.sreebell.R
import com.royalit.sreebell.adapter.ViewAllProductList_Adapter
import com.royalit.sreebell.api.APIClient
import com.royalit.sreebell.databinding.ProductsScreenBinding
import com.royalit.sreebell.models.AddtoCartResponse
import com.royalit.sreebell.models.CartListResponse
import com.royalit.sreebell.models.DirectCartAddResponse
import com.royalit.sreebell.models.Product_ListResponse
import com.royalit.sreebell.models.Product_Response
import com.royalit.sreebell.models.UpdateCartResponse
import com.royalit.sreebell.roomdb.CartItems
import com.royalit.sreebell.roomdb.MotherChoiceDB
import com.royalit.sreebell.utils.NetWorkConection
import com.royalit.sreebell.utils.Utilities
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class ViewAllProducts_Fragment : Fragment(), ViewAllProductList_Adapter.ProductItemClick {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: ProductsScreenBinding? = null
    lateinit var subproduct_adapter: ViewAllProductList_Adapter
    var cartData = ArrayList<com.royalit.sreebell.roomdb.CartItems>()
    lateinit var motherChoiceDB: com.royalit.sreebell.roomdb.MotherChoiceDB

    // private lateinit var selectedserviceslist: ArrayList<Product_Response>
    private var selectedserviceslist: List<Product_Response> = ArrayList()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var sharedPreferences: SharedPreferences
    lateinit var category_id: String
    private var isSortAscending = true
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
        val root: View = binding.root
        sharedPreferences =
            requireContext().getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )

        getCart()
        subproduct_adapter = ViewAllProductList_Adapter(
            activity as Activity,
            selectedserviceslist as ArrayList<Product_Response>,
            cartData,
            this@ViewAllProducts_Fragment,
            sharedPreferences.getInt("customer_category", 0)!!,
            sharedPreferences.getString("district_id", "")!!
        )
       // binding.subproductlist.setNestedScrollingEnabled(false);
       // binding.subproductlist.setHasFixedSize(false);

        binding.subproductlist.adapter = subproduct_adapter
        val edittextSearch = binding.edittextSearch
        val layoutManager = LinearLayoutManager(
            activity as Activity,

        )
        binding.subproductlist.layoutManager = layoutManager

//        edittextSearch.setOnClickListener {
//            val intent = Intent(requireContext(), Products_price_search::class.java)
//            startActivity(intent)
//            // Handle the click event (e.g., open a new page, start filtering, etc.)
//        }

        binding.edittextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()

                if (query.length > 0) {
                    // Fetch products xbased on the offer price
                    fetchProductsByOfferPrice(query)
                } else {
                    subproduct_adapter.resetData(selectedserviceslist)
                }
            }
    })
        // Set up a query text listener for the SearchView

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
            Room.databaseBuilder(activity as Activity, com.royalit.sreebell.roomdb.MotherChoiceDB::class.java, "mother-choice")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build()
        motherChoiceDB = com.royalit.sreebell.roomdb.MotherChoiceDB.getInstance(activity as Activity)



        /*val cartVM = CartViewModel(activity, false)
        cartVM.cartData()
        cartVM.getCartItems.observe(requireActivity()) { cartItems -> cartData = cartItems }
       */
        getviewallProductsList()


        /*val viewModel = CartViewModel(activity as Activity)
        viewModel.cartCount()
        viewModel.cartCount.observe(requireActivity()) { cartItems ->
            if (cartItems.size > 0) {
                binding.cartcount.visibility = View.VISIBLE
                binding.cvOneLogin.visibility = View.VISIBLE
                binding.carttext.visibility = View.VISIBLE
                binding.rlOneLogin.visibility = View.VISIBLE
                binding.cartcount.text = "" + cartItems.size
                binding.carttext.text = "Item in your basket"
            } else {
                binding.cartcount.visibility = View.GONE
                binding.cvOneLogin.visibility = View.GONE
                binding.carttext.visibility = View.GONE
                binding.rlOneLogin.visibility = View.GONE
            }

        }*/

        return root
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

                                getviewallProductsList()
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
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.popup_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort_low_to_high -> {
                // Sort products in ascending order (Low to High)
                isSortAscending = true
                sortProductsByOfferPrice()
                return true
            }
            R.id.sort_high_to_low -> {
                // Sort products in ascending order (Low to High)
                isSortAscending = false
                sortProductsByOfferPrice()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun sortProductsByOfferPrice() {
        subproduct_adapter.sortByOfferPrice(ascending = isSortAscending)
        subproduct_adapter.notifyDataSetChanged()

    }


    private fun fetchProductsByOfferPrice(query: String) {
        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {
            var apiServices = APIClient.client.create(Api::class.java)
            var call: Call<Product_ListResponse>? =null
            try {
                val d = query.toInt()
                 call =
                    apiServices.getProductsByOfferPrice(getString(R.string.api_key),query)
                Log.e("cat", "" + category_id)

            }catch (e:Exception)
            {
                call =
                    apiServices.getsearchProducts(getString(R.string.api_key),query)
                Log.e("cat", "" + category_id)

            }
              binding.subproductprogress.visibility = View.VISIBLE

            call?.enqueue(object : Callback<Product_ListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<Product_ListResponse>,
                    response: Response<Product_ListResponse>
                ) {
                    Log.e(ContentValues.TAG, response.toString())
                    binding.subproductprogress.visibility = View.GONE

                    if (response.isSuccessful) {
                        try {
                            response.body()?.toString()?.let { Log.d("API Response", it) }
                             selectedserviceslist = response.body()?.response!!

                            if(selectedserviceslist==null)
                                selectedserviceslist=ArrayList()


                            val filterList: MutableList<Product_Response> = ArrayList()


                           /* selectedserviceslist.forEach { productItem ->

                                productItem.prices.forEach { price ->
                                    var canWeAddRecord: Boolean = false
                                    if (price.district_id == sharedPreferences.getString(
                                            "district_id",
                                            ""
                                        )!!
                                    ) {
                                        canWeAddRecord =true
                                    }
                                    if(canWeAddRecord)
                                        filterList.add(productItem)

                                }


                            }*/
                            Log.d("API Response cartData ", selectedserviceslist.toString())
                            Log.d("API Response selectedserviceslist ", cartData.toString())
                            subproduct_adapter.addProductsLIst(selectedserviceslist as ArrayList<Product_Response>,cartData)

                           /* subproduct_adapter = ViewAllProductList_Adapter(
                                activity as Activity,
                                selectedserviceslist as ArrayList<Product_Response>,
                                cartData,
                                this@ViewAllProducts_Fragment
                            )

                            binding.subproductlist.adapter = subproduct_adapter
                            subproduct_adapter.notifyDataSetChanged()

                            val layoutManager = GridLayoutManager(
                                activity as Activity,
                                2,
                                RecyclerView.VERTICAL,
                                false
                            )
                            binding.subproductlist.layoutManager = layoutManager
                            binding.subproductlist.setItemViewCacheSize(selectedserviceslist.size)
*/
                        } catch (e: java.lang.NullPointerException) {
                            e.printStackTrace()
                        }
                    }
                }


                override fun onFailure(call: Call<Product_ListResponse>, t: Throwable) {
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

    private fun getviewallProductsList() {

        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)

            val call =
                apiServices.getproductsList(getString(R.string.api_key), customer_id =Utilities.customerid )

            binding.subproductprogress.visibility = View.VISIBLE
            call.enqueue(object : Callback<Product_ListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<Product_ListResponse>,
                    response: Response<Product_ListResponse>
                ) {

                    Log.e(ContentValues.TAG, response.toString())
                    binding.subproductprogress.visibility = View.GONE

                    if (response.isSuccessful) {

                        try {

                           selectedserviceslist = response.body()?.response ?: emptyList()
                          //  val listOfcategories = response.body()?.response

                            //Set the Adapter to the RecyclerView//



                          /*  subproduct_adapter =
                                ViewAllProductList_Adapter(
                                    activity as Activity,

                                    selectedserviceslist as ArrayList<Product_Response>,
                                    cartData, this@ViewAllProducts_Fragment
                                )*/

                            //val filterList: MutableList<Product_Response> = ArrayList()


                            /*selectedserviceslist.forEach { productItem ->
                                if (productItem.district_id == sharedPreferences.getString(
                                        "district_id",
                                        ""
                                    )!!
                                ) {
                                    filterList.add(productItem)
                                }

                            }*/

                            subproduct_adapter.addProductsLIst(selectedserviceslist as ArrayList<Product_Response>,cartData)

                           /* binding.subproductlist.adapter =
                                subproduct_adapter*/

                           /* subproduct_adapter.notifyDataSetChanged()

                            subproduct_adapter.languageList.addAll(selectedserviceslist)

                            binding.subproductlist.adapter = subproduct_adapter

*/
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

    }
    private fun addToCart(itemsData: Product_Response?, cartQty:String?) {

        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {
            val apiServices = APIClient.client.create(Api::class.java)
            var call:Call<AddtoCartResponse>?=null
            call = apiServices.addToCart(getString(R.string.api_key), customer_id = com.royalit.sreebell.utils.Utilities.customerid, product_id = itemsData?.products_id.toString(), quantity = "1" )

            Log.e("cat", "Add to cart products_id" + itemsData?.products_id)
            Log.e("cat", "Add to cart customerid " + com.royalit.sreebell.utils.Utilities.customerid)
            binding.subproductprogress.visibility = View.VISIBLE

            call.enqueue(object : Callback<AddtoCartResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(call: Call<AddtoCartResponse>, response: Response<AddtoCartResponse>) {
                    Log.e(ContentValues.TAG, response.toString())
                    binding.subproductprogress.visibility = View.GONE

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
    private fun updateCart(itemsData: Product_Response?, cartQty:String?) {

        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {
            val apiServices = APIClient.client.create(Api::class.java)
            var call=  apiServices.updateCart(getString(R.string.api_key),customer_id = com.royalit.sreebell.utils.Utilities.customerid, quantity = cartQty!!, product_id = itemsData?.products_id.toString()!! )

            Log.e("cat", "quantity cartQty" + cartQty+" - "+itemsData?.products_id)
            binding.subproductprogress.visibility = View.VISIBLE

            call?.enqueue(object : Callback<UpdateCartResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(call: Call<UpdateCartResponse>, response: Response<UpdateCartResponse>) {
                    Log.e(ContentValues.TAG, response.toString())
                    binding.subproductprogress.visibility = View.GONE
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
            try{
                ( requireActivity() as HomeScreen).updateCartCount()
            }catch (e: java.lang.Exception)
            {

            }
            val apiServices = APIClient.client.create(Api::class.java)
            var call=  apiServices.getCartList(getString(R.string.api_key),customer_id = com.royalit.sreebell.utils.Utilities.customerid)

            Log.e("cat", " customerid " + com.royalit.sreebell.utils.Utilities.customerid)
            binding.subproductprogress.visibility = View.VISIBLE

            call?.enqueue(object : Callback<CartListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(call: Call<CartListResponse>, response: Response<CartListResponse>) {
                    Log.e(ContentValues.TAG, response.toString())
                    binding.subproductprogress.visibility = View.GONE

                    if (response.isSuccessful) {
                        try {



                            if (response.body()?.cartList?.size!! > 0) {
                                binding.cartcount.visibility = View.VISIBLE
                                binding.cvOneLogin.visibility = View.VISIBLE
                                binding.carttext.visibility = View.VISIBLE
                                binding.rlOneLogin.visibility = View.VISIBLE
                                binding.cartcount.text = "" + response.body()?.cartList?.size!!
                                binding.carttext.text = "Item in your basket"
                            } else {
                                binding.cartcount.visibility = View.GONE
                                binding.cvOneLogin.visibility = View.GONE
                                binding.carttext.visibility = View.GONE
                                binding.rlOneLogin.visibility = View.GONE
                            }
                            cartData=response?.body()?.cartList!! as ArrayList<com.royalit.sreebell.roomdb.CartItems>

                            subproduct_adapter.addProductsLIst(selectedserviceslist as ArrayList<Product_Response>,cartData)




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
    override fun onAddToCartClicked(itemsData: Product_Response, cartQty: String?,isAdd:Int) {
        if(isAdd==0) {
            addToCart(itemsData, cartQty)
        }
        else
            updateCart(itemsData, cartQty)
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



