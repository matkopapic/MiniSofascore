@file:Suppress("DEPRECATION")
package com.example.minisofascore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.minisofascore.data.models.Tournament
import com.example.minisofascore.data.repository.Repository
import com.example.minisofascore.databinding.ActivityTournamentBinding
import com.example.minisofascore.databinding.TabItemTextBinding
import com.example.minisofascore.ui.adapters.TournamentViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.math.abs

class TournamentActivity : AppCompatActivity() {

    companion object {
        private const val TOURNAMENT_INFO = "tournament_info"
        fun newInstance(context: Context, tournament: Tournament): Intent {
            return Intent(context, TournamentActivity::class.java).apply {
                putExtra(TOURNAMENT_INFO, tournament)
            }
        }
    }

    private lateinit var binding: ActivityTournamentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTournamentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tournament = intent.getSerializableExtra(TOURNAMENT_INFO) as Tournament

        binding.toolbarBackArrow.backArrow.setOnClickListener {
            finish()
        }

        binding.toolbarBackArrow.toolbarName.text = tournament.name

        // animating text opacity in toolbar when scrolling
        binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange
            val percentage = abs(verticalOffset).toFloat() / totalScrollRange.toFloat()
            binding.toolbarBackArrow.toolbarName.alpha = percentage
        }

        binding.toolbarImageText.name.text = tournament.name
        binding.toolbarImageText.mainLogo.load(Repository.getTournamentLogoUrl(tournament.id))
        binding.toolbarImageText.countryName.text = tournament.country.name
        binding.toolbarImageText.countryLogo.load(Repository.getTournamentLogoUrl(tournament.id))

        val viewpager = binding.viewpager
        val tabLayout = binding.tabLayout
        val adapter = TournamentViewPagerAdapter(supportFragmentManager, lifecycle, tournament)
        viewpager.adapter = adapter

        val tabNames = arrayOf(
            getString(R.string.matches),
            getString(R.string.standings)
        )

        TabLayoutMediator(tabLayout, viewpager){ tab, position ->
            val tabBinding = TabItemTextBinding.inflate(layoutInflater)
            tabBinding.tabText.text = tabNames[position]
            tab.customView = tabBinding.root
        }.attach()

    }
}