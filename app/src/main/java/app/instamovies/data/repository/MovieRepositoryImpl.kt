package app.instamovies.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.instamovies.data.remote.MovieApi
import app.instamovies.data.remote.dto.list.ListsDto
import app.instamovies.data.remote.dto.movie.MovieListsDto
import app.instamovies.data.remote.dto.movie.MovieResult
import app.instamovies.data.remote.dto.movie.details.MovieDetailsDto
import app.instamovies.data.remote.dto.person.details.PersonDetailsDto
import app.instamovies.data.remote.dto.person.popular.PersonResult
import app.instamovies.data.remote.dto.search.SearchDto
import app.instamovies.data.remote.dto.search.SearchResult
import app.instamovies.data.remote.dto.series.SeriesResult
import app.instamovies.data.remote.dto.series.details.SeriesDetailsDto
import app.instamovies.data.remote.dto.trending.TrendingDto
import app.instamovies.data.remote.dto.trending.person.TrendingPersonDto
import app.instamovies.data.remote.paging.AiringTodaySeriesPagingSource
import app.instamovies.data.remote.paging.NowPlayingMoviesPagingSource
import app.instamovies.data.remote.paging.OnTheAirSeriesPagingSource
import app.instamovies.data.remote.paging.PopularMoviesPagingSource
import app.instamovies.data.remote.paging.PopularPersonPagingSource
import app.instamovies.data.remote.paging.PopularSeriesPagingSource
import app.instamovies.data.remote.paging.SearchMultiPagingSource
import app.instamovies.data.remote.paging.TopRatedMoviesPagingSource
import app.instamovies.data.remote.paging.TopRatedSeriesPagingSource
import app.instamovies.data.remote.paging.UpcomingMoviesPagingSource
import app.instamovies.domain.repository.MovieRepository
import app.instamovies.domain.repository.TimeWindow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl
    @Inject
    constructor(
        private val movieApi: MovieApi,
    ) : MovieRepository {
        override suspend fun getList(listId: Int): ListsDto = movieApi.getList(listId)

        override suspend fun getNowPlayingMovies(
            language: String,
            page: Int,
            region: String?,
        ): Flow<PagingData<MovieResult>> =
            Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
                pagingSourceFactory = {
                    NowPlayingMoviesPagingSource(movieApi, language, page, region)
                },
            ).flow

        override suspend fun getPopularMovies(
            language: String,
            page: Int,
            region: String?,
        ): MovieListsDto = movieApi.getPopularMovies(language, page, region)

        override suspend fun getPopularMoviesPaging(
            language: String,
            page: Int,
            region: String?,
        ): Flow<PagingData<MovieResult>> =
            Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
                pagingSourceFactory = {
                    PopularMoviesPagingSource(movieApi, language, page, region)
                },
            ).flow

        override suspend fun getTopRatedMovies(
            language: String,
            page: Int,
            region: String?,
        ): Flow<PagingData<MovieResult>> =
            Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
                pagingSourceFactory = {
                    TopRatedMoviesPagingSource(movieApi, language, page, region)
                },
            ).flow

        override suspend fun getUpcomingMovies(
            language: String,
            page: Int,
            region: String?,
        ): Flow<PagingData<MovieResult>> =
            Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
                pagingSourceFactory = {
                    UpcomingMoviesPagingSource(movieApi, language, page, region)
                },
            ).flow

        override suspend fun getMovieDetails(
            movieId: Int,
            appendToResponse: String?,
            language: String,
        ): MovieDetailsDto = movieApi.getMovieDetails(movieId, appendToResponse, language)

        override suspend fun getPopularPerson(
            language: String,
            page: Int,
        ): Flow<PagingData<PersonResult>> =
            Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
                pagingSourceFactory = {
                    PopularPersonPagingSource(movieApi, language, page)
                },
            ).flow

        override suspend fun getPersonDetails(
            personId: Int,
            appendToResponse: String?,
            language: String,
        ): PersonDetailsDto = movieApi.getPersonDetails(personId, appendToResponse, language)

        override suspend fun searchMulti(
            query: String,
            includeAdult: Boolean,
            language: String,
            page: Int,
        ): SearchDto = movieApi.searchMulti(query, includeAdult, language, page)

        override suspend fun searchMultiPaging(
            query: String,
            includeAdult: Boolean,
            language: String,
            page: Int,
        ): Flow<PagingData<SearchResult>> =
            Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
                pagingSourceFactory = {
                    SearchMultiPagingSource(movieApi, query, includeAdult, language, page)
                },
            ).flow

        override suspend fun getTrending(
            timeWindow: TimeWindow,
            language: String,
        ): TrendingDto = movieApi.getTrending(timeWindow, language)

        override suspend fun getTrendingPerson(
            timeWindow: TimeWindow,
            language: String,
        ): TrendingPersonDto = movieApi.getTrendingPerson(timeWindow, language)

        override suspend fun getAiringTodaySeries(
            language: String,
            page: Int,
            timezone: String?,
        ): Flow<PagingData<SeriesResult>> =
            Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
                pagingSourceFactory = {
                    AiringTodaySeriesPagingSource(movieApi, language, page, timezone)
                },
            ).flow

        override suspend fun getOnTheAirSeries(
            language: String,
            page: Int,
            timezone: String?,
        ): Flow<PagingData<SeriesResult>> =
            Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
                pagingSourceFactory = {
                    OnTheAirSeriesPagingSource(movieApi, language, page, timezone)
                },
            ).flow

        override suspend fun getPopularSeries(
            language: String,
            page: Int,
        ): Flow<PagingData<SeriesResult>> =
            Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
                pagingSourceFactory = {
                    PopularSeriesPagingSource(movieApi, language, page)
                },
            ).flow

        override suspend fun getTopRatedSeries(
            language: String,
            page: Int,
        ): Flow<PagingData<SeriesResult>> =
            Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
                pagingSourceFactory = {
                    TopRatedSeriesPagingSource(movieApi, language, page)
                },
            ).flow

        override suspend fun getSeriesDetails(
            seriesId: Int,
            appendToResponse: String?,
            language: String,
        ): SeriesDetailsDto = movieApi.getSeriesDetails(seriesId, appendToResponse, language)

        companion object {
            const val NETWORK_PAGE_SIZE = 20
        }
    }
