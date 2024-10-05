package tv.cloudwalker.cwnxt.launcher.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tv.cloudwalker.cwnxt.launcher.repository.AdRepository
import tv.cloudwalker.cwnxt.launcher.repository.MovieRepository
import tv.cloudwalker.cwnxt.launcher.repository.impl.AdRepositoryImpl
import tv.cloudwalker.cwnxt.launcher.repository.impl.MovieRepositoryImpl


@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Binds
    fun providesAuthRepository(authRepositoryImpl: MovieRepositoryImpl): MovieRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Binds
    fun provideAdService(adRepositoryImpl: AdRepositoryImpl): AdRepository

}
