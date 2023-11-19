package instamovies.app.navigation

sealed class Screen(val route: String, val args: String? = null) {
    data object HomeScreen : Screen(Routes.Screen.HOME_SCREEN)

    data object HomeContainerScreen : Screen(Routes.Screen.HOME_CONTAINER_SCREEN)

    data object MoviesScreen : Screen(Routes.Screen.MOVIES_SCREEN)

    data object MovieDetailsScreen :
        Screen(Routes.Screen.MOVIE_DETAILS_SCREEN, Routes.Args.MOVIE_DETAILS_SCREEN)

    data object PersonDetailsScreen :
        Screen(Routes.Screen.PERSON_DETAILS_SCREEN, Routes.Args.PERSON_DETAILS_SCREEN)

    data object PersonScreen : Screen(Routes.Screen.PERSON_SCREEN)

    data object SearchScreen : Screen(Routes.Screen.SEARCH_SCREEN, Routes.Args.SEARCH_SCREEN)

    data object TvShowDetailsScreen :
        Screen(Routes.Screen.TV_SHOW_DETAILS_SCREEN, Routes.Args.TV_SHOW_DETAILS_SCREEN)

    data object TvShowsScreen : Screen(Routes.Screen.TV_SHOWS_SCREEN)

    fun withArgs(vararg args: String?): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

    fun withArgs(vararg args: Any?): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
