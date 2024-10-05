package tv.cloudwalker.cwnxt.launcher.ui.activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import dagger.hilt.android.AndroidEntryPoint
import tv.cloudwalker.cwnxt.launcher.R
import tv.cloudwalker.cwnxt.launcher.ui.fragment.LauncherFragment


@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //clearAllTables();
        val fragmentManager = supportFragmentManager
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
       val launcherFragment = LauncherFragment()
        fragmentManager
            .beginTransaction()
            .add(R.id.main_browse_frame, launcherFragment, LauncherFragment::class.java.simpleName)
            .commit()
    }
}