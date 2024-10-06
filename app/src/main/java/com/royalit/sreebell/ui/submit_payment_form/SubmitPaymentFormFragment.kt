package com.royalit.sreebell.ui.submit_payment_form

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.erepairs.app.api.Api
import com.erepairs.app.models.Common_Response
import com.royalit.sreebell.HomeScreen
import com.royalit.sreebell.R
import com.royalit.sreebell.SignIn_Screen
import com.royalit.sreebell.Signup_Screen
import com.royalit.sreebell.adapter.CategoryList_Adapter
import com.royalit.sreebell.adapter.ManualPaymentList_Adapter
import com.royalit.sreebell.adapter.SubProductList_Adapter
import com.royalit.sreebell.api.APIClient
import com.royalit.sreebell.databinding.FragmentPaymentDetailsBinding
import com.royalit.sreebell.databinding.FragmentSubmitPaymentFormBinding
import com.royalit.sreebell.models.Category_ListResponse
import com.royalit.sreebell.models.Category_Response
import com.royalit.sreebell.models.Category_subListResponse
import com.royalit.sreebell.models.Category_subResponse
import com.royalit.sreebell.models.ManualOrderPaymentList
import com.royalit.sreebell.models.ManualOrderPaymentResponse
import com.royalit.sreebell.models.PaymentDetailsSubmit_Response
import com.royalit.sreebell.models.SignupList_Response
import com.royalit.sreebell.roomdb.CartItems
import com.royalit.sreebell.ui.payment_details.PaymentDetailsViewModel
import com.royalit.sreebell.utils.Constants
import com.royalit.sreebell.utils.NetWorkConection
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
import java.util.Calendar

class SubmitPaymentFormFragment : Fragment() {


    private lateinit var AmountDepositedTransferred: String
    private lateinit var InvoiceNumber: String
    private lateinit var UTRNumber: String
    private lateinit var InvoiceDate: String
    private lateinit var DeposittedDate: String
    private lateinit var CustomerName: String
    private var _binding: FragmentSubmitPaymentFormBinding? = null
    lateinit var sharedPreferences: SharedPreferences
    lateinit var customerid: String
    lateinit var manualPaymentAdapter: ManualPaymentList_Adapter
    private var manualOrderPaymentList: List<ManualOrderPaymentResponse> = ArrayList()
    lateinit var layoutManager: LayoutManager

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myAccountViewModel =
            ViewModelProvider(this).get(PaymentDetailsViewModel::class.java)

        _binding = FragmentSubmitPaymentFormBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // val textView: TextView = binding.tvHelp
        myAccountViewModel.text.observe(viewLifecycleOwner) {
            //textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences =
            requireContext().getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )

        customerid = sharedPreferences.getString("userid", "").toString()

        binding.etInvoiceDate.setOnClickListener {
            datePickerClickOne("etInvoiceDate")
        }
        binding.etDeposittedDate.setOnClickListener {
            datePickerClickOne("etDeposittedDate")
        }
        binding.btnSubmit.setOnClickListener {
            CustomerName = binding.etCustomerName.text.toString()
            DeposittedDate = binding.etDeposittedDate.text.toString()
            InvoiceDate = binding.etInvoiceDate.text.toString()
            UTRNumber = binding.etUTRNumber.text.toString()
            InvoiceNumber = binding.etInvoiceNumber.text.toString()
            AmountDepositedTransferred = binding.etAmountDepositedTransferred.text.toString()

            if (CustomerName.isEmpty()) {
                binding.etCustomerName.error = "Please Enter Name"
            } else if (DeposittedDate.isEmpty()) {
                binding.etDeposittedDate.error = "Please Select Deposited Date"
            } else if (InvoiceDate.isEmpty()) {
                binding.etInvoiceDate.error = "Please Select Invoiced Date"
            } else if (UTRNumber.isEmpty()) {
                binding.etUTRNumber.error = "Please Enter UTR Number"
            } else if (InvoiceNumber.isEmpty()) {
                binding.etInvoiceNumber.error = "Please Enter Invoice Number"
            } else if (AmountDepositedTransferred.isEmpty()) {
                binding.etAmountDepositedTransferred.error =
                    "Please Enter Amount Deposited Transferred"
            } else {
                postValidationSubmitForm()
            }
        }

        binding.fabAddPayment.setOnClickListener {
            binding.fabAddPayment.visibility = View.GONE
            binding.llForm.visibility = View.VISIBLE
            binding.rcvManualPayments.visibility = View.GONE
            binding.tvNoRecordsFound.visibility = View.GONE

            binding.etCustomerName.setText("")
            binding.etDeposittedDate.setText("")
            binding.etInvoiceDate.setText("")
            binding.etUTRNumber.setText("")
            binding.etInvoiceNumber.setText("")
            binding.etAmountDepositedTransferred.setText("")
        }

