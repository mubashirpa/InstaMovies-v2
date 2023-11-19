package instamovies.app.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import instamovies.app.data.remote.MovieApi
import instamovies.app.data.remote.dto.search.SearchResult
import retrofit2.HttpException
import java.io.IOException

class SearchMultiPagingSource(
    private val movieApi: MovieApi,
    private val query: String,
    private val includeAdult: Boolean,
    private val language: String,
    private val page: Int,
) : PagingSource<Int, SearchResult>() {
    override fun getRefreshKey(state: PagingState<Int, SearchResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchResult> {
        val position = params.key ?: page

        return try {
            val response = movieApi.searchMulti(query, includeAdult, language, position)
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
