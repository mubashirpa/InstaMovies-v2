package app.instamovies.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.instamovies.data.remote.MovieApi
import app.instamovies.data.remote.dto.series.SeriesResult
import retrofit2.HttpException
import java.io.IOException

class AiringTodaySeriesPagingSource(
    private val movieApi: MovieApi,
    private val language: String,
    private val page: Int,
    private val timezone: String?,
) : PagingSource<Int, SeriesResult>() {
    override fun getRefreshKey(state: PagingState<Int, SeriesResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SeriesResult> {
        val position = params.key ?: page

        return try {
            val response = movieApi.getAiringTodaySeries(language, position, timezone)
            val results = response.results.orEmpty()
            val totalPages = response.totalPages ?: position

            val prevKey = if (position == page) null else position - 1
            val nextKey = if (position >= totalPages || results.isEmpty()) null else position + 1

            LoadResult.Page(
                data = results,
                prevKey = prevKey,
                nextKey = nextKey,
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
