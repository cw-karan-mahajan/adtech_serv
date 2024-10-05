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
                            val adImageUrl = extractAdImageUrl(resource.data)
                            tile.adImageUrl = adImageUrl
                        }

                        is Resource.Error -> {
                            Timber.e("Failed to fetch ad: ${resource.message}")
                        }
                    }
                }
            }
        }
    }

    private fun extractAdImageUrl(adResponse: AdResponse): String? {
        return adResponse.seatbid?.firstOrNull()?.bid?.firstOrNull()?.let { bid ->
            val nativeAdWrapper = gson.fromJson(bid.adm, NativeAdWrapper::class.java)
            nativeAdWrapper.native?.assets?.firstOrNull { it.img != null }?.img?.url
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

    fun onTileSelected(tile: MovieTile) {
        // Handle tile selection
    }

    fun refreshContent() {
        // Refresh the content
        getHomeScreenData()
    }

    override fun onCleared() {
        super.onCleared()
        // Cancel any ongoing coroutines or cleanup resources
    }
}

sealed class UiState {
    object Loading : UiState()
    data class Success(val data: MovieResponse) : UiState()
    data class Error(val message: String) : UiState()
}