package com.riahi.graphqlapp3.repositories

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.exception.ApolloException
import com.riahi.graphqlapp3.GetLatestTrendingRepositoriesInLastWeekQuery
import com.riahi.graphqlapp3.type.SearchType
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class LatestTrendingRepositories() {

    fun getLatestTrendingRepositoriesInLastWeek(completion: (result: Pair<List<GetLatestTrendingRepositoriesInLastWeekQuery.Edge>?, Error?>) -> Unit) {
        val lastWeekDate = LocalDate.now().minusDays(7)
        val formattedDateText = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH).format(lastWeekDate)
        val queryCall = GetLatestTrendingRepositoriesInLastWeekQuery
            .builder()
            .query("created:>$formattedDateText sort:stars-desc")
            .first(25)
            .type(SearchType.REPOSITORY)
            .build()


        Api.apolloClient.query(queryCall).enqueue(object: ApolloCall.Callback<GetLatestTrendingRepositoriesInLastWeekQuery.Data>() {
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
        })
    }
}