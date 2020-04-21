package com.riahi.graphqlapp3.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.riahi.graphqlapp3.R
import com.riahi.graphqlapp3.viewmodels.LatestTrendingViewModels


class LastWeekRepoFragment : Fragment() {

    val repoAdapter = LatestWeekRepoAdapter()
    private lateinit var recyclerViewRepo: RecyclerView
    private lateinit var viewModel: LatestTrendingViewModels

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_last_week_repo, container, false)
        recyclerViewRepo = rootView.findViewById(R.id.recyclerView_lastWeekRepo)
        recyclerViewRepo.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewRepo.adapter = repoAdapter

        observeTrendingRepositories()

        return rootView
    }

    private fun observeTrendingRepositories() {
        viewModel = ViewModelProvider(requireActivity()).get(LatestTrendingViewModels::class.java)
        viewModel.reposResult
            .observe(viewLifecycleOwner, Observer {
                val repos = it?.first
                if (repos != null) {
                    Log.i("repos ", "$repos")
                    repoAdapter.updateRepos(repos)
                }
            })
    }
}
