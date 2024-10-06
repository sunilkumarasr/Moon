package com.royalit.sreebell.notifications

import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.erepairs.app.api.Api
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.royalit.sreebell.HomeScreen
import com.royalit.sreebell.R
import com.royalit.sreebell.api.APIClient
import com.royalit.sreebell.models.LoginList_Response
import com.royalit.sreebell.utils.Constants
import com.royalit.sreebell.utils.NetWorkConection

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.lang.System.currentTimeMillis
import java.net.URL
import javax.net.ssl.HttpsURLConnection


/**
 *  Created by Sucharitha Peddinti on 17/10/21.
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        lateinit var sharedPreferences: SharedPreferences

        private val TAG = "MyFirebaseToken"
        private lateinit var notificationManager: NotificationManager
        private  var title: String=""
        private  var notification_body: String=""
        private  var userid: String=""
        var token: String? = ""
        var body: String? = ""
        var id: String? = ""
         //var context: Context


        fun sendMessage(title: String, content: String, topic: String) {
            GlobalScope.launch {
                val endpoint = "https://fcm.googleapis.com/fcm/send"
                try {
                    val url = URL(endpoint)
                    val httpsURLConnection: HttpsURLConnection =
                        url.openConnection() as HttpsURLConnection
                    httpsURLConnection.readTimeout = 10000
                    httpsURLConnection.connectTimeout = 15000
                    httpsURLConnection.requestMethod = "POST"
                    httpsURLConnection.doInput = true
                    httpsURLConnection.doOutput = true

                    // Adding the necessary headers
                    httpsURLConnection.setRequestProperty(
                        "authorization",
                        "key=${R.string.fcm_key}"
                    )
                    httpsURLConnection.setRequestProperty("Content-Type", "application/json")

                    // Creating the JSON with post params
                    val body = JSONObject()

                    val data = JSONObject()
                    data.put("title", title)
                    data.put("body", content)
                    // body.put("body", data)

                    body.put("to", "/topics/$topic")

                    val outputStream: OutputStream =
                        BufferedOutputStream(httpsURLConnection.outputStream)
                    val writer = BufferedWriter(OutputStreamWriter(outputStream, "utf-8"))
                    writer.write(body.toString())
                    writer.flush()
                    writer.close()
                    outputStream.close()
                    val responseCode: Int = httpsURLConnection.responseCode
                    val responseMessage: String = httpsURLConnection.responseMessage
                    Log.d("Response:", "$responseCode $responseMessage")
                    var result = String()
                    var inputStream: InputStream? = null

                    inputStream = if (responseCode in 400..499) {
                        httpsURLConnection.errorStream
                    } else {
                        httpsURLConnection.inputStream
                    }

                    if (responseCode == 200) {
                        Log.e("Success:", "notification sent $title \n $content")
                        // The details of the user can be obtained from the result variable in JSON format


                    } else {
                        Log.e("Error", "Error Response")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }


    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.e("onMessageReceived: ", p0.data.toString())

        Log.e("onMessageReceived: ", p0.notification.toString())


        if (p0.data.isNotEmpty()) {
            try {
                val params: Map<String?, String?> = p0.data
                val json = JSONObject(params)

                val message = json.getString("message")
                var obj = JSONObject(message)
                val jsonString = message.substring(1, message.length - 1).replace("\\", "")
                val jsonObject = JSONTokener(message).nextValue() as JSONObject
                if(jsonObject.getString("type")=="0")
                {
                    getProfile()
                    return
                }
                id = jsonObject.getString("id")
                title = jsonObject.getString("title")
                body = jsonObject.getString("body")
//
//                if ((id.equals("1"))){
//                    startActivity(Intent(context,Home_Screen::class.java))
//                }

                Log.e(TAG, "onMessageReceived: $json")
                Log.e("jsonString", "" + jsonString)
                Log.e("id", "" + id)


                val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

//        Log.e("tilte", "" + title)
                val intent = Intent(this, HomeScreen::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.putExtra("id", id)
                Log.e("id", " 12 " + id)
                val pendingIntent: PendingIntent
                pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )
                } else {
                    PendingIntent.getActivity(
                        this,
                        0, intent, PendingIntent.FLAG_IMMUTABLE
                    )
                }
                Log.e("id", " 13 " + id)

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    checkNotificationChannel("Sreebel_channel")
                }

                Log.e("id", " 14 " + id)

                val notification = NotificationCompat.Builder(applicationContext, "Sreebel_channel")
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val color = ContextCompat.getColor(this, R.color.red)
                    notification.setColor(
                        Color.RED

                    )
//            notification.setColor(ContextCompat.getColor(this, R.color.red))


                } else {
                    val color = ContextCompat.getColor(this, R.color.red)
                    notification.setColor(
                        Color.RED


                    )
//            notification.setColor(ContextCompat.getColor(this, R.color.red))
                }
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("SreeBell")

                    .setContentTitle(title)
                    .setContentText(body)
                    .setStyle(
                        NotificationCompat.MessagingStyle(title!!)
                            .setGroupConversation(false)
                            .addMessage(body, currentTimeMillis(), title)
                    )
                    //.setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setSound(defaultSound)
                Log.e("id", " 15 " + id)

                val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(1, notification.build())
                Log.e("id", " 16 " + id)

            } catch (e: JSONException) {
                Log.e(TAG, "Exception: " + e.message)
            }
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkNotificationChannel(CHANNEL_ID: String) {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            "Sreebel_channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.description = body
        notificationChannel.enableLights(true)

        notificationChannel.enableVibration(true)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    override fun onNewToken(p0: String) {
        token = p0
        super.onNewToken(p0)
    }

    private fun getProfile() {
        try {
            Log.e("Profile Data", "Called get profile data 000")
           val  sharedPreferences =
                applicationContext.getSharedPreferences(
                    "loginprefs",
                    Context.MODE_PRIVATE
                )
            Log.e("Profile Data", "Called get profile data 010")
           val customerid = sharedPreferences.getString("userid", "").toString()
            Log.e("Profile Data", "Called get profile data 110")
            val apiServices = APIClient.client.create(Api::class.java)
                val call = apiServices.getprofile(getString(R.string.api_key), customerid)



                call.enqueue(object : Callback<LoginList_Response> {
                    @SuppressLint("WrongConstant")
                    override fun onResponse(
                        call: Call<LoginList_Response>,
                        response: Response<LoginList_Response>
                    ) {
                        Log.e("Profile Data", response.toString())


                        if (response.isSuccessful) {
                            try {
                                val editor = sharedPreferences.edit()
                                editor.putString(
                                    "userid",
                                    response.body()?.response!!.customer_id.toString()
                                )
                                editor.putString(
                                    "address",
                                    response.body()?.response!!.address.toString()
                                )
                                editor.putString(
                                    "username",
                                    response.body()?.response!!.full_name.toString()
                                )
                                editor.putBoolean("islogin", true)

                                editor.putString(
                                    "mobilenumber",
                                    response.body()!!.response.mobile_number
                                )
                                editor.putInt(
                                    "customer_category",
                                    response.body()!!.response.customer_category
                                )
                                editor.putString(
                                    "district_id",
                                    response.body()!!.response.district_id
                                )
                                editor.commit()

                                Log.e("Profile Data", "Called get profile data 111")
                                if(Constants.IS_APP_STARTED==1)
                                {
                                    val intent=Intent(applicationContext,HomeScreen::class.java)
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    startActivity(intent)
                                }

                            } catch (e: java.lang.NullPointerException) {
                                e.printStackTrace()
                                Log.e("Profile Data", "Called get profile data 123")
                            }
                        }
                    }

                    override fun onFailure(call: Call<LoginList_Response>, t: Throwable) {
                        Log.e("Profile Data", t.toString())
                        Log.e("Profile Data", "Called get profile data 124")
                    }
                })

        } catch (e: NullPointerException) {
            e.printStackTrace()
            Log.e("Profile Data", "Called get profile data 125 ${e.message}")
        } catch (e: JSONException) {
            e.printStackTrace()
            Log.e("Profile Data", "Called get profile data 126 ${e.message}")
        }
    }
}