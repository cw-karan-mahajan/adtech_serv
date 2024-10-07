package tv.cloudwalker.cwnxt.launcher.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.QueryMap
import retrofit2.http.Url
import tv.cloudwalker.cwnxt.launcher.models.AdResponse
import tv.cloudwalker.cwnxt.launcher.models.MovieResponse

interface ApiService {

    @Headers("Accept-Version: 3.0.0")
    @GET("cats/{profileId}")
    suspend fun getHomeScreenData(@Path("profileId") profileId: String) : Response<MovieResponse?>
}


interface AdApiService {
    @GET
    suspend fun getAd(@Url path: String, @QueryMap queryParams: Map<String, String>): Response<AdResponse>

    @GET
    suspend fun trackImpression(@Url path: String, @QueryMap queryParams: Map<String, String>): Response<Unit>

    @GET
    suspend fun trackClick(@Url path: String, @QueryMap queryParams: Map<String, String>): Response<Unit>
}
