package tv.cloudwalker.cwnxt.launcher.repository

import retrofit2.Response
import tv.cloudwalker.cwnxt.launcher.core.Resource
import tv.cloudwalker.cwnxt.launcher.models.AdResponse

interface AdRepository {
    suspend fun fetchAds(adUrls: List<String>): List<Pair<String, Resource<AdResponse>>>
    suspend fun trackImpressions(impressions: List<Pair<String, String>>): List<Pair<String, Resource<Unit>>>
    fun getImpressionTrackerUrls(adsServerUrl: String): List<String>
}