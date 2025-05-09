package app.instamovies.domain.usecase.movie

import app.instamovies.core.util.Resource
import app.instamovies.core.util.UiText
import app.instamovies.data.mapper.toMovieResultModel
import app.instamovies.domain.model.movie.MovieResultModel
import app.instamovies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import app.instamovies.R.string as Strings

class GetPopularMoviesUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        operator fun invoke(
            language: String = "en-US",
            page: Int = 1,
            region: String? = null,
        ): Flow<Resource<List<MovieResultModel>>> =
            flow {
                try {
                    emit(Resource.Loading())
                    val results =
                        movieRepository.getPopularMovies(
                            language,
                            page,
                            region,
                        ).results?.map { it.toMovieResultModel() }
                    emit(Resource.Success(results))
                } catch (e: IOException) {
                    emit(Resource.Error(UiText.StringResource(Strings.error_no_internet)))
                } catch (e: HttpException) {
                    emit(Resource.Error(UiText.StringResource(Strings.error_unexpected)))
                } catch (e: Exception) {
                    emit(Resource.Error(UiText.StringResource(Strings.error_unknown)))
                }
            }
    }
