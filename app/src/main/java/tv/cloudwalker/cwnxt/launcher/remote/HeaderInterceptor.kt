package tv.cloudwalker.cwnxt.launcher.remote

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeaderInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val deviceInfo = getHeaderMap()
        Log.d("Headers", "" + deviceInfo)

        val modifiedRequest = originalRequest.newBuilder()
            .apply {
                deviceInfo.forEach { (key, value) ->
                    addHeader(key, value)
                }
            }
            .build()

        return chain.proceed(modifiedRequest)
    }

   private fun getHeaderMap(): HashMap<String, String> {
        val headerMap = HashMap<String, String>()
        headerMap["emac"] = "00:25:92:84:F7:47"
        headerMap["wmac"] = "D4:9E:3B:37:BA:07"
        headerMap["mboard"] = "TP.SK518D.PB802"
        headerMap["ram"] = "1G"
        headerMap["skinversion"] = "1.0.0-dynamic_skin-62-g3bc3758"//getUiVersion()

        headerMap["panel"] = "V320BJ8_Q01"//getPanel()
        headerMap["model"] = "SMART TV"//getModel()
        headerMap["keymd5"] = "FD889462A56360ED250705AF8603A602"//getKeyMD5()
        headerMap["keysha256"] = "FF062321D475505A7DB6ED582A3F3411"//getSHA256()
        headerMap["cotaversion"] = ""//
        headerMap["fotaversion"] = "20240628_203625"//getFOTA()
        headerMap["brand"] = "CLOUDTV_DEMO"//getBrand()
        headerMap["vendor"] = "CLOUDWALKER"//getVendor()
        headerMap["factory"] = "CLOUDWALKER"//getFactory()


        headerMap["package"] = "tv.cloudwalker.cwnxt.launcher.com"//getPackageName()
        headerMap["features"] = "CLOUDTV_VOICE"//getFeatures()
        headerMap["serialno"] = ""//getSerialNo()

        headerMap["lversion"] = "project2"
        headerMap["appVersion"] = "project2"

        headerMap["uid"] = "0a8064587baced48c38280fef63011fc"
            //(context.getApplicationContext() as CloudwalkerApplication).getActiveProfileUID()
        headerMap["androidSDKVersion"] = "30"//CloudwalkerApplication.instance.getAndroidSDKVersion()
        headerMap["ocsNumber"] = "24095769"//getOcsNumber()
        headerMap["androidVersion"] = "11"//getAndroidVersion()

       //User-Agent: CLOUDTV_DEMO-20240628_203625/3.2.1-41-g9b8cb5e-new-dirty-cvte-com-dev
       //ram: 1G
        return headerMap
    }

}
