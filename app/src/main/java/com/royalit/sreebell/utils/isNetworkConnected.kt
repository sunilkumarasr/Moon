package com.royalit.sreebell.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager


/**
 * Created by Suchi 18/07/2019.
 */
class NetWorkConection {

    private var mProgressDialog: ProgressDialog? = null

    companion object {
        @SuppressLint("MissingPermission")
        fun isNEtworkConnected(context: Activity): Boolean {

            var connectionManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectionManager.activeNetworkInfo

            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
        }
    }

    fun showSimpleProgressDialog(
        context: Context,
        nothing: Nothing?,
        s: String,
        b: Boolean
    ) {
        showSimpleProgressDialog(context, null, "Loading...", false)
    }

    fun removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog!!.isShowing) {
                    mProgressDialog!!.dismiss()
                    mProgressDialog = null
                }
            }
        } catch (ie: IllegalArgumentException) {
            ie.printStackTrace()

        } catch (re: RuntimeException) {
            re.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}

