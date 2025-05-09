package app.instamovies.domain.usecase.search

import app.instamovies.R
import app.instamovies.core.util.Resource
import app.instamovies.core.util.UiText
import app.instamovies.data.mapper.toSearchResultModel
import app.instamovies.domain.model.search.SearchResultModel
import app.instamovies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SearchMultiUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        operator fun invoke(
            query: String,
            includeAdult: Boolean = false,
            language: String = "en-US",
            page: Int = 1,
        ): Flow<Resource<List<SearchResultModel>>> =
            flow {
                try {
                    emit(Resource.Loading())
                    val results =
                        movieRepository.searchMulti(
                            query,
                            includeAdult,
                            language,
                            page,
                        ).results?.map { it.toSearchResultModel() }
                    emit(Resource.Success(results))
                } catch (e: IOException) {
                    emit(Resource.Error(UiText.StringResource(R.string.error_no_internet)))
                } catch (e: HttpException) {
                    emit(Resource.Error(UiText.StringResource(R.string.error_unexpected)))
                } catch (e: Exception) {
                    emit(Resource.Error(UiText.StringResource(R.string.error_unknown)))
                }
            }
    }
