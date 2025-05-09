package instamovies.app.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import instamovies.app.core.Constants
import instamovies.app.data.remote.MovieApi
import instamovies.app.data.repository.MovieRepositoryImpl
import instamovies.app.data.repository.UserPreferencesRepositoryImpl
import instamovies.app.domain.repository.MovieRepository
import instamovies.app.domain.repository.UserPreferencesRepository
import instamovies.app.domain.repository.dataStore
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideTheMovieApi(): MovieApi =
        Retrofit
            .Builder()
            .baseUrl(Constants.TMDB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApi::class.java)

    @Singleton
    @Provides
    fun provideDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.dataStore

    @Singleton
    @Provides
    fun provideMovieRepository(movieApi: MovieApi): MovieRepository = MovieRepositoryImpl(movieApi)

    @Singleton
    @Provides
    fun provideUserPreferencesRepository(dataStore: DataStore<Preferences>): UserPreferencesRepository =
        UserPreferencesRepositoryImpl(dataStore)
}
