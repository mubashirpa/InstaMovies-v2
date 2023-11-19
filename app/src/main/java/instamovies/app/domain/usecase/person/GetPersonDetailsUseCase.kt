package instamovies.app.domain.usecase.person

import instamovies.app.core.util.Resource
import instamovies.app.core.util.UiText
import instamovies.app.data.mapper.toPersonDetails
import instamovies.app.domain.model.person.PersonDetails
import instamovies.app.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import instamovies.app.R.string as Strings

class GetPersonDetailsUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        operator fun invoke(
            personId: Int,
            appendToResponse: String? = null,
            language: String = "en-US",
        ): Flow<Resource<PersonDetails>> =
            flow {
                try {
                    emit(Resource.Loading())
                    val details =
                        movieRepository.getPersonDetails(personId, appendToResponse, language)
                            .toPersonDetails()
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
