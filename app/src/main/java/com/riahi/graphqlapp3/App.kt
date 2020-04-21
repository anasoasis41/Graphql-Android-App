package com.riahi.graphqlapp3

import android.app.Application
import com.apollographql.apollo.ApolloClient
import com.jakewharton.threetenabp.AndroidThreeTen
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class App: Application() {

    private lateinit var apolloClient: ApolloClient

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)

        val okHttpClient = OkHttpClient.Builder()
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor {
                val original = it.request()
                val builder = original.newBuilder().method(original.method(), original.body())
                builder.header("Authorization", "Bearer $GITHUB_AUTH_TOKEN")
                it.proceed(builder.build())
            }
            .build()


        apolloClient = ApolloClient.builder()
            .serverUrl(GITHUB_GRAPHQL_ENDPOINT)
            .okHttpClient(okHttpClient)
            .build()
    }

    fun apolloClient(): ApolloClient {
        return this.apolloClient
    }
}