package instamovies.app.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import instamovies.app.data.remote.MovieApi
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
import instamovies.app.data.remote.paging.AiringTodaySeriesPagingSource
import instamovies.app.data.remote.paging.NowPlayingMoviesPagingSource
import instamovies.app.data.remote.paging.OnTheAirSeriesPagingSource
import instamovies.app.data.remote.paging.PopularMoviesPagingSource
import instamovies.app.data.remote.paging.PopularPersonPagingSource
import instamovies.app.data.remote.paging.PopularSeriesPagingSource
import instamovies.app.data.remote.paging.SearchMultiPagingSource
import instamovies.app.data.remote.paging.TopRatedMoviesPagingSource
import instamovies.app.data.remote.paging.TopRatedSeriesPagingSource
import instamovies.app.data.remote.paging.UpcomingMoviesPagingSource
import instamovies.app.domain.repository.MovieRepository
import instamovies.app.domain.repository.TimeWindow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl
    @Inject
    constructor(
        private val movieApi: MovieApi,
    ) : MovieRepository {
        override suspend fun getList(listId: Int): ListsDto {
            return movieApi.getList(listId)
        }

        override suspend fun getNowPlayingMovies(
            language: String,
            page: Int,
            region: String?,
        ): Flow<PagingData<MovieResult>> {
            return Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
                pagingSourceFactory = {
                    NowPlayingMoviesPagingSource(movieApi, language, page, region)
                },
            ).flow
        }

        override suspend fun getPopularMovies(
            language: String,
            page: Int,
            region: String?,
        ): MovieListsDto {
            return movieApi.getPopularMovies(language, page, region)
        }

        override suspend fun getPopularMoviesPaging(
            language: String,
            page: Int,
            region: String?,
        ): Flow<PagingData<MovieResult>> {
            return Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
                pagingSourceFactory = {
                    PopularMoviesPagingSource(movieApi, language, page, region)
                },
            ).flow
        }

        override suspend fun getTopRatedMovies(
            language: String,
            page: Int,
            region: String?,
        ): Flow<PagingData<MovieResult>> {
            return Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
                pagingSourceFactory = {
                    TopRatedMoviesPagingSource(movieApi, language, page, region)
                },
            ).flow
        }

        override suspend fun getUpcomingMovies(
            language: String,
            page: Int,
            region: String?,
        ): Flow<PagingData<MovieResult>> {
            return Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
                pagingSourceFactory = {
                    UpcomingMoviesPagingSource(movieApi, language, page, region)
                },
            ).flow
        }

        override suspend fun getMovieDetails(
            movieId: Int,
            appendToResponse: String?,
            language: String,
        ): MovieDetailsDto {
            return movieApi.getMovieDetails(movieId, appendToResponse, language)
        }

        override suspend fun getPopularPerson(
            language: String,
            page: Int,
        ): Flow<PagingData<PersonResult>> {
            return Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
                pagingSourceFactory = {
                    PopularPersonPagingSource(movieApi, language, page)
                },
            ).flow
        }

        override suspend fun getPersonDetails(
            personId: Int,
            appendToResponse: String?,
            language: String,
        ): PersonDetailsDto {
            return movieApi.getPersonDetails(personId, appendToResponse, language)
        }

        override suspend fun searchMulti(
            query: String,
            includeAdult: Boolean,
            language: String,
            page: Int,
        ): SearchDto {
            return movieApi.searchMulti(query, includeAdult, language, page)
        }

        override suspend fun searchMultiPaging(
            query: String,
            includeAdult: Boolean,
            language: String,
            page: Int,
        ): Flow<PagingData<SearchResult>> {
            return Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
                pagingSourceFactory = {
                    SearchMultiPagingSource(movieApi, query, includeAdult, language, page)
                },
            ).flow
        }

        override suspend fun getTrending(
            timeWindow: TimeWindow,
            language: String,
        ): TrendingDto {
            return movieApi.getTrending(timeWindow, language)
        }

        override suspend fun getTrendingPerson(
            timeWindow: TimeWindow,
            language: String,
        ): TrendingPersonDto {
            return movieApi.getTrendingPerson(timeWindow, language)
        }

        override suspend fun getAiringTodaySeries(
            language: String,
            page: Int,
            timezone: String?,
        ): Flow<PagingData<SeriesResult>> {
            return Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
                pagingSourceFactory = {
                    AiringTodaySeriesPagingSource(movieApi, language, page, timezone)
                },
            ).flow
        }

        override suspend fun getOnTheAirSeries(
            language: String,
            page: Int,
            timezone: String?,
        ): Flow<PagingData<SeriesResult>> {
            return Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
                pagingSourceFactory = {
                    OnTheAirSeriesPagingSource(movieApi, language, page, timezone)
                },
            ).flow
        }

        override suspend fun getPopularSeries(
            language: String,
            page: Int,
        ): Flow<PagingData<SeriesResult>> {
            return Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
                pagingSourceFactory = {
                    PopularSeriesPagingSource(movieApi, language, page)
                },
            ).flow
        }

        override suspend fun getTopRatedSeries(
            language: String,
            page: Int,
        ): Flow<PagingData<SeriesResult>> {
            return Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
                pagingSourceFactory = {
                    TopRatedSeriesPagingSource(movieApi, language, page)
                },
            ).flow
        }

        override suspend fun getSeriesDetails(
            seriesId: Int,
            appendToResponse: String?,
            language: String,
        ): SeriesDetailsDto {
            return movieApi.getSeriesDetails(seriesId, appendToResponse, language)
        }

        companion object {
            const val NETWORK_PAGE_SIZE = 20
        }
    }