        //val recyclerView = binding.rcvManualPayments
        layoutManager = LinearLayoutManager(activity)
        binding.rcvManualPayments.layoutManager = layoutManager
        binding.rcvManualPayments.setNestedScrollingEnabled(false);
       // ViewCompat.setNestedScrollingEnabled(binding.rcvManualPayments, false);

        getManualPayments()

    }


    private fun getManualPayments() {

        if (NetWorkConection.isNEtworkConnected(activity as Activity)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)

            val call =
                apiServices.manualOrderPaymentList(getString(R.string.api_key), customerid)


            binding.subproductprogress.visibility = View.VISIBLE
            call.enqueue(object : Callback<ManualOrderPaymentList> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<ManualOrderPaymentList>,
                    response: Response<ManualOrderPaymentList>
                ) {

                    Log.e(ContentValues.TAG, response.toString())
                    binding.subproductprogress.visibility = View.GONE

                    if (response.isSuccessful) {

                        try {

                            //Set the Adapter to the RecyclerView//

                            manualOrderPaymentList =
                                response.body()?.response!!
                            if(manualOrderPaymentList.size > 0) {
                                // manualPaymentAdapter.setProductsList(manualOrderPaymentList as ArrayList<ManualOrderPaymentResponse>)
                                manualPaymentAdapter =
                                    ManualPaymentList_Adapter(
                                        activity as Activity,
                                        manualOrderPaymentList
                                    )
                               // manualPaymentAdapter.setDataList(manualOrderPaymentList)
                                binding.rcvManualPayments.adapter = manualPaymentAdapter
                                manualPaymentAdapter.notifyDataSetChanged()
                            }else{
                                binding.tvNoRecordsFound.visibility = View.VISIBLE
                            }


                        } catch (e: java.lang.NullPointerException) {
                            e.printStackTrace()
                        }

                    }

                }

                override fun onFailure(call: Call<ManualOrderPaymentList>, t: Throwable) {
                    Log.e(ContentValues.TAG, t.toString())
                    binding.subproductprogress.visibility = View.GONE

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

    private fun postValidationSubmitForm() {

        try {


            if (NetWorkConection.isNEtworkConnected(requireActivity())) {

                //Set the Adapter to the RecyclerView//
                binding.progressBarregister.visibility = View.VISIBLE



                var apiServices = APIClient.client.create(Api::class.java)

                val call =
                    apiServices.manualOrderPayment(
                        getString(R.string.api_key),
                        CustomerName,
                        InvoiceNumber,
                        InvoiceDate,
                        AmountDepositedTransferred,
                        customerid,
                        DeposittedDate,
                        UTRNumber

                    )



                call.enqueue(object : Callback<PaymentDetailsSubmit_Response> {
                    override fun onResponse(
                        call: Call<PaymentDetailsSubmit_Response>,
                        response: Response<PaymentDetailsSubmit_Response>
                    ) {

                        binding.progressBarregister.visibility = View.GONE
                        Log.e(ContentValues.TAG, response.toString())

                        try {
                            Constants.showToast(requireActivity(), response.body()!!.message)
                            /*val intent =
                                Intent(requireActivity(), HomeScreen::class.java)
                            startActivity(intent)
                            requireActivity().finish()*/


                            binding.fabAddPayment.visibility = View.VISIBLE
                            binding.llForm.visibility = View.GONE
                            binding.rcvManualPayments.visibility = View.VISIBLE
                            getManualPayments()

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


                    override fun onFailure(
                        call: Call<PaymentDetailsSubmit_Response>,
                        t: Throwable
                    ) {
                        binding.progressBarregister.visibility = View.GONE
                        Log.e(ContentValues.TAG, t.localizedMessage)
                        Toast.makeText(
                            requireActivity(),
                            "${t.localizedMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })


            } else {
                Toast.makeText(requireContext(), "Please Check your internet", Toast.LENGTH_LONG)
                    .show()

            }
        } catch (e: UninitializedPropertyAccessException) {
            e.printStackTrace()
        }
    }

    fun datePickerClickOne(typeOfClick: String) {
        val c = Calendar.getInstance()
        val mYear = c[Calendar.YEAR]
        val mMonth = c[Calendar.MONTH]
        val mDay = c[Calendar.DAY_OF_MONTH]
        val newCalendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            binding?.etDeposittedDate!!.getContext(),
            { view1: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                newCalendar[dayOfMonth, month] = year
                val date = year.toString() + "-" + (month + 1) + "-" + dayOfMonth.toString()
                if (typeOfClick == "etDeposittedDate")
                    binding!!.etDeposittedDate.setText(date)
                else if (typeOfClick == "etInvoiceDate")
                    binding!!.etInvoiceDate.setText(date)
            }, mYear, mMonth, mDay
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }


}