package tv.cloudwalker.cwnxt.launcher.ui.viewmodel

import androidx.lifecycle.*
import androidx.leanback.widget.*
import androidx.media3.common.util.UnstableApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import tv.cloudwalker.cwnxt.launcher.core.Constants.PROFILE_UID
import tv.cloudwalker.cwnxt.launcher.core.Resource
import tv.cloudwalker.cwnxt.launcher.models.MovieResponse
import tv.cloudwalker.cwnxt.launcher.models.MovieTile
import tv.cloudwalker.cwnxt.launcher.repository.MovieRepository
import tv.cloudwalker.cwnxt.launcher.repository.AdRepository
import tv.cloudwalker.cwnxt.launcher.models.AdResponse
import tv.cloudwalker.cwnxt.launcher.models.NativeAdWrapper
import com.google.gson.Gson
import timber.log.Timber
import java.util.Collections
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val adRepository: AdRepository,
    private val gson: Gson
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _networkStatus = MutableLiveData<Boolean>()
    val networkStatus: LiveData<Boolean> = _networkStatus

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    var mRowsAdapter: ArrayObjectAdapter? = null

    private val _trackedImpressionTileIds = Collections.synchronizedSet(mutableSetOf<String>())
    private val _pendingImpressions = Collections.synchronizedList(mutableListOf<Pair<String, String>>())
    private var impressionTrackingJob: Job? = null

    fun getHomeScreenData(profileId: String = "") {
        viewModelScope.launch {
            repository.getHomeScreenData(PROFILE_UID).distinctUntilChanged().collect { response ->
                when (response) {
                    is Resource.Success -> {
                        val movieResponse = response.data
                        fetchAdsForTiles(movieResponse.rows.flatMap { it.rowItems })
                        _uiState.value = UiState.Success(movieResponse)
                    }
                    is Resource.Error -> {
                        _uiState.value = UiState.Error(response.message)
                        _toastMessage.postValue("Error loading data: ${response.message}")
                    }
                }
            }
        }
    }

    private suspend fun fetchAdsForTiles(tiles: List<MovieTile>) {
        tiles.forEach { tile ->
            tile.ads_server?.let { adsServerUrl ->
                adRepository.fetchAd(adsServerUrl).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            updateTileWithAdData(tile, resource.data)
                        }
                        is Resource.Error -> {
                            Timber.e("Failed to fetch ad: ${resource.message}")
                        }
                    }
                }
            }
        }
    }

    private fun updateTileWithAdData(tile: MovieTile, adResponse: AdResponse) {
        adResponse.seatbid?.firstOrNull()?.bid?.firstOrNull()?.let { bid ->
            val nativeAdWrapper = gson.fromJson(bid.adm, NativeAdWrapper::class.java)
            tile.adImageUrl = nativeAdWrapper.native?.assets?.firstOrNull { it.img != null }?.img?.url
            tile.clickTrackers = nativeAdWrapper.native?.link?.clicktrackers ?: emptyList()
            tile.impressionTrackers = nativeAdWrapper.native?.imptrackers ?: emptyList()
        }
    }

    fun onTileFocused(tile: MovieTile) {
        if (_trackedImpressionTileIds.add(tile.tid)) {
            tile.impressionTrackers?.forEach { impTrackerUrl ->
                _pendingImpressions.add(tile.tid to impTrackerUrl)
            }
            if (tile.impressionTrackers?.isNotEmpty() == true) {
                scheduleImpressionTracking()
            }
        }
    }

    private fun scheduleImpressionTracking() {
        if (impressionTrackingJob?.isActive != true) {
            impressionTrackingJob = viewModelScope.launch {
                delay(100) // Small delay to batch impressions
                trackPendingImpressions()
            }
        }
    }

    private suspend fun trackPendingImpressions() {
        val impressions = _pendingImpressions.toList()
        _pendingImpressions.clear()

        impressions.forEach { (tileId, impTrackerUrl) ->
            when (val result = adRepository.trackImpression(impTrackerUrl)) {
                is Resource.Success -> {
                    Timber.d("Impression tracked successfully for tile: $tileId")
                }
                is Resource.Error -> {
                    Timber.e("Failed to track impression for tile: $tileId. Error: ${result.message}")
                    _pendingImpressions.add(tileId to impTrackerUrl)
                }
            }
        }
    }

    fun onTileClicked(tile: MovieTile) {
        tile.clickTrackers?.forEach { clickTrackerUrl ->
            viewModelScope.launch {
                when (val result = adRepository.trackClick(clickTrackerUrl)) {
                    is Resource.Success -> {
                        Timber.d("Click tracked successfully for tile: ${tile.tid}")
                    }
                    is Resource.Error -> {
                        Timber.e("Failed to track click for tile: ${tile.tid}. Error: ${result.message}")
                    }
                }
            }
        }
    }

    fun setNetworkStatus(isConnected: Boolean) {
        if (isConnected) {
            _toastMessage.value = "Network connected"
        } else {
            _toastMessage.value = "Network disconnected"
        }
        _networkStatus.value = isConnected
    }

    fun refreshContent() {
        getHomeScreenData()
    }

    fun clearData() {
        _uiState.value = UiState.Loading
        mRowsAdapter?.clear()
        _trackedImpressionTileIds.clear()
        _pendingImpressions.clear()
    }

    override fun onCleared() {
        super.onCleared()
        impressionTrackingJob?.cancel()
        // Cancel any other ongoing coroutines or cleanup resources
    }
}

sealed class UiState {
    object Loading : UiState()
    data class Success(val data: MovieResponse) : UiState()
    data class Error(val message: String) : UiState()
}