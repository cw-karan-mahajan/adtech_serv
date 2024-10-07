package tv.cloudwalker.cwnxt.launcher.ui.fragment

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.FocusHighlight
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.util.UnstableApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import tv.cloudwalker.cwnxt.launcher.R
import tv.cloudwalker.cwnxt.launcher.models.MovieResponse
import tv.cloudwalker.cwnxt.launcher.ui.presenters.DynamicPresenterSelector
import tv.cloudwalker.cwnxt.launcher.ui.viewmodel.MovieViewModel
import tv.cloudwalker.cwnxt.launcher.ui.viewmodel.UiState
import tv.cloudwalker.cwnxt.launcher.utils.NetworkChangeReceiver
import tv.cloudwalker.cwnxt.launcher.utils.isConnected

@UnstableApi
@AndroidEntryPoint
class LauncherFragment : BrowseSupportFragment(), isConnected {

    private val viewModel: MovieViewModel by viewModels()
    private var networkChangeReceiver: NetworkChangeReceiver? = null
    private lateinit var rowsAdapter: ArrayObjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkChangeReceiver = NetworkChangeReceiver(this)
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        requireActivity().registerReceiver(networkChangeReceiver, filter)
        setUI()
    }

    private fun setUI() {
        title = "CloudTV"
        headersState = HEADERS_DISABLED
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        //setupBackPressHandler()
        //view.post { setupScrollListener(view) }
    }


    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is UiState.Loading -> showProgressBar()
                        is UiState.Success -> {
                            hideProgressBar()
                            populateRows(state.data)
                            viewModel.mRowsAdapter = rowsAdapter
                            setupEventListeners()
                        }

                        is UiState.Error -> {
                            hideProgressBar()
                            Toast.makeText(context, "Error: ${state.message}", Toast.LENGTH_LONG)
                                .show()
                        }
                        else -> {
                            hideProgressBar()
                        }
                    }
                }
            }
        }


        viewModel.networkStatus.observe(viewLifecycleOwner) { isConnected ->
            //updateNetworkUI(isConnected)
        }

        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

    }

    private fun populateRows(data: MovieResponse) {
        val lrp = ListRowPresenter(FocusHighlight.ZOOM_FACTOR_NONE, false).apply {
            shadowEnabled = false
            selectEffectEnabled = false
        }
        showProgressBar()
        if (!::rowsAdapter.isInitialized) {
            rowsAdapter = ArrayObjectAdapter(lrp)
        } else {
            rowsAdapter.clear()
        }
        val dynamicPresenter = createCardLayout()

        data.rows.forEachIndexed { index, row ->
            val headerItem = HeaderItem(index.toLong(), row.rowHeader)
            val listRowAdapter = ArrayObjectAdapter(dynamicPresenter)

            row.rowItems.forEach { tile ->
                tile.rowLayout = row.rowLayout
                tile.rowHeader = row.rowHeader
                tile.rowAdConfig = row.rowAdConfig
                listRowAdapter.add(tile)
            }
            if (row.rowAdConfig != null && row.rowAdConfig.rowAdType == "typeAdsBanner") {
                rowsAdapter.add(ListRow(listRowAdapter))
            } else {
                rowsAdapter.add(ListRow(headerItem, listRowAdapter))
            }
        }
        adapter = rowsAdapter
    }

    private fun createCardLayout(): DynamicPresenterSelector {
        return DynamicPresenterSelector(viewModel)
    }

    private fun showProgressBar() {
        requireActivity().findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        requireActivity().findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE
    }

    override fun connected() {
        viewModel.setNetworkStatus(true)
        refreshContent()
    }

    override fun notconnected() {
        viewModel.setNetworkStatus(false)
    }

    private fun refreshContent() {
        viewLifecycleOwner.lifecycleScope.launch {
            if (::rowsAdapter.isInitialized) {
                rowsAdapter.clear() // Clear existing items
            }
            adapter = null // Remove the adapter from the fragment
            viewModel.clearData() // Clear data in ViewModel
            viewModel.getHomeScreenData() // Fetch new data
        }
    }

    private fun setupEventListeners() {
        setOnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->

        }
    }
}