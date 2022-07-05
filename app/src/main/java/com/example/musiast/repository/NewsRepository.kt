package com.example.musiast.repository

import com.example.musiast.repository.service.RetrofitClient

class NewsRepository() {
    suspend fun getBreakingNews(q : String, pageNumber : Int) =
        RetrofitClient.api.getBreakingNews(q, pageNumber)
}