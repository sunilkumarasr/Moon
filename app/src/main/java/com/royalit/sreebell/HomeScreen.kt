package com.royalit.sreebell

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.erepairs.app.api.Api
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.royalit.sreebell.api.APIClient
import com.royalit.sreebell.databinding.ActionbarTitleBinding
import com.royalit.sreebell.databinding.ActivityHomeScreenBinding
import com.royalit.sreebell.models.CartListResponse
import com.royalit.sreebell.models.LoginList_Response
import com.royalit.sreebell.roomdb.AppConstants
import com.royalit.sreebell.roomdb.CartViewModel
import com.royalit.sreebell.utils.Constants
import com.royalit.sreebell.utils.Constants.IS_APP_STARTED
import com.royalit.sreebell.utils.NetWorkConection
import com.royalit.sreebell.utils.Utilities
import com.royalit.sreebell.utils.Utilities.customerid
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeScreen : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var navBottomView: BottomNavigationView
    lateinit var navView: NavigationView
    lateinit var profilepic: ImageView
    lateinit var profile_name: TextView
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeScreenBinding
    lateinit var navController: NavController
    lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var customerid: String
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IS_APP_STARTED=1
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Constants.changeNotificationBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        drawerLayout = binding.drawerLayout
        navView = binding.navView
        navBottomView = findViewById(R.id.bottom_navigation_view)
        sharedPreferences =
            getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )

        customerid = sharedPreferences.getString("userid", "")!!
        com.royalit.sreebell.utils.Utilities.customer_category = sharedPreferences.getInt("customer_category", 1)!!
        com.royalit.sreebell.utils.Utilities.customerid=customerid
        navController = findNavController(R.id.nav_host_fragment_content_home_screen)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.navigation_categories, R.id.nav_contactus
            ), drawerLayout
        )

        navView.setupWithNavController(navController)
        navBottomView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)
        val headerView: View = binding.navView.getHeaderView(0)

        binding.appBarHomeScreen.imgMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.appBarHomeScreen.imgNotification.setOnClickListener {
            navController.navigate(R.id.navigation_notify)
        }
        binding.appBarHomeScreen.bagredCart.bagredImage.setOnClickListener{
            navController.navigate(R.id.navigation_cart)
        }

        profile_name = headerView.findViewById(R.id.text_profile)
        profilepic = headerView.findViewById(R.id.imageView_home)
        getProfile()
        getCart()
        askNotificationPermission()

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
        {

        }else
        {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.isNotEmpty()) {

            }
        }
    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {

            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // Display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
                ActivityCompat.requestPermissions(this@HomeScreen,  arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 200);
            } else {
                // Directly ask for the permission
                ActivityCompat.requestPermissions(this@HomeScreen,  arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 200);

                //requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isChecked = true
        drawerLayout.closeDrawers()

        val id = item.itemId

        when (id) {
            R.id.nav_contactus ->
                navController.navigate(R.id.nav_contactus)
            R.id.nav_home ->
                navController.navigate(R.id.nav_home)
            R.id.navigation_categories ->
                navController.navigate(R.id.navigation_categories)
            R.id.navigation_products ->
                navController.navigate(R.id.navigation_viewallproducts)
            R.id.navigation_cart ->
                navController.navigate(R.id.navigation_cart)
            R.id.navigation_account ->
                navController.navigate(R.id.navigation_account)
            R.id.nav_orderhis ->
                navController.navigate(R.id.navigation_orderhis)
            R.id.nav_payment_details ->
                navController.navigate(R.id.navigation_payment_details)
            R.id.nav_submit_payment_details ->
                navController.navigate(R.id.navigation_submit_payment_details)
            R.id.nav_logout ->
                logoutfun()
        }

        return true

    }

    fun logoutfun() {

        val editor=sharedPreferences.edit()
        editor.clear()
        editor.commit()
        //sharedPreferences.edit().clear().commit()
       // sharedPreferences.edit().clear().apply()

        intent = Intent(applicationContext,SignIn_Screen::class.java)
        startActivity(intent)
        finish()

    }

    private fun getProfile() {
        try {

            if (NetWorkConection.isNEtworkConnected(this)) {

                //Set the Adapter to the RecyclerView//

                var apiServices = APIClient.client.create(Api::class.java)

                val call =
                    apiServices.getprofile(getString(R.string.api_key), customerid)

                call.enqueue(object : Callback<LoginList_Response> {
                    @SuppressLint("WrongConstant")
                    override fun onResponse(
                        call: Call<LoginList_Response>,
                        response: Response<LoginList_Response>
                    ) {

                        Log.e(ContentValues.TAG, response.toString())

                        if (response.isSuccessful) {

                            try {
//
//                                Glide.with(this@HomeScreen)
////                                    .load(response.body()?.response!!.profile_pic)
//                                    .placeholder(R.drawable.logo)
//                                    .apply(RequestOptions().centerCrop())
//                                    .into(profilepic)


                                profile_name.text = "" + response.body()!!.response.full_name

//                            val editor = sharedPreferences.edit()
//                            editor.putString("selectcatid", response.body()!!.response.category_ids)
//                            editor.putString(
//                                "selectsubcatid",
//                                response.body()!!.response.subcategory_ids
//                            )
//                            editor.commit()

                            } catch (e: NullPointerException) {
                                e.printStackTrace()
                            } catch (e: TypeCastException) {
                                e.printStackTrace()
                            } catch (e: IllegalArgumentException) {
                                e.printStackTrace()
                            } catch (e: IllegalStateException) {
                                e.printStackTrace()
                            }

                        }


                    }

                    override fun onFailure(call: Call<LoginList_Response>, t: Throwable) {
                        Log.e(ContentValues.TAG, t.toString())

                    }

                })


            } else {

                Toast.makeText(
                    this,
                    "Please Check your internet",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home_screen)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun getCart() {

        if (NetWorkConection.isNEtworkConnected(this@HomeScreen)) {

                val apiServices = APIClient.client.create(Api::class.java)
                var call=  apiServices.getCartList(getString(R.string.api_key),customer_id = com.royalit.sreebell.utils.Utilities.customerid)

                Log.e("cat", " customerid " + com.royalit.sreebell.utils.Utilities.customerid)

                call?.enqueue(object : Callback<CartListResponse> {
                    @SuppressLint("WrongConstant")
                    override fun onResponse(call: Call<CartListResponse>, response: Response<CartListResponse>) {
                        Log.e(ContentValues.TAG, response.toString())

                        if (response.isSuccessful) {
                            try {

                                binding.appBarHomeScreen.bagredCart.cartBadgeCount?.text  = "" + response.body()?.cartList?.size!!

                            } catch (e: java.lang.NullPointerException) {
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun onFailure(call: Call<CartListResponse>, t: Throwable) {
                        Log.e(ContentValues.TAG, t.toString())
                    }
                })
            } else {
            Toast.makeText(
                applicationContext,
                "Please Check your internet",
                Toast.LENGTH_LONG
            ).show()
        }


    }

    fun updateCartCount()
    {
        getCart()
    }
}