package tv.cloudwalker.cwnxt.launcher.application

import android.app.Application
import androidx.multidex.MultiDex

class CloudwalkerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
    }
}