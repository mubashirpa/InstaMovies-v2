package instamovies.app.domain.usecase

import instamovies.app.core.util.Resource
import instamovies.app.core.util.UiText
import instamovies.app.data.mapper.toListResultModel
import instamovies.app.domain.model.list.ListResultModel
import instamovies.app.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import instamovies.app.R.string as Strings

class GetListUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        operator fun invoke(listId: Int): Flow<Resource<List<ListResultModel>>> =
            flow {
                try {
                    emit(Resource.Loading())
                    val results =
                        movieRepository.getList(listId).results?.map { it.toListResultModel() }
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
