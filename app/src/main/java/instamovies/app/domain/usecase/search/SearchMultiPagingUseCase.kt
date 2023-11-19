package instamovies.app.domain.usecase.search

import androidx.paging.PagingData
import androidx.paging.map
import instamovies.app.data.mapper.toSearchResultModel
import instamovies.app.domain.model.search.SearchResultModel
import instamovies.app.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchMultiPagingUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        suspend operator fun invoke(
            query: String,
            includeAdult: Boolean = false,
            language: String = "en-US",
            page: Int = 1,
        ): Flow<PagingData<SearchResultModel>> {
            return movieRepository.searchMultiPaging(query, includeAdult, language, page)
                .map { pagingData ->
                    pagingData.map {
                        it.toSearchResultModel()
                    }
                }
        }
    }
