package instamovies.app.data.remote

import instamovies.app.BuildConfig
import instamovies.app.data.remote.dto.list.ListsDto
import instamovies.app.data.remote.dto.movie.MovieListsDto
import instamovies.app.data.remote.dto.movie.MovieListsRangeDto
import instamovies.app.data.remote.dto.movie.details.MovieDetailsDto
import instamovies.app.data.remote.dto.person.details.PersonDetailsDto
import instamovies.app.data.remote.dto.person.popular.PopularPersonDto
import instamovies.app.data.remote.dto.search.SearchDto
import instamovies.app.data.remote.dto.series.SeriesListsDto
import instamovies.app.data.remote.dto.series.details.SeriesDetailsDto
import instamovies.app.data.remote.dto.trending.TrendingDto
import instamovies.app.data.remote.dto.trending.person.TrendingPersonDto
import instamovies.app.domain.repository.TimeWindow
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    // List

    @Headers(
        "accept: application/json",
        "Authorization: Bearer ${BuildConfig.TMDB_API_TOKEN}",
    )
    @GET("4/list/{list_id}")
    suspend fun getList(
        @Path("list_id") listId: Int,
    ): ListsDto

    // Movie Lists

    /**
     * Get a list of movies that are currently in theatres.
     */
    @Headers(
        "accept: application/json",
        "Authorization: Bearer ${BuildConfig.TMDB_API_TOKEN}",
    )
    @GET("3/movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("region") region: String? = null,
    ): MovieListsRangeDto

    /**
     * Get a list of movies ordered by popularity.
     */
    @Headers(
        "accept: application/json",
        "Authorization: Bearer ${BuildConfig.TMDB_API_TOKEN}",
    )
    @GET("3/movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("region") region: String? = null,
    ): MovieListsDto

    /**
     * Get a list of movies ordered by rating.
     */
    @Headers(
        "accept: application/json",
        "Authorization: Bearer ${BuildConfig.TMDB_API_TOKEN}",
    )
    @GET("3/movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("region") region: String? = null,
    ): MovieListsDto

    /**
     * Get a list of movies that are being released soon.
     */
    @Headers(
        "accept: application/json",
        "Authorization: Bearer ${BuildConfig.TMDB_API_TOKEN}",
    )
    @GET("3/movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("region") region: String? = null,
    ): MovieListsRangeDto

    // Movies

    /**
     * Get the top level details of a movie by ID.
     */
    @Headers(
        "accept: application/json",
        "Authorization: Bearer ${BuildConfig.TMDB_API_TOKEN}",
    )
    @GET("3/movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("append_to_response") appendToResponse: String? = null,
        @Query("language") language: String = "en-US",
    ): MovieDetailsDto

    // People Lists

    /**
     * Get a list of people ordered by popularity.
     */
    @Headers(
        "accept: application/json",
        "Authorization: Bearer ${BuildConfig.TMDB_API_TOKEN}",
    )
    @GET("3/person/popular")
    suspend fun getPopularPerson(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
    ): PopularPersonDto

    // People

    /**
     * Query the top level details of a person.
     */
    @Headers(
        "accept: application/json",
        "Authorization: Bearer ${BuildConfig.TMDB_API_TOKEN}",
    )
    @GET("3/person/{person_id}")
    suspend fun getPersonDetails(
        @Path("person_id") personId: Int,
        @Query("append_to_response") appendToResponse: String? = null,
        @Query("language") language: String = "en-US",
    ): PersonDetailsDto

    // Search

    @Headers(
        "accept: application/json",
        "Authorization: Bearer ${BuildConfig.TMDB_API_TOKEN}",
    )
    @GET("3/search/multi")
    suspend fun searchMulti(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
    ): SearchDto

    // Trending

    /**
     * Get the trending movies, TV shows and people
     */
    @Headers(
        "accept: application/json",
        "Authorization: Bearer ${BuildConfig.TMDB_API_TOKEN}",
    )
    @GET("3/trending/all/{time_window}")
    suspend fun getTrending(
        @Path("time_window") timeWindow: TimeWindow = TimeWindow.day,
        @Query("language") language: String = "en-US",
    ): TrendingDto

    /**
     * Get the trending people on TMDB
     */
    @Headers(
        "accept: application/json",
        "Authorization: Bearer ${BuildConfig.TMDB_API_TOKEN}",
    )
    @GET("3/trending/person/{time_window}")
    suspend fun getTrendingPerson(
        @Path("time_window") timeWindow: TimeWindow = TimeWindow.day,
        @Query("language") language: String = "en-US",
    ): TrendingPersonDto

    // TV Series Lists

    /**
     * Get a list of TV shows airing today.
     */
    @Headers(
        "accept: application/json",
        "Authorization: Bearer ${BuildConfig.TMDB_API_TOKEN}",
    )
    @GET("3/tv/airing_today")
    suspend fun getAiringTodaySeries(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("timezone") timezone: String? = null,
    ): SeriesListsDto

    /**
     * Get a list of TV shows that air in the next 7 days.
     */
    @Headers(
        "accept: application/json",
        "Authorization: Bearer ${BuildConfig.TMDB_API_TOKEN}",
    )
    @GET("3/tv/on_the_air")
    suspend fun getOnTheAirSeries(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("timezone") timezone: String? = null,
    ): SeriesListsDto

    /**
     * Get a list of TV shows ordered by popularity.
     */
    @Headers(
        "accept: application/json",
        "Authorization: Bearer ${BuildConfig.TMDB_API_TOKEN}",
    )
    @GET("3/tv/popular")
    suspend fun getPopularSeries(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
    ): SeriesListsDto

    /**
     * Get a list of TV shows ordered by rating.
     */
    @Headers(
        "accept: application/json",
        "Authorization: Bearer ${BuildConfig.TMDB_API_TOKEN}",
    )
    @GET("3/tv/top_rated")
    suspend fun getTopRatedSeries(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
    ): SeriesListsDto

    // TV Series

    /**
     * Get the details of a TV show
     */
    @Headers(
        "accept: application/json",
        "Authorization: Bearer ${BuildConfig.TMDB_API_TOKEN}",
    )
    @GET("3/tv/{series_id}")
    suspend fun getSeriesDetails(
        @Path("series_id") seriesId: Int,
        @Query("append_to_response") appendToResponse: String? = null,
        @Query("language") language: String = "en-US",
    ): SeriesDetailsDto
}
