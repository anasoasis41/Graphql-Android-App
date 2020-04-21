package com.riahi.graphqlapp3.repositories

import android.annotation.SuppressLint
import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.fetcher.ApolloResponseFetchers
import com.apollographql.apollo.rx2.Rx2Apollo
import com.riahi.graphqlapp3.GetLatestTrendingRepositoriesInLastWeekQuery
import com.riahi.graphqlapp3.type.SearchType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class LatestTrendingRepositories {

    val TAG: String = "Last trending"
    lateinit var latestTrendingReposCall: ApolloCall<GetLatestTrendingRepositoriesInLastWeekQuery.Data>

    @SuppressLint("CheckResult")
    fun getLatestTrendingRepositoriesInLastWeek(completion: (result: Pair<List<GetLatestTrendingRepositoriesInLastWeekQuery.Edge>?, Error?>) -> Unit) {
        val lastWeekDate = LocalDate.now().minusDays(7)
        val formattedDateText = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH).format(lastWeekDate)
        val queryCall = GetLatestTrendingRepositoriesInLastWeekQuery
            .builder()
            .query("created:>$formattedDateText sort:stars-desc")
            .first(25)
            .type(SearchType.REPOSITORY)
            .build()

        val loadLatestTrendingQuery: GetLatestTrendingRepositoriesInLastWeekQuery = GetLatestTrendingRepositoriesInLastWeekQuery.builder()
            .query("created:>$formattedDateText sort:stars-desc")
            .first(25)
            .type(SearchType.REPOSITORY)
            .build()

        latestTrendingReposCall = Api.apolloClient
            .query(loadLatestTrendingQuery)
            .responseFetcher(ApolloResponseFetchers.NETWORK_FIRST)

        Rx2Apollo.from(latestTrendingReposCall)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<Response<GetLatestTrendingRepositoriesInLastWeekQuery.Data>>() {
                override fun onComplete() {
                    Log.d(TAG, "Complete")
                }

                override fun onNext(response: Response<GetLatestTrendingRepositoriesInLastWeekQuery.Data>) {
                    val errors = response.errors()
                    if (!errors.isEmpty()) {
                        val message = errors[0]?.message() ?: ""
                        completion(Pair(null, Error(message)))
                    } else {
                        completion(Pair(response.data()?.search()?.edges() ?: listOf(), null))
                    }
                }

                override fun onError(e: Throwable) {
                    completion(Pair(null, Error(e.message)))
                }
            })

        /*Api.apolloClient.query(queryCall)
            .enqueue(object: ApolloCall.Callback<GetLatestTrendingRepositoriesInLastWeekQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                completion(Pair(null, Error(e.message)))
            }

            override fun onResponse(response: com.apollographql.apollo.api.Response<GetLatestTrendingRepositoriesInLastWeekQuery.Data>) {
                val errors = response.errors()
                if (!errors.isEmpty()) {
                    val message = errors[0]?.message() ?: ""
                    completion(Pair(null, Error(message)))
                } else {
                    completion(Pair(response.data()?.search()?.edges() ?: listOf(), null))
                }
            }
        })*/
    }
}