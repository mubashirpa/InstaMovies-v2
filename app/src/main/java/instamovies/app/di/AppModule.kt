package instamovies.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import instamovies.app.core.Constants
import instamovies.app.data.remote.MovieApi
import instamovies.app.data.repository.MovieRepositoryImpl
import instamovies.app.domain.repository.MovieRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideTheMovieApi(): MovieApi {
        return Retrofit.Builder()
            .baseUrl(Constants.TMDB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMovieRepository(movieApi: MovieApi): MovieRepository = MovieRepositoryImpl(movieApi)
}
