package app.instamovies.presentation.tvShows

import androidx.annotation.StringRes
import app.instamovies.R.string as Strings

enum class TvShowsScreenPages(
    @StringRes val titleResId: Int,
) {
    Popular(Strings.label_popular),
    TopRated(Strings.label_top_rated),
    OnTV(Strings.label_on_tv),
    AiringToday(Strings.label_airing_today),
}
