package com.example.kinopoisk.presentation.cinema.detail


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide

import com.example.kinopoisk.R
import com.example.kinopoisk.base.BaseFragment
import com.example.kinopoisk.utils.AppConstants
import org.koin.android.ext.android.inject

/**
 * A simple [Fragment] subclass.
 */
class CinemaDetail : BaseFragment() {

    private val viewModel: CinemaDetailViewModel by inject()

    private lateinit var ivBackdrop: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvParking: TextView
    private lateinit var tvContacts: TextView
    private lateinit var tvEntry: TextView

    private var cinemaId: Int? = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cinema_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        setData()
    }

    override fun bindViews(view: View) = with(view) {
        ivBackdrop = view.findViewById(R.id.ivBackdrop)
        tvName = view.findViewById(R.id.tvName)
        tvAddress = view.findViewById(R.id.tvAddress)
        tvParking = view.findViewById(R.id.tvParking)
        tvContacts = view.findViewById(R.id.tvContacts)
        tvEntry = view.findViewById(R.id.tvEntry)
    }

    override fun setData() {
        cinemaId = arguments?.getInt(AppConstants.CINEMA_ID)
        cinemaId?.let { id ->
            viewModel.getCinema(id)
        }
        viewModel.liveData.observe(viewLifecycleOwner, Observer {result ->
            val imageUrl = "${AppConstants.POSTER_CINEMA_BASE_URL}${result.poster}"
            Glide.with(this)
                .load(imageUrl)
                .into(ivBackdrop)
            tvName.text = result.name
            tvAddress.text = result.address
            tvParking.text = result.parking
            tvContacts.text = result.phoneNumber
            tvEntry.text = result.entry
        })
    }


}
