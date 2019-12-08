
package com.example.kinopoisk.presentation.cinema


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager

import com.example.kinopoisk.R
import com.example.kinopoisk.presentation.cinema.list.CinemaList
import com.example.kinopoisk.presentation.cinema.map.CinemasMap
import com.google.android.gms.maps.MapFragment
import com.google.android.material.tabs.TabLayout

/**
 * A simple [Fragment] subclass.
 */

class CinemasFragment : Fragment() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cinemas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupViewPager()
    }

    private fun initViews(view: View) = with(view) {
        tabs = view.findViewById(R.id.tabs)
        viewPager = view.findViewById(R.id.viewPager)
    }

    private fun setupViewPager() {
        val adapter = childFragmentManager.let { fragmentManager ->
            ViewPagerAdapter(
                fragmentManager
            )
        }
        val cinemaListFragment: CinemaList = CinemaList.newInstance()
        val cinemaMapFragment: CinemasMap = CinemasMap.newInstance()

        adapter.addFragment(cinemaListFragment, "Cinemas")
        adapter.addFragment(cinemaMapFragment, "Map")

        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }

}
