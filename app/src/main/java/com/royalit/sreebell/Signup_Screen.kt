package com.royalit.sreebell

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.erepairs.app.api.Api
import com.erepairs.app.models.Areas_ListResponse
import com.erepairs.app.models.Areas_Response
import com.erepairs.app.models.Districts_ListResponse
import com.erepairs.app.models.Districts_Response
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.gson.Gson
import com.royalit.sreebell.api.APIClient
import com.royalit.sreebell.databinding.SignupScreenBinding
import com.royalit.sreebell.models.SignupList_Response
import com.royalit.sreebell.utils.Constants
import com.royalit.sreebell.utils.LocationUtils
import com.royalit.sreebell.utils.NetWorkConection
import com.vanillaplacepicker.presentation.builder.VanillaPlacePicker
import com.vanillaplacepicker.utils.MapType
import com.vanillaplacepicker.utils.PickerLanguage
import com.vanillaplacepicker.utils.PickerType
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



/**
 *  Created by Sucharitha Peddinti on 27/11/21.
 */
class Signup_Screen : ComponentActivity() {
    private lateinit var binding: SignupScreenBinding
    var pattern =
        "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$"
    lateinit var name_strng: String
    lateinit var phone_strng: String
    lateinit var email_strng: String
    lateinit var password_strng: String
    lateinit var cnfrmpassword_strng: String
    lateinit var address: String
    lateinit var username_shopname: String
    lateinit var city_name: String
    var emailPatter=Patterns.EMAIL_ADDRESS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Constants.changeNotificationBarColor(this, ContextCompat.getColor(this, R.color.black), false)

