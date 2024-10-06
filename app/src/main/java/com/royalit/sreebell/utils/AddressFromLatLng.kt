package com.royalit.sreebell.utils
import android.content.Context
import android.location.Geocoder
import android.text.TextUtils
import java.io.IOException
import java.util.Locale

class AddressFromLatLng {
    companion object {
        fun getAddressFromLatLong(context: Context, latitude: Double, longitude: Double): String {

            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(latitude, longitude, 5)
                var addressString = ""
                for (i in addresses?.indices!!) {
                    val index = (i + 1).toString() + ""
                    addressString =
                        if (index.isNotEmpty()) "$addressString\nindex: $index\n" else addressString

                    val postalCode = addresses[i].postalCode
                    addressString =
                        if (!postalCode.isNullOrEmpty()) "$addressString postalCode: $postalCode\n" else addressString

                    val adminArea = addresses[i].adminArea
                    addressString =
                        if (!adminArea.isNullOrEmpty()) "$addressString adminArea: $adminArea\n" else addressString

                    val subAdminArea = addresses[i].subAdminArea
                    addressString =
                        if (!subAdminArea.isNullOrEmpty()) "$addressString subAdminArea: $subAdminArea\n" else addressString

                    val locality = addresses[i].locality
                    addressString =
                        if (!locality.isNullOrEmpty()) "$addressString locality: $locality\n" else addressString

                    val subLocality = addresses[i].subLocality
                    addressString =
                        if (!subLocality.isNullOrEmpty()) "$addressString subLocality: $subLocality\n" else addressString

                    val phone = addresses[i].phone
                    addressString =
                        if (!phone.isNullOrEmpty()) "$addressString phone: $phone\n" else addressString
                }
                if (!TextUtils.isEmpty(addressString)) {
                    return addressString
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return ""
        }
    }
}