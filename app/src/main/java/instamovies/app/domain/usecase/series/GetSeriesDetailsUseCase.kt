package instamovies.app.domain.usecase.series

import instamovies.app.core.util.Resource
import instamovies.app.core.util.UiText
import instamovies.app.data.mapper.toSeriesDetails
import instamovies.app.domain.model.series.details.SeriesDetails
import instamovies.app.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import instamovies.app.R.string as Strings

class GetSeriesDetailsUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        operator fun invoke(
            seriesId: Int,
            appendToResponse: String? = null,
            language: String = "en-US",
        ): Flow<Resource<SeriesDetails>> =
            flow {
                try {
                    emit(Resource.Loading())
                    val details =
                        movieRepository.getSeriesDetails(seriesId, appendToResponse, language)
                            .toSeriesDetails()
                    emit(Resource.Success(details))
                } catch (e: IOException) {
                    emit(Resource.Error(UiText.StringResource(Strings.error_no_internet)))
                } catch (e: HttpException) {
                    emit(Resource.Error(UiText.StringResource(Strings.error_unexpected)))
                } catch (e: Exception) {
                    emit(Resource.Error(UiText.StringResource(Strings.error_unknown)))
                }
            }
    }
