package tv.cloudwalker.cwnxt.launcher.repository.impl

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import tv.cloudwalker.cwnxt.launcher.core.Resource
import tv.cloudwalker.cwnxt.launcher.models.MovieResponse
import tv.cloudwalker.cwnxt.launcher.remote.ApiService
import tv.cloudwalker.cwnxt.launcher.repository.MovieRepository
import tv.cloudwalker.cwnxt.launcher.utils.NetworkConnectivity
import tv.cloudwalker.cwnxt.launcher.utils.getResponse
import javax.inject.Inject
import javax.inject.Singleton


@ExperimentalCoroutinesApi
@Singleton
class MovieRepositoryImpl @Inject internal constructor(
    private val authService: ApiService,
    private val networkConnectivity: NetworkConnectivity
) : MovieRepository {

    override fun getHomeScreenData(profileId:String): Flow<Resource<MovieResponse>> = flow {
        if (!networkConnectivity.isConnected()) {
            emit(Resource.error("You are offline. Connect to the Internet to access the app"))
            return@flow
        } else {
            val code = authService.getHomeScreenData(profileId).code()
            val authServiceResponse = authService.getHomeScreenData(profileId).getResponse()
            val state =
                if (authServiceResponse != null && code == 200) Resource.success(authServiceResponse) else Resource.error(
                    "Something went wrong"
                )
            emit(state)
        }
    }.catch { e -> emit(Resource.error("Something went wrong $e")) }

}