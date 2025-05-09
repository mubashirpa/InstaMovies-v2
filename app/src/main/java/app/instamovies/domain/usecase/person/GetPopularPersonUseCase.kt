package app.instamovies.domain.usecase.person

import androidx.paging.PagingData
import androidx.paging.map
import app.instamovies.data.mapper.toPersonResultModel
import app.instamovies.domain.model.person.popular.PersonResultModel
import app.instamovies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPopularPersonUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        suspend operator fun invoke(
            language: String = "en-US",
            page: Int = 1,
        ): Flow<PagingData<PersonResultModel>> {
            return movieRepository.getPopularPerson(language, page)
                .map { pagingData ->
                    pagingData.map {
                        it.toPersonResultModel()
                    }
                }
        }
    }
