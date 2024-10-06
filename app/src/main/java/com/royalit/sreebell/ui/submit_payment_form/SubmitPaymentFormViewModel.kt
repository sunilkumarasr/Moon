package com.royalit.sreebell.ui.submit_payment_form

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.royalit.sreebell.databinding.FragmentSubmitPaymentFormBinding
import java.util.Calendar

class SubmitPaymentFormViewModel : ViewModel() {
    var binding: FragmentSubmitPaymentFormBinding? = null
    private val _text = MutableLiveData<String>().apply {
        value = "This is Help"
    }
    val text: LiveData<String> = _text


    fun datePickerClickOne() {
        val c = Calendar.getInstance()
        val mYear = c[Calendar.YEAR]
        val mMonth = c[Calendar.MONTH]
        val mDay = c[Calendar.DAY_OF_MONTH]
        val newCalendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(binding?.etDeposittedDate!!.getContext(),
            { view1: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                newCalendar[dayOfMonth, month] = year
                val date = dayOfMonth.toString() + "-" + (month + 1) + "-" + year
                binding!!.etDeposittedDate.setText(date)
            }, mYear, mMonth, mDay
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }
}