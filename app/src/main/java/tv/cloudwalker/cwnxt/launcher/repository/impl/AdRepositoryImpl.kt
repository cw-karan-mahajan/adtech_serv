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
import javax.inject.Singleton

@Singleton
class AdRepositoryImpl @Inject constructor(
    private val dynamicApiServiceFactory: DynamicApiServiceFactory,
    private val networkConnectivity: NetworkConnectivity
) : AdRepository {

    override fun fetchAd(adsServerUrl: String): Flow<Resource<AdResponse>> = flow {
        if (!networkConnectivity.isConnected()) {
            emit(Resource.error("No internet connection"))
            return@flow
        }
        try {
            val adApiService = dynamicApiServiceFactory.createService(AdApiService::class.java, adsServerUrl)
            val path = dynamicApiServiceFactory.extractPath(adsServerUrl)
            val queryParams = dynamicApiServiceFactory.extractQueryParams(adsServerUrl)

            val response = adApiService.getAd(path, queryParams)
            val adResponse = response.getResponse()
            val code = response.code()

            if (adResponse != null && code == 200) {
                emit(Resource.success(adResponse))
            } else {
                emit(Resource.error("Failed to fetch ad: $code"))
            }
        } catch (e: Exception) {
            emit(Resource.error("Error fetching ad: ${e.message}"))
        }
    }

    override suspend fun trackImpression(impTrackerUrl: String): Resource<Unit> {
        if (!networkConnectivity.isConnected()) {
            return Resource.error("No internet connection")
        }
        return try {
            val adApiService = dynamicApiServiceFactory.createService(AdApiService::class.java, impTrackerUrl)
            val path = dynamicApiServiceFactory.extractPath(impTrackerUrl)
            val queryParams = dynamicApiServiceFactory.extractQueryParams(impTrackerUrl)

            val response = adApiService.trackImpression(path, queryParams)
            if (response.isSuccessful || response.code() == 200) {
                Resource.success(Unit)
            } else {
                Resource.error("Failed to track impression: ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.error("Error tracking impression: ${e.message}")
        }
    }

    override suspend fun trackClick(clickTrackerUrl: String): Resource<Unit> {
        if (!networkConnectivity.isConnected()) {
            return Resource.error("No internet connection")
        }
        return try {
            val adApiService = dynamicApiServiceFactory.createService(AdApiService::class.java, clickTrackerUrl)
            val path = dynamicApiServiceFactory.extractPath(clickTrackerUrl)
            val queryParams = dynamicApiServiceFactory.extractQueryParams(clickTrackerUrl)

            val response = adApiService.trackClick(path, queryParams)
            if (response.isSuccessful || response.code() == 200) {
                Resource.success(Unit)
            } else {
                Resource.error("Failed to track click: ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.error("Error tracking click: ${e.message}")
        }
    }
}