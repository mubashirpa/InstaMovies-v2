package instamovies.app.domain.usecase.movie

import androidx.paging.PagingData
import androidx.paging.map
import instamovies.app.data.mapper.toMovieResultModel
import instamovies.app.domain.model.movie.MovieResultModel
import instamovies.app.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNowPlayingMoviesUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        suspend operator fun invoke(
            language: String = "en-US",
            page: Int = 1,
            region: String? = null,
        ): Flow<PagingData<MovieResultModel>> {
            return movieRepository.getNowPlayingMovies(language, page, region).map { pagingData ->
                pagingData.map {
                    it.toMovieResultModel()
                }
            }
        }
    }
