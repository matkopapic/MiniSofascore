package com.example.minisofascore

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.example.minisofascore.data.models.Sport
import com.example.minisofascore.databinding.ActivityMainBinding
import com.example.minisofascore.databinding.TabItemSportBinding
import com.example.minisofascore.ui.adapters.SportsViewPagerAdapter
import com.example.minisofascore.ui.home.HomeViewModel
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val homeViewModel by viewModels<HomeViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val tabLayoutSports = binding.tabLayoutSports
        val viewPager = binding.viewPagerSports
        viewPager.isUserInputEnabled = false

        val adapter = SportsViewPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

//        homeViewModel.getAllSports()
//
//        homeViewModel.sports.observe(this){
//            Log.d("aaaa", "main activity sports: $it")
//        }

        TabLayoutMediator(tabLayoutSports, viewPager) { tab, position ->
            val tabBinding = TabItemSportBinding.inflate(layoutInflater)
            when (position) {
                0 -> {
                    tabBinding.tabText.text = getString(R.string.football)
                    tabBinding.tabIcon.setImageResource(R.drawable.ic_football)
                }
                1 -> {
                    tabBinding.tabText.text = getString(R.string.basketball)
                    tabBinding.tabIcon.setImageResource(R.drawable.ic_basketball)
                }
                else -> {
                    tabBinding.tabText.text = getString(R.string.am_football)
                    tabBinding.tabIcon.setImageResource(R.drawable.ic_american_football)
                }
            }
            tab.setCustomView(tabBinding.root)
        }.attach()

    }

}