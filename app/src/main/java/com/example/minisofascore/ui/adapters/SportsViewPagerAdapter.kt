package com.example.minisofascore.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.minisofascore.ui.dashboard.DashboardFragment
import com.example.minisofascore.ui.home.HomeFragment
import com.example.minisofascore.ui.notifications.NotificationsFragment

class SportsViewPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            1 -> {
                return NotificationsFragment()
            }
            2 -> {
                return DashboardFragment()
            }
        }
        return HomeFragment()
    }



}