package com.riahi.graphqlapp3

import android.app.Application
import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient

class App: Application() {

    private val BASE_URL = "https://api.github.com/graphql"

    //TODO: put your token here!!!
    private val GITHUB_AUTH_TOKEN = "4b4003ea2e14da4bc4b273cdd0530e1e13d67d86"

    private lateinit var apolloClient: ApolloClient

    override fun onCreate() {
        super.onCreate()

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor {
                val original = it.request()
                val builder = original.newBuilder().method(original.method(), original.body())
                builder.header("Authorization", "Bearer $GITHUB_AUTH_TOKEN")
                it.proceed(builder.build())
            }
            .build()


        apolloClient = ApolloClient.builder()
            .serverUrl(BASE_URL)
            .okHttpClient(okHttpClient)
            .build()
    }

    fun apolloClient(): ApolloClient {
        return this.apolloClient
    }
}