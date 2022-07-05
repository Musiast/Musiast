package com.example.musiast.repository.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.musiast.model.Article
import com.example.musiast.model.NewsResponse
import com.example.musiast.repository.service.RetrofitClient
import com.example.musiast.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ArticleDataSource(val scope: CoroutineScope) : PageKeyedDataSource<Int, Article>(){
    val breakingNews : MutableLiveData<MutableList<Article>> = MutableLiveData()
    var brekingPageNumebr = 1
    var breakingNewsResponse : NewsResponse? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Article>
    ) {
        scope.launch {
            try {
                val response = RetrofitClient.api.getBreakingNews("in", 1, Constants.API_KEY)
                when {
                    response.isSuccessful -> {
                        response.body()?.articles?.let {
                            breakingNews.postValue(it)
                            callback.onResult(it, null, 2)
                        }
                    }
                }
            }catch (exception : Exception) {
                Log.e("DataSource :: ", exception.message.toString())
            }
        }
    }
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {
        try {
            scope.launch {
                val response = RetrofitClient.api.getBreakingNews("in", params.requestedLoadSize, Constants.API_KEY)
                when{
                    response.isSuccessful -> {
                        response.body()?.articles?.let {
                            callback.onResult(it, params.key+1)
                        }
                    }
                }
            }
        } catch (exception : Exception) {
            Log.e("DataSource :: ", exception.message.toString())
        }
    }
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {
    }
}