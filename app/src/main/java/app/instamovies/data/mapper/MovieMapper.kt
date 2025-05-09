package app.instamovies.data.mapper

import app.instamovies.data.remote.dto.movie.MovieResult
import app.instamovies.data.remote.dto.movie.credits.Cast
import app.instamovies.data.remote.dto.movie.credits.Crew
import app.instamovies.data.remote.dto.movie.credits.MovieCreditsDto
import app.instamovies.data.remote.dto.movie.details.Genre
import app.instamovies.data.remote.dto.movie.details.MovieDetailsDto
import app.instamovies.domain.model.movie.MovieResultModel
import app.instamovies.domain.model.movie.credits.MovieCast
import app.instamovies.domain.model.movie.credits.MovieCredits
import app.instamovies.domain.model.movie.credits.MovieCrew
import app.instamovies.domain.model.movie.details.MovieDetails
import app.instamovies.domain.model.movie.details.MovieGenre

fun MovieDetailsDto.toMovieDetails(): MovieDetails {
    return MovieDetails(
        backdropPath,
        genres?.map { it.toMovieGenre() },
        id,
        overview,
        posterPath,
        releaseDate,
        runtime,
        title,
        voteAverage,
        credits?.toMovieCredits(),
        recommendations?.results?.map { it.toMovieResultModel() },
    )
}

fun Genre.toMovieGenre(): MovieGenre {
    return MovieGenre(name)
}

fun MovieCreditsDto.toMovieCredits(): MovieCredits {
    return MovieCredits(cast?.map { it.toMovieCast() }, crew?.map { it.toMovieCrew() })
}

fun Cast.toMovieCast(): MovieCast {
    return MovieCast(character, id, name, profilePath)
}

fun Crew.toMovieCrew(): MovieCrew {
    return MovieCrew(id, job, name, profilePath)
}

fun MovieResult.toMovieResultModel(): MovieResultModel {
    return MovieResultModel(id, posterPath, releaseDate, title, voteAverage)
}
