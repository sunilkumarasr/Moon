package com.royalit.sreebell.ui.home

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.royalit.sreebell.R
import com.royalit.sreebell.databinding.SucessScreenBinding
import com.royalit.sreebell.models.PlaceOrder_Response


class Sucess_Fragment : Fragment() {
    lateinit var usersubcategory_id_: String

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: SucessScreenBinding? = null

    lateinit var root: View

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var sharedPreferences: SharedPreferences
    lateinit var category_id: String
    lateinit var customerid: String
    lateinit var Orderid: String
    lateinit var orderamount: String
    lateinit var placeOrder_Response: PlaceOrder_Response

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

        _binding = SucessScreenBinding.inflate(inflater, container, false)
        root = binding.root
        sharedPreferences =
            requireContext().getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )

        category_id = sharedPreferences.getString("categoryid", "")!!
        customerid = sharedPreferences.getString("userid", "").toString()
        Orderid = sharedPreferences.getString("Orderid", "").toString()
        orderamount = sharedPreferences.getString("orderamount", "").toString()

        try {
            placeOrder_Response =
                arguments?.getSerializable("place_order_details") as PlaceOrder_Response
        }catch (exception: Exception){
            Log.e("EXCEPTION", exception.message.toString())
        }


        Log.e("cust", customerid)

        binding.ordernumText.text = "Order Id : "+ Orderid
        binding.amountText.text = "Rs. " + orderamount
       // binding.orderdate.text = "" + orderamount

        binding.okbtn.setOnClickListener {

            val navController =
                Navigation.findNavController(
                    context as Activity,
                    R.id.nav_host_fragment_content_home_screen
                )
            navController.navigate(R.id.nav_home)
        }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding
    }


}