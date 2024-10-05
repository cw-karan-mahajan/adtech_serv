package tv.cloudwalker.cwnxt.launcher.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import tv.cloudwalker.cwnxt.launcher.core.Constants.BASE_URL
import tv.cloudwalker.cwnxt.launcher.remote.ApiService
import tv.cloudwalker.cwnxt.launcher.remote.DynamicApiServiceFactory
import tv.cloudwalker.cwnxt.launcher.remote.HeaderInterceptor
import tv.cloudwalker.cwnxt.launcher.utils.Network
import tv.cloudwalker.cwnxt.launcher.utils.NetworkConnectivity
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideNetworkConnectivity(@ApplicationContext context: Context): NetworkConnectivity {
        return Network(context)
    }

    @Provides
    @Singleton
    fun provideDynamicApiServiceFactory(
        retrofitBuilder: Retrofit.Builder,
        okHttpClientBuilder: OkHttpClient.Builder
    ): DynamicApiServiceFactory {
        return DynamicApiServiceFactory(retrofitBuilder, okHttpClientBuilder)
    }

    @Provides
    @Singleton
    fun provideHeaderInterceptor(): HeaderInterceptor {
        return HeaderInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .hostnameVerifier { _, _ -> true }
            .addInterceptor(loggingInterceptor)
    }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Provides
    @Singleton
    fun provideApiService(retrofitBuilder: Retrofit.Builder, okHttpClientBuilder: OkHttpClient.Builder,
                          headerInterceptor: HeaderInterceptor): ApiService {
        val client = okHttpClientBuilder
            .addInterceptor(headerInterceptor)
            .build()
        return retrofitBuilder
            .baseUrl(BASE_URL)
            .client(client)
            .build()
            .create(ApiService::class.java)
    }


   /* @UnstableApi
    @Provides
    @Singleton
    fun provideExoPlayerManager(
        @ApplicationContext context: Context
    ): ExoPlayerManager {
        return ExoPlayerManager(context)
    }*/
}