package instamovies.app.core

object Constants {
    const val TMDB_API_KEY = "8fbbde1477961525d0e2fb35b3458255"
    const val TMDB_API_TOKEN =
        "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4ZmJiZGUxNDc3OTYxNTI1ZDBlMmZiMzViMzQ1ODI1NSIsInN1YiI6IjYwNmM2MWZlNDRlYTU0MDAyOTE3MTJmMiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.OODyWaT3s5XdUj4wmSVBzgussw2GNvx_waQ8qsh193Y"
    const val TMDB_BASE_URL = "https://api.themoviedb.org/"
    private const val TMDB_IMAGES_BASE_URL = "https://image.tmdb.org/t/p/"
    const val TMDB_IMAGES_PREFIX_ORIGINAL = "${TMDB_IMAGES_BASE_URL}original"
    const val TMDB_BACKDROP_PREFIX = "${TMDB_IMAGES_BASE_URL}w780"
    const val TMDB_LOGO_PREFIX = "${TMDB_IMAGES_BASE_URL}w300"
    const val TMDB_POSTER_PREFIX = "${TMDB_IMAGES_BASE_URL}w500"
    const val TMDB_PROFILE_PREFIX = "${TMDB_IMAGES_BASE_URL}w185"
    const val TMDB_STILL_PREFIX = "${TMDB_IMAGES_BASE_URL}w185"
}
