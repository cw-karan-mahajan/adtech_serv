package tv.cloudwalker.cwnxt.launcher.utils

import com.squareup.otto.Bus
import com.squareup.otto.ThreadEnforcer

object OttoBus {
    private var mBus: Bus? = null

    val bus: Bus?
        get() {
            if (mBus == null) {
                mBus = Bus(ThreadEnforcer.ANY)
            }
            return mBus
        }
}
