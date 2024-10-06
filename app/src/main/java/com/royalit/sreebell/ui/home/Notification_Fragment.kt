package com.royalit.sreebell.ui.home

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erepairs.app.adapter.NotificationAdapter
import com.erepairs.app.api.Api
import com.royalit.sreebell.R
import com.royalit.sreebell.adapter.SubProductList_Adapter
import com.royalit.sreebell.api.APIClient
import com.royalit.sreebell.databinding.FragmentNavigationNotifyBinding
import com.royalit.sreebell.models.Category_subResponse
import com.royalit.sreebell.models.NotificationListResponse
import com.royalit.sreebell.models.NotificationModel
import com.royalit.sreebell.utils.NetWorkConection
import retrofit2.Call
import retrofit2.Response

class Notification_Fragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentNavigationNotifyBinding? = null
    private val binding get() = _binding!!
    lateinit var root: View
    lateinit var notificationAdapter:NotificationAdapter
    private var notificationList: List<NotificationModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        homeViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(HomeViewModel::class.java)

        _binding = FragmentNavigationNotifyBinding.inflate(inflater, container, false)
        root = binding.root
       return  root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)

            val call =
                apiServices.getNotificationList(getString(R.string.api_key))

            call.enqueue(object:  retrofit2.Callback<NotificationListResponse> {
                override fun onResponse(
                    call: Call<NotificationListResponse>,
                    response: Response<NotificationListResponse>
                ) {

                    if (response.isSuccessful) {
                        binding.carviewNoItem.visibility=View.GONE

                        try {

                            //Set the Adapter to the RecyclerView//

                            notificationList =
                                response.body()?.response!!
                            notificationAdapter= NotificationAdapter(requireActivity(),notificationList as ArrayList<NotificationModel>)

                            binding.recycleNotifi.adapter =
                                notificationAdapter

                            notificationAdapter.notifyDataSetChanged()
                            val layoutManager = LinearLayoutManager(activity as Activity, RecyclerView.VERTICAL, false)

                            binding.recycleNotifi.layoutManager = layoutManager

                            binding.recycleNotifi.setItemViewCacheSize(notificationList.size)
                            if(notificationList.size>0)
                                binding.carviewNoItem.visibility=View.GONE
                            else
                                binding.carviewNoItem.visibility=View.VISIBLE

                        } catch (e: java.lang.NullPointerException) {
                            e.printStackTrace()
                        }

                    }else
                    {
                        binding.carviewNoItem.visibility=View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<NotificationListResponse>, t: Throwable) {


                }

            }
            )

        }else {

            Toast.makeText(
                activity as Activity,
                "Please Check your internet",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}

