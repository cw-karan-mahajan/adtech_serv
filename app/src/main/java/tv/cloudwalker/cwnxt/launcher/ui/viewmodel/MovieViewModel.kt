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
import tv.cloudwalker.cwnxt.launcher.repository.MovieRepository
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _networkStatus = MutableLiveData<Boolean>()
    val networkStatus: LiveData<Boolean> = _networkStatus

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    var mRowsAdapter: ArrayObjectAdapter? = null

    fun getHomeScreenData(profileId: String? = "") {
        viewModelScope.launch {
            repository.getHomeScreenData(PROFILE_UID).distinctUntilChanged().collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _uiState.value = UiState.Success(response.data)
                    }
                    is Resource.Error -> {
                        _uiState.value = UiState.Error(response.message)
                        _toastMessage.postValue("Error loading data: ${response.message}")
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
}

sealed class UiState {
    object Loading : UiState()
    data class Success(val data: MovieResponse) : UiState()
    data class Error(val message: String) : UiState()
}