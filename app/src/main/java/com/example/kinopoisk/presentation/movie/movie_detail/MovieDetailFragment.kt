package com.example.kinopoisk.presentation.movie.movie_detail


import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.kinopoisk.base.BaseFragment

import com.example.kinopoisk.R
import com.example.kinopoisk.presentation.movie.favorites.FavoritesViewModel
import com.example.kinopoisk.presentation.movie.movie_list.MovieListViewModel
import com.example.kinopoisk.utils.AppConstants
import com.example.kinopoisk.utils.AppPreferences
import kotlinx.android.synthetic.main.for_movie.view.*
import kotlinx.android.synthetic.main.for_movie.view.tvName
import kotlinx.android.synthetic.main.fragment_movie_detail.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

/**
 * A simple [Fragment] subclass.
 */
class MovieDetailFragment : BaseFragment() {

    private val viewModel: MovieDetailViewModel by inject()

    private lateinit var progressBar: ProgressBar
    private lateinit var ivBackdrop: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvGenre: TextView
    private lateinit var tvOverview: TextView
    private lateinit var ivFavorite: ImageView

    private var movieId: Int? = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = view.findViewById(R.id.progressBar)
        ivBackdrop = view.findViewById(R.id.ivBackdrop)
        tvName = view.findViewById(R.id.tvName)
        tvRating = view.findViewById(R.id.tvRating)
        tvGenre = view.findViewById(R.id.tvGenre)
        tvOverview = view.findViewById(R.id.tvOverview)
        ivFavorite = view.findViewById(R.id.ivFavorite)

        bindViews(view)

        setData()

        val accountId = AppPreferences.getAccountId(activity?.applicationContext!!)
        val sessionId = AppPreferences.getSessionId(activity?.applicationContext!!)


        val parentFragment = arguments?.getString(AppConstants.PARENT_FRAGMENT)

        ivFavorite.setOnClickListener{
            movieId?.let {
                accountId?.let {
                    sessionId?.let {
                        if (parentFragment.equals("list_fragment")) {
                            viewModel.setFavorite(accountId, movieId!!, sessionId, true)
                        } else if (parentFragment.equals("favorite_fragment")){
                            viewModel.setFavorite(accountId, movieId!!, sessionId, false)
                            Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }


    override fun bindViews(view: View) = with(view){
        movieId = arguments?.getInt(AppConstants.MOVIE_ID)
    }
    override fun setData() {
        movieId?.let { movieId ->
            viewModel.getMovie(movieId)
        }

        viewModel.liveData.observe(viewLifecycleOwner, Observer {result ->
            when(result) {
                is MovieDetailViewModel.State.ShowLoading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is MovieDetailViewModel.State.HideLoading -> {
                    progressBar.visibility = View.GONE
                }
                is MovieDetailViewModel.State.Result -> {
                    val imageUrl = "${AppConstants.BACKDROP_BASE_URL}${result.movie.backdropPath}"
                    Glide.with(this)
                        .load(imageUrl)
                        .into(ivBackdrop)
                    if(result.movie.adult == true){
                        tvName.text = result.movie.title + " (18+)"
                    }
                    else{
                        tvName.text = result.movie.title
                    }
                    tvRating.text = "${result.movie.voteAverage}/10"
                    tvGenre.text = "Genre: " + result.movie.genres?.first()?.name
                    tvOverview.text = result.movie.overview
                }
                is MovieDetailViewModel.State.FavoriteMovie -> {
                    when(result.resultCode) {
                        1 -> Toast.makeText(context, "Successfully added to your favorite movies!", Toast.LENGTH_SHORT).show()
                        12 -> Toast.makeText(context, "The movie was updated successfully", Toast.LENGTH_SHORT).show()
                        13 -> Toast.makeText(context, "The movie was deleted from favorites", Toast.LENGTH_SHORT).show()
                    }
                }
                is MovieDetailViewModel.State.Error -> {
                    Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                }
                is MovieDetailViewModel.State.IntError -> {
                    Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}