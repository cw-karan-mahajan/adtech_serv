package tv.cloudwalker.cwnxt.launcher.utils

import android.content.Context
import android.net.ConnectivityManager
import timber.log.Timber

/**
 * Created by cognoscis on 13/3/18.
 */
object NetworkUtils {
    const val TYPE_NOT_CONNECTED: Int = 0
    const val TYPE_WIFI: Int = 1
    const val TYPE_MOBILE: Int = 2
    const val TYPE_ETHERNET: Int = 3

    fun getConnectivityStatus(context: Context): Int {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        Timber.e("activeNetwork in NetworkUtils  :  %s", activeNetwork)
        /*
        connectivityManager.getActiveNetworkInfo();
        This may return null when there is no default network.
        Note that if the default network is a VPN, this method will return the NetworkInfo for one of its underlying networks instead,
        or null if the VPN agent did not specify any.
*/
        if (activeNetwork != null) {
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                return TYPE_WIFI
            }

            if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                return TYPE_MOBILE
            }

            if (activeNetwork.type == ConnectivityManager.TYPE_ETHERNET) {
                return TYPE_ETHERNET
            }
        }

        return TYPE_NOT_CONNECTED
    }

    fun getConnectivityStatusString(context: Context): String? {
        val conn = getConnectivityStatus(context)
        var status: String? = null
        if (conn == TYPE_WIFI) {
            status = "Wifi enabled"
        } else if (conn == TYPE_MOBILE) {
            status = "Mobile data enabled"
        } else if (conn == TYPE_ETHERNET) {
            status = "Ethernet enabled"
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet"
        }
        return status
    }
}
