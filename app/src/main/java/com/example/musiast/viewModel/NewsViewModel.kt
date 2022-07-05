package com.example.musiast.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.example.musiast.model.Article
import com.example.musiast.model.NewsResponse
import com.example.musiast.repository.NewsRepository
import com.example.musiast.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val newsrepository: NewsRepository
) : ViewModel() {
    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var brekingPageNumebr = 1
    var breakingNewsResponse: NewsResponse? = null
    lateinit var articles: LiveData<PagedList<Article>>

    init {
        getBreakingNews("songs")
    }

    private fun getBreakingNews(q: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = newsrepository.getBreakingNews(q, brekingPageNumebr)
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }
    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                brekingPageNumebr++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun getBreakingNews() : LiveData<PagedList<Article>>{
        return articles
    }
}