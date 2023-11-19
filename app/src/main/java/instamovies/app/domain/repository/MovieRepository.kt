package instamovies.app.domain.repository

import androidx.paging.PagingData
import instamovies.app.data.remote.dto.list.ListsDto
import instamovies.app.data.remote.dto.movie.MovieListsDto
import instamovies.app.data.remote.dto.movie.MovieResult
import instamovies.app.data.remote.dto.movie.details.MovieDetailsDto
import instamovies.app.data.remote.dto.person.details.PersonDetailsDto
import instamovies.app.data.remote.dto.person.popular.PersonResult
import instamovies.app.data.remote.dto.search.SearchDto
import instamovies.app.data.remote.dto.search.SearchResult
import instamovies.app.data.remote.dto.series.SeriesResult
import instamovies.app.data.remote.dto.series.details.SeriesDetailsDto
import instamovies.app.data.remote.dto.trending.TrendingDto
import instamovies.app.data.remote.dto.trending.person.TrendingPersonDto
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    // List

    suspend fun getList(listId: Int): ListsDto

    // Movie Lists

    suspend fun getNowPlayingMovies(
        language: String = "en-US",
        page: Int = 1,
        region: String? = null,
    ): Flow<PagingData<MovieResult>>

    suspend fun getPopularMovies(
        language: String = "en-US",
        page: Int = 1,
        region: String? = null,
    ): MovieListsDto

    suspend fun getPopularMoviesPaging(
        language: String = "en-US",
        page: Int = 1,
        region: String? = null,
    ): Flow<PagingData<MovieResult>>

    suspend fun getTopRatedMovies(
        language: String = "en-US",
        page: Int = 1,
        region: String? = null,
    ): Flow<PagingData<MovieResult>>

    suspend fun getUpcomingMovies(
        language: String = "en-US",
        page: Int = 1,
        region: String? = null,
    ): Flow<PagingData<MovieResult>>

    // Movies

    suspend fun getMovieDetails(
        movieId: Int,
        appendToResponse: String? = null,
        language: String = "en-US",
    ): MovieDetailsDto

    // People Lists

    suspend fun getPopularPerson(
        language: String = "en-US",
        page: Int = 1,
    ): Flow<PagingData<PersonResult>>

    // People

    suspend fun getPersonDetails(
        personId: Int,
        appendToResponse: String? = null,
        language: String = "en-US",
    ): PersonDetailsDto

    // Search

    suspend fun searchMulti(
        query: String,
        includeAdult: Boolean = false,
        language: String = "en-US",
        page: Int = 1,
    ): SearchDto

    suspend fun searchMultiPaging(
        query: String,
        includeAdult: Boolean = false,
        language: String = "en-US",
        page: Int = 1,
    ): Flow<PagingData<SearchResult>>

    // Trending

    suspend fun getTrending(
        timeWindow: TimeWindow = TimeWindow.day,
        language: String = "en-US",
    ): TrendingDto

    suspend fun getTrendingPerson(
        timeWindow: TimeWindow = TimeWindow.day,
        language: String = "en-US",
    ): TrendingPersonDto

    // TV Series Lists

    suspend fun getAiringTodaySeries(
        language: String = "en-US",
        page: Int = 1,
        timezone: String? = null,
    ): Flow<PagingData<SeriesResult>>

    suspend fun getOnTheAirSeries(
        language: String = "en-US",
        page: Int = 1,
        timezone: String? = null,
    ): Flow<PagingData<SeriesResult>>

    suspend fun getPopularSeries(
        language: String = "en-US",
        page: Int = 1,
    ): Flow<PagingData<SeriesResult>>

    suspend fun getTopRatedSeries(
        language: String = "en-US",
        page: Int = 1,
    ): Flow<PagingData<SeriesResult>>

    // TV Series

    suspend fun getSeriesDetails(
        seriesId: Int,
        appendToResponse: String? = null,
        language: String = "en-US",
    ): SeriesDetailsDto
}

enum class TimeWindow {
    day,
    week,
}
