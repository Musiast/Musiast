package com.example.musiast.repository.service
import com.example.musiast.model.NewsResponse
import com.example.musiast.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/everything")
    suspend fun getBreakingNews(
        @Query("q") q: String = "songs",
        @Query("page") pageNumber: Int,
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): Response<NewsResponse>
}