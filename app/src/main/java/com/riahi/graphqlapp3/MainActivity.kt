package com.riahi.graphqlapp3

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.fetcher.ApolloResponseFetchers
import com.apollographql.apollo.rx2.Rx2Apollo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    val TAG: String = "Main activity"

    val application: App by lazy {
        getApplication() as App
    }

    lateinit var githubRepositoriesCall: ApolloCall<LoadGithubRepositoriesQuery.Data>

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loadGithubMembersQuery: LoadGithubRepositoriesQuery = LoadGithubRepositoriesQuery.builder()
            .login("anasoasis41")
            .build()
        githubRepositoriesCall = application.apolloClient()
            .query(loadGithubMembersQuery)
            .responseFetcher(ApolloResponseFetchers.NETWORK_FIRST)

        Rx2Apollo.from(githubRepositoriesCall)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<Response<LoadGithubRepositoriesQuery.Data>>() {
                override fun onComplete() {
                    Log.d(TAG, "Complete")
                }

                override fun onNext(t: Response<LoadGithubRepositoriesQuery.Data>) {
                    Log.d(TAG, "success ${t.data()?.user?.repositories?.nodes}")
                }

                override fun onError(e: Throwable) {
                    Log.d(TAG, "error $e")
                }
            })

    }
}
