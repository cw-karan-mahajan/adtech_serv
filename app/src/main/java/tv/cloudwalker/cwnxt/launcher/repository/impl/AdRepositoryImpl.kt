package tv.cloudwalker.cwnxt.launcher.repository.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tv.cloudwalker.cwnxt.launcher.core.Resource
import tv.cloudwalker.cwnxt.launcher.models.AdResponse
import tv.cloudwalker.cwnxt.launcher.remote.AdApiService
import tv.cloudwalker.cwnxt.launcher.remote.DynamicApiServiceFactory
import tv.cloudwalker.cwnxt.launcher.repository.AdRepository
import tv.cloudwalker.cwnxt.launcher.utils.NetworkConnectivity
import tv.cloudwalker.cwnxt.launcher.utils.getResponse
import javax.inject.Inject

class AdRepositoryImpl @Inject constructor(
    private val dynamicApiServiceFactory: DynamicApiServiceFactory,
    private val networkConnectivity: NetworkConnectivity
) : AdRepository {

    override suspend fun fetchAd(adsServerUrl: String): Flow<Resource<AdResponse>> = flow {
        if (!networkConnectivity.isConnected()) {
            emit(Resource.error("No internet connection"))
            return@flow
        }
        try {
            val adApiService = dynamicApiServiceFactory.createService(AdApiService::class.java, adsServerUrl)
            val path = dynamicApiServiceFactory.extractPath(adsServerUrl)
            val queryParams = dynamicApiServiceFactory.extractQueryParams(adsServerUrl)

            val response = adApiService.getAd(path, queryParams).getResponse()
            val code = adApiService.getAd(path, queryParams).code()
            if (response!= null && code == 200) {
                emit(Resource.success(response))
            } else {
                emit(Resource.error("Failed to fetch ad: $code"))
            }
        } catch (e: Exception) {
            emit(Resource.error("Error fetching ad: ${e.message}"))
        }
    }
}