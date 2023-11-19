package instamovies.app.domain.usecase.series

import androidx.paging.PagingData
import androidx.paging.map
import instamovies.app.data.mapper.toSeriesResultModel
import instamovies.app.domain.model.series.SeriesResultModel
import instamovies.app.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAiringTodaySeriesUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        suspend operator fun invoke(
            language: String = "en-US",
            page: Int = 1,
            timezone: String? = null,
        ): Flow<PagingData<SeriesResultModel>> {
            return movieRepository.getAiringTodaySeries(language, page, timezone).map { pagingData ->
                pagingData.map {
                    it.toSeriesResultModel()
                }
            }
        }
    }
