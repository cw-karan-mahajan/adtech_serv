package tv.cloudwalker.cwnxt.launcher.ui.activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import tv.cloudwalker.cwnxt.launcher.ui.fragment.LauncherFragment
import tv.cloudwalker.cwnxt.launcher.R

/**
 * Loads [LauncherFragment].
 */
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_browse_fragment, LauncherFragment())
                .commitNow()
        }
    }
}