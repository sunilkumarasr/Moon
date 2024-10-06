package com.royalit.sreebell.ui

//import android.annotation.SuppressLint
//import android.app.Activity
//import android.content.ContentValues
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.widget.Toast
//import androidx.appcompat.widget.SearchView
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.erepairs.app.api.Api
//import com.royalit.sreebell.R
//import com.royalit.sreebell.adapter.Product_price_Adapter
//import com.royalit.sreebell.adapter.SubProductList_Adapter
//import com.royalit.sreebell.api.APIClient
//import com.royalit.sreebell.databinding.ActivityProductsPriceSearchBinding
//import com.royalit.sreebell.models.Category_subListResponse
//import com.royalit.sreebell.models.Category_subResponse
//import com.royalit.sreebell.models.Product_priceresponse
//import com.royalit.sreebell.models.Productprice_listresponse
//import com.royalit.sreebell.utils.NetWorkConection
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response

//class Products_price_search : AppCompatActivity() {
//
//    private var binding: ActivityProductsPriceSearchBinding? = null
//
//    private var products: List<Product_priceresponse> = emptyList()
//    private var matchedProducts: List<Product_priceresponse> = emptyList()
//    private lateinit var productAdapter: Product_price_Adapter
//    val value = "your_value_here"
//    val activity = this
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityProductsPriceSearchBinding.inflate(layoutInflater)
//        setContentView(binding?.root)
//
//        productAdapter = Product_price_Adapter(this, ArrayList())
//
//        binding?.recyclerView?.layoutManager = LinearLayoutManager(this)
//        binding?.recyclerView?.adapter = productAdapter
//
//        fetchData()
//        setupSearchView()
//    }
//
//    private fun fetchData() {
//        val priceQuery = "your_price_query_here"
//
//        // Replace this with your actual API call.
//        // This is just a placeholder to demonstrate the structure.
//        fetchProductsByOfferPrice(priceQuery) { productsResponse, error ->
//            if (error == null && productsResponse != null) {
//                products = productsResponse.Response
//                updateRecyclerView(productsResponse.Response)
//            } else {
//                showError("Failed to fetch products: ${error?.message}")
//            }
//        }
//    }
//
//    private fun fetchProductsByOfferPrice(priceQuery: String, function: (Productprice_listresponse?, Throwable?) -> Unit) {
//        if (NetWorkConection.isNEtworkConnected(activity)) {
//            val apiServices = APIClient.client.create(Api::class.java)
//            val call = apiServices.getProductsByOfferPrice(getString(R.string.api_key), value)
//
//            Log.e("cat", "" + value)
//            binding?.subproductprogress?.visibility = View.VISIBLE
//
//            call.enqueue(object : Callback<Productprice_listresponse> {
//                @SuppressLint("WrongConstant")
//                override fun onResponse(call: Call<Productprice_listresponse>, response: Response<Productprice_listresponse>) {
//                    Log.e(ContentValues.TAG, response.toString())
//                    binding?.subproductprogress?.visibility = View.GONE
//
//                    if (response.isSuccessful) {
//                        try {
//                            response.body()?.toString()?.let { Log.d("API Response", it) }
//                            val selectedserviceslist = response.body()?.Response!!
//
//                            // If you need filtered products, include that logic here.
//                            val sortedList = selectedserviceslist.sortedBy { it.offer_price.toDouble() }
//
//                            productAdapter = Product_price_Adapter(activity, selectedserviceslist as ArrayList<Product_priceresponse>)
//                            binding?.recyclerView?.adapter = productAdapter
//                            productAdapter.notifyDataSetChanged()
//
//                            val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
//                            binding?.recyclerView?.layoutManager = layoutManager
//                            binding?.recyclerView?.setItemViewCacheSize(sortedList.size)
//                        } catch (e: java.lang.NullPointerException) {
//                            e.printStackTrace()
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<Productprice_listresponse>, t: Throwable) {
//                    Log.e(ContentValues.TAG, t.toString())
//                    binding?.subproductprogress?.visibility = View.GONE
//                }
//            })
//        } else {
//            Toast.makeText(activity, "Please Check your internet", Toast.LENGTH_LONG).show()
//        }
//    }
//
//
//    private fun setupSearchView() {
//        binding?.edittextSearch?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                search(query)
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                search(newText)
//                return true
//            }
//        })
//    }
//
//    private fun search(text: String?) {
//        matchedProducts = products.filter { product ->
//            val nameMatch = product.product_name.contains(text.orEmpty(), ignoreCase = true)
//            val priceMatch = product.offer_price.contains(text.orEmpty(), ignoreCase = true)
//            nameMatch || priceMatch
//        }
//
//        updateRecyclerView(matchedProducts)
//    }
//
//    private fun updateRecyclerView(products: List<Product_priceresponse>) {
//        productAdapter.updateData(products)
//    }
//
//    private fun showError(message: String) {
//        // Handle and display error messages
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        binding = null
//    }
//}
//
