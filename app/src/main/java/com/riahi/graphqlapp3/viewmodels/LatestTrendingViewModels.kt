package com.riahi.graphqlapp3.viewmodels

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riahi.graphqlapp3.GetLatestTrendingRepositoriesInLastWeekQuery
import com.riahi.graphqlapp3.repositories.LatestTrendingRepositories

class LatestTrendingViewModels: ViewModel() {

    private val trendingRepositories = LatestTrendingRepositories()
    val reposResult = MutableLiveData<Pair<List<GetLatestTrendingRepositoriesInLastWeekQuery.Edge>?, Error?>>()

    init {
        loadRepos()
    }

    fun loadRepos() {
        trendingRepositories.getLatestTrendingRepositoriesInLastWeek {
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                reposResult.value = it
            }
        }
    }
}