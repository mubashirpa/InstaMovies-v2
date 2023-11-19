package instamovies.app.navigation

object Routes {
    object Screen {
        const val HOME_CONTAINER_SCREEN = "home_container_screen"
        const val HOME_SCREEN = "home_screen"
        const val MOVIES_SCREEN = "movies_screen"
        const val MOVIE_DETAILS_SCREEN = "movie_details_screen"
        const val PERSON_DETAILS_SCREEN = "person_details_screen"
        const val PERSON_SCREEN = "person_screen"
        const val SEARCH_SCREEN = "search_screen"
        const val TV_SHOW_DETAILS_SCREEN = "tv_show_details_screen"
        const val TV_SHOWS_SCREEN = "tv_shows_screen"
    }

    object Args {
        const val MOVIE_DETAILS_MOVIE_ID = "movie_id"
        const val MOVIE_DETAILS_SCREEN = "/{$MOVIE_DETAILS_MOVIE_ID}"

        const val PERSON_DETAILS_PERSON_ID = "person_id"
        const val PERSON_DETAILS_TITLE = "title"
        const val PERSON_DETAILS_SCREEN = "/{$PERSON_DETAILS_PERSON_ID}/{$PERSON_DETAILS_TITLE}"

        const val SEARCH_SCREEN_QUERY = "search_query"
        const val SEARCH_SCREEN = "/{$SEARCH_SCREEN_QUERY}"

        const val TV_SHOW_DETAILS_SERIES_ID = "series_id"
        const val TV_SHOW_DETAILS_SCREEN = "/{$TV_SHOW_DETAILS_SERIES_ID}"
    }
}
