package com.royalit.sreebell

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.Window
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.erepairs.app.api.Api
import com.google.android.gms.location.places.ui.PlacePicker
import com.royalit.sreebell.api.APIClient
import com.royalit.sreebell.databinding.SignupScreenBinding
import com.royalit.sreebell.models.SignupList_Response
import com.royalit.sreebell.utils.Constants
import com.royalit.sreebell.utils.LocationUtils
import com.royalit.sreebell.utils.NetWorkConection
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
    lateinit var city_name: String
    lateinit var sate_name: String
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
            address = binding.addressEdit.text.toString()
            sate_name = binding.stateEdit.text.toString()
            city_name = binding.cityEdit.text.toString()
            password_strng = binding.passwordEdit.text.toString()
            cnfrmpassword_strng = binding.confirmpasswordEdit.text.toString()

            if (name_strng.isEmpty()) {
                binding.usernameEdit.error = "Please Enter Name"
            }
            else if (phone_strng.isEmpty()) {
                binding.phoneEdit.error = "Please Enter Phone Number"
            }
            else if (email_strng.isEmpty()) {
                binding.emailEdit.error = "Please Enter Email"
            }
            else if (!emailPatter.matcher(email_strng).matches()) {
                binding.emailEdit.error = "Please Enter Valid Email"
            }
            else if (phone_strng.length < 10) {
                binding.phoneEdit.error = "Please Enter 10 digits Phone Number"

            }
            else if (!phone_strng.matches(pattern.toRegex())) {
                binding.phoneEdit.error = "Please Enter valid Phone Number"

            }
            else if (address.isEmpty()) {
                binding.addressEdit.error = "Please Enter Address"
            }
            else if (city_name.isEmpty()) {
                binding.cityEdit.error = "Please Enter City Name"
            }
            else if (sate_name.isEmpty()) {
                binding.stateEdit.error = "Please Enter State Name"
            }
            else if (password_strng.isEmpty()) {
                binding.passwordEdit.error = "Please Enter Password"
            }
            else if (cnfrmpassword_strng.isEmpty()) {
                binding.confirmpasswordEdit.error = "Please Enter Confirm Password"
            }
            else if (!password_strng.equals(cnfrmpassword_strng)) {
                binding.confirmpasswordEdit.error = "Password doesn't matched"

            }
            else if (!binding.termscheck.isChecked) {
                Toast.makeText(this, "Please accept Terms and Conditions", Toast.LENGTH_LONG).show()
            }
            else {
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
                        phone_strng, email_strng, address, sate_name, city_name, password_strng)

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
        lateinit var sharedPreferences: SharedPreferences
    }

}