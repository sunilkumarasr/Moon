package com.royalit.sreebell.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.ProgressBar;

/**
 * Created By Venkei
 * On 08-04-2021
 */
public class Utilities {

    private static final String TAG = "Utilities";
    private static ProgressDialog mProgressDialog;
    private static ProgressBar progressBar;
    public static String customerid;
    public static Integer customer_category=1;

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    public static String getDeviceID(Activity activity) {
        return Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }



}
