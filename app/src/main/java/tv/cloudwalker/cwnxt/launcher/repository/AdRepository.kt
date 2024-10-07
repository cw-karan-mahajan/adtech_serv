package tv.cloudwalker.cwnxt.launcher.repository

import kotlinx.coroutines.flow.Flow
import tv.cloudwalker.cwnxt.launcher.core.Resource
import tv.cloudwalker.cwnxt.launcher.models.AdResponse

interface AdRepository {
   fun fetchAd(adsServerUrl: String): Flow<Resource<AdResponse>>
   suspend fun trackImpression(impTrackerUrl: String): Resource<Unit>
   suspend fun trackClick(clickTrackerUrl: String): Resource<Unit>
}