package com.royalit.sreebell.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erepairs.app.api.Api
import com.royalit.sreebell.R
import com.royalit.sreebell.adapter.CategoryList_Adapter
import com.royalit.sreebell.adapter.CategoryNew_Adapter
import com.royalit.sreebell.api.APIClient
import com.royalit.sreebell.databinding.FragmentCategoryBinding
import com.royalit.sreebell.models.Category_ListResponse
import com.royalit.sreebell.models.Category_Response
import com.royalit.sreebell.utils.NetWorkConection
import com.royalit.sreebell.utils.Utilities.customerid
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


class CategoryFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    lateinit var sharedPreferences: SharedPreferences

    lateinit var categoryadapter: CategoryNew_Adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(HomeViewModel::class.java)

        // Inflate the layout for this fragment
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sharedPreferences =
            requireContext().getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )

        customerid = sharedPreferences.getString("userid", "")!!

        getCategories()

        return root

    }


    private fun getCategories() {
        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {
            val apiServices = APIClient.client.create(Api::class.java)
            val call =
                apiServices.getcategoruesList(getString(R.string.api_key))
            binding.homeprogress.visibility = View.VISIBLE
            call.enqueue(object : Callback<Category_ListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<Category_ListResponse>,
                    response: Response<Category_ListResponse>
                ) {

                    Log.e(ContentValues.TAG, response.toString())
                    binding.homeprogress.visibility = View.GONE

                    if (response.isSuccessful) {

                        try {
                            val listOfcategories = response.body()?.response

                            //Set the Adapter to the RecyclerView//

                            val selectedserviceslist =
                                response.body()?.response!!

                            activity?.let {
                                categoryadapter =
                                    CategoryNew_Adapter(
                                        activity as Activity,
                                        selectedserviceslist as ArrayList<Category_Response>
                                    )

                                binding.categoryList.adapter =
                                    categoryadapter


                                binding.categoryList.setItemViewCacheSize(selectedserviceslist.size)
                                binding.categoryList.layoutManager = GridLayoutManager(activity, 2)
                                binding.categoryList.setHasFixedSize(true)
                                categoryadapter.notifyDataSetChanged()

                            }
                        } catch (e: java.lang.NullPointerException) {
                            e.printStackTrace()
                        }

                    }

                }

                override fun onFailure(call: Call<Category_ListResponse>, t: Throwable) {
                    Log.e(ContentValues.TAG, t.toString())
                    binding.homeprogress.visibility = View.GONE
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