package app.instamovies.domain.usecase.series

import androidx.paging.PagingData
import androidx.paging.map
import app.instamovies.data.mapper.toSeriesResultModel
import app.instamovies.domain.model.series.SeriesResultModel
import app.instamovies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetOnTheAirSeriesUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        suspend operator fun invoke(
            language: String = "en-US",
            page: Int = 1,
            timezone: String? = null,
        ): Flow<PagingData<SeriesResultModel>> {
            return movieRepository.getOnTheAirSeries(language, page, timezone).map { pagingData ->
                pagingData.map {
                    it.toSeriesResultModel()
                }
            }
        }
    }
