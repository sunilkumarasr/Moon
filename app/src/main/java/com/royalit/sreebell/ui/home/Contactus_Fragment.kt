package com.royalit.sreebell.ui.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.royalit.sreebell.databinding.ContactusScreenBinding


class Contactus_Fragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: ContactusScreenBinding? = null

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

        _binding = ContactusScreenBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.phone2.setOnClickListener {

            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:" + 919849152904) //change the number.
            if (Build.VERSION.SDK_INT > 23) {
                startActivity(callIntent)
            } else {
                if (ActivityCompat.checkSelfPermission(
                        activity as Activity,
                        Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(
                        activity as Activity,
                        "Permission Not Granted ",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    val PERMISSIONS_STORAGE = arrayOf<String>(Manifest.permission.CALL_PHONE)
                    ActivityCompat.requestPermissions(activity as Activity, PERMISSIONS_STORAGE, 9)
                    startActivity(callIntent)
                }
            }
        }
        binding.phone2.setOnClickListener {

            onCall()
        }


        val submitButton: Button = binding.contactusBtn
        submitButton.visibility=View.GONE
        submitButton.setOnClickListener {
            // Handle the click event here
            // You can perform actions such as submitting data or navigating to another screen
            // For example, you can show a toast message for demonstration purposes:
            Toast.makeText(requireContext(), "SUBMIT button clicked", Toast.LENGTH_SHORT).show()
        }
        return root
    }

    fun onCall() {
        val permissionCheck =
            ContextCompat.checkSelfPermission(activity as Activity, Manifest.permission.CALL_PHONE)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity as Activity, arrayOf(Manifest.permission.CALL_PHONE),
                123
            )
        } else {
            startActivity(Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:9676166188")))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}