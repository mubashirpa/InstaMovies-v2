package app.instamovies.domain.usecase.trending

import app.instamovies.core.util.Resource
import app.instamovies.core.util.UiText
import app.instamovies.data.mapper.toTrendingResultModel
import app.instamovies.domain.model.trending.TrendingResultModel
import app.instamovies.domain.repository.MovieRepository
import app.instamovies.domain.repository.TimeWindow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import app.instamovies.R.string as Strings

class GetTrendingUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        operator fun invoke(
            timeWindow: TimeWindow = TimeWindow.day,
            language: String = "en-US",
        ): Flow<Resource<List<TrendingResultModel>>> =
            flow {
                try {
                    emit(Resource.Loading())
                    val results =
                        movieRepository.getTrending(
                            timeWindow,
                            language,
                        ).results?.map { it.toTrendingResultModel() }
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
