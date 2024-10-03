package tv.cloudwalker.cwnxt.launcher.ui.presenters

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.leanback.widget.TitleViewAdapter
import tv.cloudwalker.cwnxt.launcher.R
import tv.cloudwalker.cwnxt.launcher.databinding.MainCustomTitleviewBinding

class CustomTitleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), TitleViewAdapter.Provider {

    private val binding: MainCustomTitleviewBinding = MainCustomTitleviewBinding.inflate(LayoutInflater.from(context), this)

    private val titleViewAdapter = object : TitleViewAdapter() {
        override fun getSearchAffordanceView() = null
    }

    private val logoMap = mapOf(
        R.id.profile_orb to binding.profileOrb,
        R.id.search_orb to binding.searchOrb,
        R.id.network_orb to binding.networkOrb,
        R.id.source_orb to binding.sourceOrb,
        R.id.settings_orb to binding.settingsOrb,
        R.id.apps_orb to binding.appsOrb
    )

    init {
        setupClickListeners()
        setupKeyListeners()
    }

    private fun setupClickListeners() {
        logoMap.forEach { (id, view) ->
            view.setOnClickListener {
                when (id) {
                    R.id.settings_orb -> launchSettings(Settings.ACTION_SETTINGS)
                    R.id.network_orb -> launchSettings(Settings.ACTION_WIFI_SETTINGS)
                    else -> showToast("You Clicked ${resources.getResourceEntryName(id)}")
                }
            }
        }
    }

    private fun setupKeyListeners() {
        val keyOrder = listOf(R.id.profile_orb, R.id.search_orb, R.id.network_orb, R.id.source_orb,
            R.id.settings_orb, R.id.apps_orb)
        keyOrder.forEachIndexed { index, id ->
            logoMap[id]?.setOnKeyListener { _, keyCode, event ->
                when {
                    keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.action == KeyEvent.ACTION_DOWN -> {
                        logoMap[keyOrder[(index + 1) % keyOrder.size]]?.requestFocus()
                        true
                    }
                    keyCode == KeyEvent.KEYCODE_DPAD_UP && event.action == KeyEvent.ACTION_DOWN -> {
                        logoMap[id]?.requestFocus()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun launchSettings(action: String) {
        showToast("You Clicked ${action.substringAfterLast('.')}")
        context.startActivity(Intent(action))
    }

    override fun getTitleViewAdapter(): TitleViewAdapter = titleViewAdapter
}