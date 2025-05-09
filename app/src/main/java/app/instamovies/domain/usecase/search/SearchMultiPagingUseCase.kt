package app.instamovies.domain.usecase.search

import androidx.paging.PagingData
import androidx.paging.map
import app.instamovies.data.mapper.toSearchResultModel
import app.instamovies.domain.model.search.SearchResultModel
import app.instamovies.domain.repository.MovieRepository
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