        sharedPreferences =
            getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )

        binding.siginBtn.setOnClickListener {

            val intent = Intent(
                this@Signup_Screen,
                SignIn_Screen::class.java
            )
            startActivity(intent)
            finish()
        }


        var placePickerResultLauncher =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    val vanillaAddress = VanillaPlacePicker.getPlaceResult(result.data)
                    Log.e("ADDRESSSSS---> ", vanillaAddress.toString())
                    //Constants.showToast(requireContext(), vanillaAddress.toString())
                    if (vanillaAddress != null) {
                        vanillaAddress.latitude?.let {
                            vanillaAddress.longitude?.let {it1->getAddress(it, it1 )
                            }
                        }
                    }



                }

            }

        binding.addressEdit.setOnClickListener {
Log.e("Place Call","Place Call")

            val intent = VanillaPlacePicker.Builder(applicationContext)
                .setMapType(MapType.NORMAL)
                .with(PickerType.MAP_WITH_AUTO_COMPLETE)
                .withLocation(18.1124,79.0193)
                .setPickerLanguage(PickerLanguage.ENGLISH)
                .enableShowMapAfterSearchResult(true)
                .build()
            placePickerResultLauncher.launch(intent)
            Log.e("Place Call","Place Call")
        }
        binding.areaSpinner.setOnClickListener {


           // binding.areaSpinner.visibility = View.VISIBLE
            //binding.categoryText.visibility = View.GONE
//Inflate the dialog with custom view
            val dialogview =
                LayoutInflater.from(this).inflate(R.layout.areaslist_screen, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(this)
                .setView(dialogview)
                .setTitle("Areas List")

            //AlertDialogBuilder

            //show dialog
            val mAlertDialog = mBuilder.show()

            areaslist = dialogview.findViewById<RecyclerView>(R.id.areaslist)
            val progressBarcatee = dialogview.findViewById<ProgressBar>(R.id.progressBararea)
            val okbtn = dialogview.findViewById<Button>(R.id.okbtn)


            // String array for alert dialog multi choice items


            if (NetWorkConection.isNEtworkConnected(this)) {

                //Set the Adapter to the RecyclerView//


                var apiServices = APIClient.client.create(Api::class.java)

                try {
                    val call = apiServices.getAreasList(getString(R.string.api_key))
                    call.enqueue(object : Callback<Areas_ListResponse?> {
                        override fun onResponse(
                            call: Call<Areas_ListResponse?>,
                            response: Response<Areas_ListResponse?>
                        ) {
                            try {
                                if (response.isSuccessful) {


                                    val `object` = JSONObject(Gson().toJson(response.body()))
                                    Log.e("TAG", "onResponse: $`object`")

                                    val selectedserviceslist =
                                        response.body()?.response!!

                                    Log.e("Res", "" + selectedserviceslist)
                                    areasAdapter =
                                        AreasAdapter(
                                            this@Signup_Screen,
                                            selectedserviceslist as ArrayList<Areas_Response>
                                        )
                                    areaslist!!.adapter =
                                        areasAdapter
                                    areasAdapter.notifyDataSetChanged()

                                    okbtn.visibility = View.VISIBLE

                                    okbtn.setOnClickListener {

                                        if(area_name==null|| area_name!!.isEmpty()||area_name=="null")
                                        {
                                            Toast.makeText(applicationContext,"Please select any of district",Toast.LENGTH_SHORT).show()
                                            return@setOnClickListener
                                        }
                                        mAlertDialog.dismiss()

                                        binding.categoryText.text = "" + area_name
                                        binding.areaSpinner.text = "" + area_name
                                        areaslist!!.visibility = View.GONE
                                       // binding.categoryText.visibility = View.VISIBLE
                                       // binding.areaSpinner.visibility = View.GONE
                                    }

                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(call: Call<Areas_ListResponse?>, t: Throwable) {
                            progressBarcatee.visibility = View.VISIBLE

                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            } else {

                Toast.makeText(
                    this,
                    "Please Check your internet",
                    Toast.LENGTH_LONG
                ).show()
            }


        }


        binding.districtSpinner.setOnClickListener {


            //binding.districtSpinner.visibility = View.VISIBLE
           // binding.categoryDistrictText.visibility = View.GONE
//Inflate the dialog with custom view
            val dialogview =
                LayoutInflater.from(this).inflate(R.layout.areaslist_screen, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(this)
                .setView(dialogview)
                .setTitle("Districts List")

            //AlertDialogBuilder

            //show dialog
            val mAlertDialog = mBuilder.show()

            districtlist = dialogview.findViewById<RecyclerView>(R.id.areaslist)
            val progressBarcatee = dialogview.findViewById<ProgressBar>(R.id.progressBararea)
            val okbtn = dialogview.findViewById<Button>(R.id.okbtn)


            // String array for alert dialog multi choice items


            if (NetWorkConection.isNEtworkConnected(this)) {

                //Set the Adapter to the RecyclerView//


                var apiServices = APIClient.client.create(Api::class.java)

                try {
                    val call = apiServices.getDistrictsList(getString(R.string.api_key))
                    call.enqueue(object : Callback<Districts_ListResponse?> {
                        override fun onResponse(
                            call: Call<Districts_ListResponse?>,
                            response: Response<Districts_ListResponse?>
                        ) {
                            try {
                                if (response.isSuccessful) {


                                    val `object` = JSONObject(Gson().toJson(response.body()))
                                    Log.e("TAG", "onResponse: $`object`")

                                    val selectedserviceslist =
                                        response.body()?.response!!

                                    Log.e("Res", "" + selectedserviceslist)
                                    districtAdapter =
                                        DistrctAdapter(
                                            this@Signup_Screen,
                                            selectedserviceslist as ArrayList<Districts_Response>
                                        )
                                    districtlist!!.adapter =
                                        districtAdapter
                                    districtAdapter.notifyDataSetChanged()

                                    okbtn.visibility = View.VISIBLE

                                    okbtn.setOnClickListener {
                                        if(district_id==null|| district_id!!.isEmpty()||district_id=="null")
                                        {
                                            Toast.makeText(applicationContext,"Please select any of district",Toast.LENGTH_SHORT).show()
                                            return@setOnClickListener
                                        }
                                        mAlertDialog.dismiss()

                                        binding.categoryDistrictText.text = "" + district_name
                                        binding.districtSpinner.text = "" + district_name
                                        districtlist!!.visibility = View.GONE
                                       // binding.categoryDistrictText.visibility = View.VISIBLE
                                        //binding.districtSpinner.visibility = View.GONE
                                    }

                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(call: Call<Districts_ListResponse?>, t: Throwable) {
                            progressBarcatee.visibility = View.VISIBLE

                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            } else {

                Toast.makeText(
                    this,
                    "Please Check your internet",
                    Toast.LENGTH_LONG
                ).show()
            }


        }

        binding.tvPrivacyPolicy.setOnClickListener(View.OnClickListener { v: View? ->
            try {
                val intent = Intent(this@Signup_Screen, UrlLoading::class.java)
                intent.putExtra("url", Constants.PRIVACY_POLICY)
                startActivity(intent)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()


            }
        })
        binding.tvTermsOfUSe.setOnClickListener(View.OnClickListener { v: View? ->
            try {
                val intent = Intent(this@Signup_Screen, UrlLoading::class.java)
                intent.putExtra("url", Constants.TERMS_OF_USE)
                startActivity(intent)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()


            }
        })



        binding.signupBtn.setOnClickListener {


            name_strng = binding.usernameEdit.text.toString()
            phone_strng = binding.phoneEdit.text.toString()
            email_strng = binding.emailEdit.text.toString()
            password_strng = binding.passwordEdit.text.toString()
            cnfrmpassword_strng = binding.confirmpasswordEdit.text.toString()
            address = binding.addressEdit.text.toString()
            username_shopname = binding.usernameShopname.text.toString()
            city_name = binding.cityEdit.text.toString()

            if (district_id.isNullOrBlank()) {
                Toast.makeText(this, "Please Select District", Toast.LENGTH_LONG).show()
            }else if (name_strng.isEmpty()) {
                binding.usernameEdit.error = "Please Enter Name"
            }else if (username_shopname.isEmpty()) {
                binding.usernameShopname.error = "Please Enter Shop/Business Name"
            } else if (phone_strng.isEmpty()) {
                binding.phoneEdit.error = "Please Enter Phone Number"
            } else if (email_strng.isEmpty()) {
                binding.emailEdit.error = "Please Enter Email"
            }
            else if (!emailPatter.matcher(email_strng).matches()) {
                binding.emailEdit.error = "Please Enter Valid Email"
            } else if (password_strng.isEmpty()) {
                binding.passwordEdit.error = "Please Enter Password"
            } else if (cnfrmpassword_strng.isEmpty()) {
                binding.confirmpasswordEdit.error = "Please Enter Confirm Password"
            } else if (address.isEmpty()) {
                binding.addressEdit.error = "Please Enter Address"
            } else if (city_name.isEmpty()) {
                binding.cityEdit.error = "Please Enter City Name"
            } else if (phone_strng.length < 10) {
                binding.phoneEdit.error = "Please Enter 10 digits Phone Number"

            } else if (!phone_strng.matches(pattern.toRegex())) {
                binding.phoneEdit.error = "Please Enter valid Phone Number"

            } else if (!password_strng.equals(cnfrmpassword_strng)) {
                binding.confirmpasswordEdit.error = "Password doesn't matched"

            } else if (!binding.termscheck.isChecked) {
                Toast.makeText(this, "Please accept Terms and Conditions", Toast.LENGTH_LONG).show()
            } else {
               postRegister()
            }

        }


    }

    private fun getAddress(latitude: Double, longitude: Double) {
        val locationUtils = LocationUtils(applicationContext)
        locationUtils.getAddressFromLocation(
            latitude,
            longitude,
            { address ->
                // Adres alımı başarılı

                try {

                        runOnUiThread {
                            binding?.addressEdit?.setText(address)
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==200&&resultCode!=null&&resultCode== RESULT_OK)
        {
            val place=PlacePicker.getPlace(this@Signup_Screen,data)
            Log.e("Address Latitude","Address Latitude ${place}")
            val latitude=place.latLng.latitude


            Log.e("Address Latitude","Address Latitude ${latitude}")
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.forgotpasswo_screen)
        val phonenum = dialog.findViewById(R.id.phone_text) as TextView
        val ok_btn = dialog.findViewById(R.id.ok_btn) as TextView

        phonenum.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:" + "9642236668")
            startActivity(dialIntent)
        }
        ok_btn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun postRegister() {

        try {


            if (NetWorkConection.isNEtworkConnected(this)) {

                //Set the Adapter to the RecyclerView//
                binding.progressBarregister.visibility = View.VISIBLE


                var apiServices = APIClient.client.create(Api::class.java)

                val call =
                    apiServices.user_registation(
                        getString(R.string.api_key),
                        name_strng,
                        phone_strng, email_strng, password_strng, area_id = "", address, district_id!!,username_shopname,city_name
                    )

                Log.e("nme", "" + name_strng + email_strng)
                Log.e("email", "" + phone_strng + password_strng)
                Log.e("area", "" + area_id + address)

                call.enqueue(object : Callback<SignupList_Response> {
                    override fun onResponse(
                        call: Call<SignupList_Response>,
                        response: Response<SignupList_Response>
                    ) {

                        binding.progressBarregister.visibility = View.GONE
                        Log.e(ContentValues.TAG, response.toString())

                        try {
                            if (response.body()?.code == 1) {

                                //Set the Adapter to the RecyclerView//


                                Toast.makeText(
                                    this@Signup_Screen,
                                    "" + response.body()!!.message,
                                    Toast.LENGTH_LONG
                                ).show()

                                val intent = Intent(this@Signup_Screen, SignIn_Screen::class.java)
                                startActivity(intent)
                                finish()


                            } else if (response.body()?.code == 3) {

                                Toast.makeText(
                                    this@Signup_Screen,
                                    "" + response.body()?.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }

//                            else {
//                                Toast.makeText(
//                                    this@Signup_Screen,
//                                    "Invalid Mobile Number",
//                                    Toast.LENGTH_LONG
//                                ).show()
//
//                            }
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
                        binding.progressBarregister.visibility = View.GONE
                        Log.e(ContentValues.TAG, t.localizedMessage)
                        Toast.makeText(
                            this@Signup_Screen,
                            "Mobile Already Exist!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })


            } else {
                Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG).show()

            }
        } catch (e: UninitializedPropertyAccessException) {
            e.printStackTrace()
        }
    }


    companion object {
        lateinit var areasAdapter: AreasAdapter
        lateinit var districtAdapter: DistrctAdapter
        lateinit var sharedPreferences: SharedPreferences
        var areaslist: RecyclerView? = null
        var districtlist: RecyclerView? = null
        var area_id: String? = null
        var district_id: String? = null
        var area_name: String? = null
        var district_name: String? = null
    }

    class AreasAdapter(
        var context: Context,
        val userList: java.util.ArrayList<Areas_Response>
    ) :
        RecyclerView.Adapter<AreasAdapter.ViewHolder>() {


        //this method is returning the view for each item in the list
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): AreasAdapter.ViewHolder {
            val v =
                LayoutInflater.from(context).inflate(R.layout.areaspinner_adapter, parent, false)
            return ViewHolder(v)
        }

        //this method is binding the data on the list
        @SuppressLint("WrongConstant")
        override fun onBindViewHolder(holder: AreasAdapter.ViewHolder, position: Int) {


            holder.areas_textview.text = userList.get(position).area_name


            Log.e("cate_name", "" + userList.get(position).area_name)

            holder.itemView.setOnClickListener {
                area_id = userList.get(position).id.toString()
                area_name = userList.get(position).area_name.toString()
                Log.e("cate_name", " area_name area_name " + area_name)

                holder.right_image.visibility = View.VISIBLE
            }

        }

        //this method is giving the size of the list
        override fun getItemCount(): Int {
            return userList.size
        }

        //the class is hodling the list view
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val areas_textview = itemView.findViewById(R.id.areas_textview) as TextView
            val right_image = itemView.findViewById(R.id.right_image) as ImageView


        }
    }


    class DistrctAdapter(
        var context: Context,
        val userList: java.util.ArrayList<Districts_Response>
    ) :
        RecyclerView.Adapter<DistrctAdapter.ViewHolder>() {


        //this method is returning the view for each item in the list
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): DistrctAdapter.ViewHolder {
            val v =
                LayoutInflater.from(context).inflate(R.layout.areaspinner_adapter, parent, false)
            return ViewHolder(v)
        }

        //this method is binding the data on the list
        @SuppressLint("WrongConstant")
        override fun onBindViewHolder(holder: DistrctAdapter.ViewHolder, position: Int) {


            holder.areas_textview.text = userList.get(position).name


            Log.e("cate_name", "" + userList.get(position).name)

            holder.itemView.setOnClickListener {
                district_id = userList.get(position).id.toString()
                district_name = userList.get(position).name.toString()
                holder.right_image.visibility = View.VISIBLE
            }

        }

        //this method is giving the size of the list
        override fun getItemCount(): Int {
            return userList.size
        }

        //the class is hodling the list view
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val areas_textview = itemView.findViewById(R.id.areas_textview) as TextView
            val right_image = itemView.findViewById(R.id.right_image) as ImageView


        }
    }


}