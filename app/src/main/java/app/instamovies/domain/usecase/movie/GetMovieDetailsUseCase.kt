package app.instamovies.domain.usecase.movie

import app.instamovies.core.util.Resource
import app.instamovies.core.util.UiText
import app.instamovies.data.mapper.toMovieDetails
import app.instamovies.domain.model.movie.details.MovieDetails
import app.instamovies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import app.instamovies.R.string as Strings

class GetMovieDetailsUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        operator fun invoke(
            movieId: Int,
            appendToResponse: String? = null,
            language: String = "en-US",
        ): Flow<Resource<MovieDetails>> =
            flow {
                try {
                    emit(Resource.Loading())
                    val details =
                        movieRepository.getMovieDetails(movieId, appendToResponse, language)
                            .toMovieDetails()
                    emit(Resource.Success(details))
                } catch (e: IOException) {
                    emit(Resource.Error(UiText.StringResource(Strings.error_no_internet)))
                } catch (e: HttpException) {
                    emit(Resource.Error(UiText.StringResource(Strings.error_unexpected)))
                } catch (e: Exception) {
                    emit(Resource.Error(UiText.StringResource(Strings.error_unknown)))
                }
            }
    }
