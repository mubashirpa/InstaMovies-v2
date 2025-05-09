package app.instamovies.data.mapper

import app.instamovies.data.remote.dto.list.ListResult
import app.instamovies.domain.model.list.ListResultModel

fun ListResult.toListResultModel(): ListResultModel {
    return ListResultModel(id, mediaType, posterPath)
}
