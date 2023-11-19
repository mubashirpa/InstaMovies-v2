package instamovies.app.presentation.movies

import androidx.annotation.StringRes
import instamovies.app.R.string as Strings

enum class MoviesScreenPages(
    @StringRes val titleResId: Int,
) {
    Popular(Strings.label_popular),
    TopRated(Strings.label_top_rated),
    Upcoming(Strings.label_upcoming),
    NowPlaying(Strings.label_now_playing),
}
