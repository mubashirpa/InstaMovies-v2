package instamovies.app.data.mapper

import instamovies.app.data.remote.dto.list.ListResult
import instamovies.app.domain.model.list.ListResultModel

fun ListResult.toListResultModel(): ListResultModel {
    return ListResultModel(id, mediaType, posterPath)
}
