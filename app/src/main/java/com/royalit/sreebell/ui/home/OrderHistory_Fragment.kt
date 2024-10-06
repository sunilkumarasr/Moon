package com.royalit.sreebell.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.royalit.sreebell.adapter.OrdersHisList_Adapter
import com.erepairs.app.api.Api
import com.royalit.sreebell.R
import com.royalit.sreebell.api.APIClient
import com.royalit.sreebell.databinding.OrderhistoryScreenBinding
import com.royalit.sreebell.models.OrderHis_ListResponse
import com.royalit.sreebell.models.OrderHis_Response
import com.royalit.sreebell.utils.NetWorkConection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class OrderHistory_Fragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: OrderhistoryScreenBinding? = null
    lateinit var orderhisadapter: OrdersHisList_Adapter
    lateinit var customerid: String
    lateinit var sharedPreferences: SharedPreferences

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


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

        _binding = OrderhistoryScreenBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedPreferences =
            requireContext().getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )

        customerid = sharedPreferences.getString("userid", "").toString()

        Log.e("customid", "" + customerid)

        getordershisList()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swiperefresh.setOnRefreshListener {
            getordershisList()
        }
    }

    private fun getordershisList() {

        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)

            val call =
                apiServices.getordershisList(getString(R.string.api_key), customerid)

            binding.orderhisprogress.visibility = View.VISIBLE
            call.enqueue(object : Callback<OrderHis_ListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<OrderHis_ListResponse>,
                    response: Response<OrderHis_ListResponse>
                ) {
                    binding.swiperefresh.isRefreshing=false
                    Log.e(ContentValues.TAG, response.toString())
                    binding.orderhisprogress.visibility = View.GONE

                    if (response.isSuccessful) {

                        try {


                            val listOfcategories = response.body()?.response

                            //Set the Adapter to the RecyclerView//


                            val selectedserviceslist =
                                response.body()?.response!!


                            orderhisadapter =
                                OrdersHisList_Adapter(
                                    activity as Activity,
                                    selectedserviceslist as ArrayList<OrderHis_Response>
                                )
                            binding.orderhisRecycler.adapter =
                                orderhisadapter

                            orderhisadapter.notifyDataSetChanged()


                        } catch (e: java.lang.NullPointerException) {
                            e.printStackTrace()
                        }

                    }


                }

                override fun onFailure(call: Call<OrderHis_ListResponse>, t: Throwable) {
                    Log.e(ContentValues.TAG, t.toString())
                    binding.orderhisprogress.visibility = View.GONE
                    binding.swiperefresh.isRefreshing=false
                }

            })


        } else {
            binding.swiperefresh.isRefreshing=false
            Toast.makeText(
                activity as Activity,
                "Please Check your internet",
                Toast.LENGTH_LONG
            ).show()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}