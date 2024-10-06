package com.royalit.sreebell.ui.payment_details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.royalit.sreebell.R
import com.royalit.sreebell.databinding.FragmentPaymentDetailsBinding

class PaymentDetailsFragment : Fragment() {

    private var _binding: FragmentPaymentDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myAccountViewModel =
            ViewModelProvider(this).get(PaymentDetailsViewModel::class.java)

        _binding = FragmentPaymentDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

       // val textView: TextView = binding.tvHelp
        myAccountViewModel.text.observe(viewLifecycleOwner) {
            //textView.text = it
        }
        return root
    }
}