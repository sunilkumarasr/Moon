package com.royalit.sreebell.notifications

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 *  Created by Sucharitha Peddinti on 17/10/21.
 */

class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {
    private var TAG = "MyFirebaseInstanceIDService"

    override fun onTokenRefresh() {
        //Get updated token
        var refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "New Token : $refreshedToken")

    }
}