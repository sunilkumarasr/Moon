package com.royalit.sreebell.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.erepairs.app.api.Api
import com.royalit.sreebell.R
import com.royalit.sreebell.SignIn_Screen
import com.royalit.sreebell.Signup_Screen
import com.royalit.sreebell.api.APIClient
import com.royalit.sreebell.databinding.MyprofileScreenBinding
import com.royalit.sreebell.models.LoginList_Response
import com.royalit.sreebell.models.SignupList_Response
import com.royalit.sreebell.utils.LocationUtils
import com.royalit.sreebell.utils.NetWorkConection
import com.vanillaplacepicker.presentation.builder.VanillaPlacePicker
import com.vanillaplacepicker.utils.MapType
import com.vanillaplacepicker.utils.PickerLanguage
import com.vanillaplacepicker.utils.PickerType
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


class MyProfile_Fragment : Fragment() {
    var emailPatter= Patterns.EMAIL_ADDRESS
    var pattern =
        "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$"

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: MyprofileScreenBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var customerid: String
    lateinit var sharedPreferences: SharedPreferences
    lateinit var root: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = MyprofileScreenBinding.inflate(inflater, container, false)
        root = binding.root
        sharedPreferences =
            requireContext().getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )

        customerid = sharedPreferences.getString("userid", "").toString()

        Log.e("customid", "" + customerid)
        getProfile()

        binding.btnUpdate.setOnClickListener {
            updateProfile()
        }
        return root
    }

    private fun getProfile() {
        try {
            if (NetWorkConection.isNEtworkConnected(activity as Activity)) {
                val apiServices = APIClient.client.create(Api::class.java)
                val call = apiServices.getprofile(getString(R.string.api_key), customerid)

                binding.progressProfile.visibility = View.VISIBLE

                call.enqueue(object : Callback<LoginList_Response> {
                    @SuppressLint("WrongConstant")
                    override fun onResponse(
                        call: Call<LoginList_Response>,
                        response: Response<LoginList_Response>
                    ) {
                        Log.e(ContentValues.TAG, response.toString())
                        binding.progressProfile.visibility = View.GONE

                        if (response.isSuccessful) {
                            try {
                                val listOfcategories = response.body()?.response

                                // Ensure binding is not null before accessing its properties
                                binding?.apply {
                                    nameProfile.setText("" + listOfcategories?.full_name)
                                    emailProfile.setText( "" + listOfcategories?.email_id)
                                    mobileProfile.setText( "" + listOfcategories?.mobile_number)
                                    addressProfile.setText("" + listOfcategories?.address)
                                    stateProfile.setText( "" + listOfcategories?.state)
                                    cityProfile.setText("" + listOfcategories?.city)
                                }
                            } catch (e: java.lang.NullPointerException) {
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun onFailure(call: Call<LoginList_Response>, t: Throwable) {
                        Log.e(ContentValues.TAG, t.toString())
                        binding?.progressProfile?.visibility = View.GONE
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
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateProfile() {

      val  name_strng = binding.nameProfile.text.toString()
        val  phone_strng = binding.mobileProfile.text.toString()
        val   email_strng = binding.emailProfile.text.toString()

        val address = binding.addressProfile.text.toString()
        val  username_shopname = binding.addressProfile.text.toString()
        val  state_name = binding.stateProfile.text.toString()
        val  city_name = binding.cityProfile.text.toString()

       if (name_strng.isEmpty()) {
            binding.nameProfile.error = "Please Enter Name"
        }else if (username_shopname.isEmpty()) {
            binding.addressProfile.error = "Please Enter Shop/Business Name"
        } else if (phone_strng.isEmpty()) {
            binding.mobileProfile.error = "Please Enter Phone Number"
        } else if (email_strng.isEmpty()) {
            binding.emailProfile.error = "Please Enter Email"
        }
        else if (!emailPatter.matcher(email_strng).matches()) {
            binding.emailProfile.error = "Please Enter Valid Email"
        }  else if (address.isEmpty()) {
            binding.addressProfile.error = "Please Enter Address"
        } else if (state_name.isEmpty()) {
            binding.cityProfile.error = "Please Enter State Name"
        } else if (city_name.isEmpty()) {
            binding.cityProfile.error = "Please Enter City Name"
        } else if (phone_strng.length < 10) {
            binding.mobileProfile.error = "Please Enter 10 digits Phone Number"

        } else if (!phone_strng.matches(pattern.toRegex())) {
            binding.mobileProfile.error = "Please Enter valid Phone Number"

        }
        try {


            if (NetWorkConection.isNEtworkConnected(requireActivity())) {

                //Set the Adapter to the RecyclerView//
                binding.progressProfile.visibility = View.VISIBLE


                var apiServices = APIClient.client.create(Api::class.java)

                val call =
                    apiServices.updateProfile(
                        getString(R.string.api_key),
                        customerid,
                        name_strng,
                         email_strng, phone_strng, state_name, city_name, address
                    )

                call.enqueue(object : Callback<SignupList_Response> {
                    override fun onResponse(
                        call: Call<SignupList_Response>,
                        response: Response<SignupList_Response>
                    ) {

                        binding.progressProfile.visibility = View.GONE
                        Log.e(ContentValues.TAG, response.toString())

                        try {
                            if (response.body()?.code == 1) {

                                //Set the Adapter to the RecyclerView//
                                Toast.makeText(
                                    activity,
                                    "" + response.body()!!.message,
                                    Toast.LENGTH_LONG
                                ).show()




                            } else if (response.body()?.code == 3) {

                                Toast.makeText(
                                    activity,
                                    "" + response.body()?.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } catch (e: NullPointerException) {
                            e.printStackTrace()
                        } catch (e: TypeCastException) {
                            e.printStackTrace()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        } catch (e: IllegalStateException) {
                            e.printStackTrace()
                        }


                    }


                    override fun onFailure(call: Call<SignupList_Response>, t: Throwable) {
                        binding.progressProfile.visibility = View.GONE
                        Log.e(ContentValues.TAG, t.localizedMessage)

                    }
                })


            } else {
                Toast.makeText(activity, "Please Check your internet", Toast.LENGTH_LONG).show()

            }
        } catch (e: UninitializedPropertyAccessException) {
            e.printStackTrace()
        }
    }

}