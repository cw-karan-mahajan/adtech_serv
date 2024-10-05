package tv.cloudwalker.cwnxt.launcher.repository

import kotlinx.coroutines.flow.Flow
import tv.cloudwalker.cwnxt.launcher.core.Resource
import tv.cloudwalker.cwnxt.launcher.models.MovieResponse
import javax.inject.Singleton

@Singleton
interface MovieRepository {
    fun getHomeScreenData(profileId: String): Flow<Resource<MovieResponse>>
}

/*
class MainRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {
    suspend fun getMyData(): MyData2 {
        val inputStream = context.assets.open("data2.json")
        val json: String = inputStream.bufferedReader().use { it.readText() }
        inputStream.close()
        Log.d("MainRepository", "JSON read: ${json.take(300)}...") // Log first 300 chars
        return gson.fromJson(json, MyData2::class.java)
    }
}*/
