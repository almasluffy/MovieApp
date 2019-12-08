package com.example.kinopoisk.presentation.cinema

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {
    private val fragmentList: ArrayList<Fragment> = arrayListOf()
    private val fragmentListTitle: ArrayList<String> = arrayListOf()

    fun addFragment(fragment : Fragment, title: String){
        fragmentList.add(fragment)
        fragmentListTitle.add(title)
    }
    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentListTitle[position]
    }

    override fun getCount(): Int {
        return fragmentListTitle.size
    }

}