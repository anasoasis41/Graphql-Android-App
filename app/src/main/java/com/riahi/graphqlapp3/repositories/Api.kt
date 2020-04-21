package com.riahi.graphqlapp3.repositories

import com.apollographql.apollo.ApolloClient
import com.riahi.graphqlapp3.GITHUB_AUTH_TOKEN
import com.riahi.graphqlapp3.GITHUB_GRAPHQL_ENDPOINT
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.util.concurrent.TimeUnit

class Api {

    companion object {

        val httpClient = OkHttpClient.Builder()
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor {
                val original = it.request()
                val builder = original.newBuilder().method(original.method(), original.body())
                builder.header("Authorization", "Bearer $GITHUB_AUTH_TOKEN")
                it.proceed(builder.build())
            }
            .build()

        /*val httpClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .addNetworkInterceptor(NetworkInterceptor())
                .build()
        }*/


        val apolloClient: ApolloClient by lazy {
            ApolloClient.builder()
                .serverUrl(GITHUB_GRAPHQL_ENDPOINT)
                .okHttpClient(httpClient)
                .build()
        }

        private class NetworkInterceptor: Interceptor {

            override fun intercept(chain: Interceptor.Chain): Response {
                return chain.proceed(chain.request().newBuilder().header("Authorization", "Bearer $GITHUB_AUTH_TOKEN").build())
            }
        }

    }
}