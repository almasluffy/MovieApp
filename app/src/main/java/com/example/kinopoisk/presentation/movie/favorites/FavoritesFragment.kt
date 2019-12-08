package com.example.kinopoisk.presentation.movie.favorites


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.kinopoisk.utils.AppPreferences
import com.example.kinopoisk.base.BaseFragment

import com.example.kinopoisk.R
import com.example.kinopoisk.data.CinemaDatabase.Cinema
import com.example.kinopoisk.data.models.MovieData
import com.example.kinopoisk.presentation.movie.MovieAdapter
import com.example.kinopoisk.utils.AppConstants
import com.example.kinopoisk.utils.PaginationListener
import org.koin.android.ext.android.inject

/**
 * A simple [Fragment] subclass.
 */
class FavoritesFragment : BaseFragment() {
    private val viewModel: FavoritesViewModel by inject()
    private lateinit var navController: NavController
    private lateinit var rvFavMovies: RecyclerView
    private lateinit var srlFavMovies: SwipeRefreshLayout

    private var currentPage = PaginationListener.PAGE_START
    private var isLastPage = false
    private var isLoading = false
    private var itemCount = 0

    private var accountId: Int? = null
    private var sessionId: String? = null


    private val onClickListener = object: MovieAdapter.ItemClickListener {
        override fun onItemClick(item: MovieData) {
            navController.navigate(
                R.id.action_favoriteFragment_to_movieDetailsFragment,
                bundleOf(
                    AppConstants.MOVIE_ID to item.id,
                    AppConstants.PARENT_FRAGMENT to "favorite_fragment"
                )
            )
        }
    }


    private val moviesAdapter by lazy {
        MovieAdapter(
            itemClickListener = onClickListener
        )
    }

    private fun setAdapter() {
        rvFavMovies.adapter = moviesAdapter
    }

    private fun initId() {
        accountId = AppPreferences.getAccountId(activity?.applicationContext!!)
        sessionId = AppPreferences.getSessionId(activity?.applicationContext!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initId()
        bindViews(view)
        setAdapter()
        setData()
    }

    override fun bindViews(view: View) = with(view) {
        navController = Navigation.findNavController(this)
        srlFavMovies = view.findViewById(R.id.srlFavMovies)
        rvFavMovies = view.findViewById(R.id.rvFavMovies)
        val layoutManager = LinearLayoutManager(context)
        rvFavMovies.layoutManager = layoutManager
        rvFavMovies.addOnScrollListener(object: PaginationListener(layoutManager) {

            override fun loadMoreItems() {
                isLoading = true
                currentPage++
                viewModel.loadFavMovies(accountId, sessionId, page = currentPage)
            }

            override fun isLastPage(): Boolean = isLastPage

            override fun isLoading(): Boolean = isLoading
        })
        srlFavMovies.setOnRefreshListener {
            moviesAdapter.clearAll()
            itemCount = 0
            currentPage = PaginationListener.PAGE_START
            isLastPage = false
            viewModel.loadFavMovies(accountId, sessionId, page = currentPage)
            srlFavMovies.isRefreshing = false
        }
    }


    override fun setData() {
        viewModel.loadFavMovies(accountId, sessionId)

        viewModel.liveData.observe(viewLifecycleOwner, Observer { result ->
            when(result) {
                is FavoritesViewModel.State.Result -> {
                    itemCount = result.list.size
                    if (currentPage != PaginationListener.PAGE_START) {
                        moviesAdapter.removeLoading()
                    }
                    moviesAdapter.addItems(result.list)
                    if (currentPage < result.totalPages) {
                        moviesAdapter.addLoading()
                    } else {
                        isLastPage = true
                    }
                    isLoading = false
                }
                is FavoritesViewModel.State.Error -> {
                    Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                }
                is FavoritesViewModel.State.IntError -> {
                    Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
