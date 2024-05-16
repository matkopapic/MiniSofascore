package com.example.minisofascore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.minisofascore.databinding.ActivityMainBinding
import com.example.minisofascore.databinding.TabItemSportBinding
import com.example.minisofascore.ui.adapters.SportsViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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

        TabLayoutMediator(tabLayoutSports, viewPager) { tab, position ->
            val tabBinding = TabItemSportBinding.inflate(layoutInflater)
            when (position) {
                0 -> {
                    tabBinding.tabText.text = "Football"
                    tabBinding.tabIcon.setImageResource(R.drawable.ic_football)
                }
                1 -> {
                    tabBinding.tabText.text = "Basketball"
                    tabBinding.tabIcon.setImageResource(R.drawable.ic_basketball)
                }
                else -> {
                    tabBinding.tabText.text = "Am. Football"
                    tabBinding.tabIcon.setImageResource(R.drawable.ic_american_football)
                }
            }
            tab.setCustomView(tabBinding.root)
        }.attach()

    }

}