package com.example.kinopoisk.presentation.cinema.list


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.kinopoisk.R
import com.example.kinopoisk.base.BaseFragment
import com.example.kinopoisk.data.CinemaDatabase.Cinema
import com.example.kinopoisk.presentation.movie.favorites.FavoritesViewModel
import com.example.kinopoisk.utils.AppConstants
import org.koin.android.ext.android.inject

/**
 * A simple [Fragment] subclass.
 */
class CinemaList : BaseFragment() {


    private val listViewModel: CinemaListViewModel by inject()

    private lateinit var navController: NavController
    private lateinit var rvCinemas: RecyclerView
    private val TAG = "CinemaTag:"

    companion object {
        fun newInstance() : CinemaList =
            CinemaList()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cinema_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //listViewModel = ViewModelProviders.of(this).get(CinemaListViewModel::class.java)
        bindViews(view)
        setAdapter()
        setData()
    }

    private val onClickListener = object:
        CinemaListAdapter.ItemClickListener {
        override fun onItemClick(item: Cinema) {
            val bundle = Bundle()
            item.id?.let { id ->
                bundle.putInt(AppConstants.CINEMA_ID, id)
            }

            navController.navigate(
                R.id.action_cinemaFragment_to_cinemaDetailsFragment,
                bundle
            )
            Log.d(TAG, bundle.getInt(AppConstants.CINEMA_ID).toString())
        }
    }
    private val cinemaListAdapter by lazy {
        CinemaListAdapter (
            context = context,
            itemClickListener = onClickListener
        )
    }

    override fun bindViews(view: View) = with(view) {
        navController = Navigation.findNavController(this)
        rvCinemas = findViewById(R.id.recyclerView)
        rvCinemas.layoutManager = LinearLayoutManager(context)
    }

    override fun setData() {
        listViewModel.liveData.observe(viewLifecycleOwner, Observer { cinemaList ->
            cinemaListAdapter.setCinemas(cinemaList)
        })
    }

    private fun setAdapter() {
        rvCinemas.adapter = cinemaListAdapter
    }
}
