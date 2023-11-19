package instamovies.app.domain.usecase.trending

import instamovies.app.core.util.Resource
import instamovies.app.core.util.UiText
import instamovies.app.data.mapper.toTrendingPersonResultModel
import instamovies.app.domain.model.trending.TrendingPersonResultModel
import instamovies.app.domain.repository.MovieRepository
import instamovies.app.domain.repository.TimeWindow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import instamovies.app.R.string as Strings

class GetTrendingPersonUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        operator fun invoke(
            timeWindow: TimeWindow = TimeWindow.day,
            language: String = "en-US",
        ): Flow<Resource<List<TrendingPersonResultModel>>> =
            flow {
                try {
                    emit(Resource.Loading())
                    val results =
                        movieRepository.getTrendingPerson(
                            timeWindow,
                            language,
                        ).results?.map { it.toTrendingPersonResultModel() }
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
