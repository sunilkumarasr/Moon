package com.royalit.sreebell.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.WindowInsetsControllerCompat
import com.royalit.sreebell.R

object Constants {
    public const val BASE_URL = "https://royalitpark.org/moon/admin/api/"

   // public const val BASE_URL = "https://sreebellkart.com/admin/api/"
    public const val BASE_URL_PHOTOS = "https://sreebellkart.com/admin/uploads/products/"
    public const val PRIVACY_POLICY = "https://sreebellkart.com/privacy.html"
    public const val TERMS_OF_USE = "https://sreebellkart.com/terms.html"
    public var OFFER_PRICE = ""
    var IS_APP_STARTED=0
    fun changeNotificationBarColor(activity: Activity, color: Int, isLight: Boolean) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.statusBarColor = color

        WindowInsetsControllerCompat(activity.window, activity.window.decorView).isAppearanceLightStatusBars = isLight
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showSuccessToast(context: Context, message: String) {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.custom_toast_success, null)

        val tvMessage = layout.findViewById<TextView>(R.id.tvMessage)
        tvMessage.text = message

        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.setGravity(Gravity.BOTTOM, 0, 0)
        toast.view = layout
        toast.show()


        /*val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val customToastLayout = inflater.inflate(R.layout.custom_toast_success,null)
        val customToast = Toast(context)
        customToast.view = customToastLayout
        customToast.setGravity(Gravity.CENTER,0,0)
        customToast.duration = Toast.LENGTH_LONG
        customToast.show()


        // Inflating the layout for the toast
        // Inflating the layout for the toast
       ;


        val layout: View =  LayoutInflater.from(context).inflate(bfer.fmis.ap.gov.R.layout.custom_toast_success, null)


        val text = layout.findViewById<View>(R.id.tvtoast) as TextView

        text.text = "No internet connection"
        text.setTextColor(Color.rgb(0, 132, 219))
        val toast = Toast(context)
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.duration = Toast.LENGTH_LONG
        toast.setView(layout)
        toast.show()*/
    }

    fun showCustomAlert(ctx: Context?, heading: String?, message: String?) {
        val dialog = Dialog(ctx!!, R.style.AlertDialogCustom)
        dialog.setContentView(R.layout.custom_alert)
        dialog.setCancelable(false)
        val tvHeading = dialog.findViewById<TextView>(R.id.tvHeading)
        val tvContent = dialog.findViewById<TextView>(R.id.tvContent)
        val btnCancel = dialog.findViewById<AppCompatButton>(R.id.btnCancel)
        val btnOk = dialog.findViewById<AppCompatButton>(R.id.btnOk)
        tvContent.text = message
        tvHeading.text = heading
        btnOk.setOnClickListener { v: View? -> dialog.dismiss() }
        btnCancel.setOnClickListener { v: View? -> dialog.dismiss() }
        dialog.show()
    }
    enum class ObserverEvents{
        CALENDER_SELECTION_ONE, CALENDER_SELECTION_TWO
    }
}