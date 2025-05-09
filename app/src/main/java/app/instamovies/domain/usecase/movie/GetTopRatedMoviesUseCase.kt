package app.instamovies.domain.usecase.movie

import androidx.paging.PagingData
import androidx.paging.map
import app.instamovies.data.mapper.toMovieResultModel
import app.instamovies.domain.model.movie.MovieResultModel
import app.instamovies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTopRatedMoviesUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        suspend operator fun invoke(
            language: String = "en-US",
            page: Int = 1,
            region: String? = null,
        ): Flow<PagingData<MovieResultModel>> {
            return movieRepository.getTopRatedMovies(language, page, region).map { pagingData ->
                pagingData.map {
                    it.toMovieResultModel()
                }
            }
        }
    }
