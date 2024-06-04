@file:Suppress("DEPRECATION")
package com.example.minisofascore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.minisofascore.data.models.Tournament
import com.example.minisofascore.databinding.ActivityTournamentBinding
import com.example.minisofascore.ui.viewpager.ViewPagerViewModel

class TournamentActivity : AppCompatActivity() {

    private val viewModel: ViewPagerViewModel by viewModels()

    companion object {
        const val TOURNAMENT_INFO = "tournament_info"
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

        setSupportActionBar(Toolbar(this))
        supportActionBar?.hide() // custom toolbar in each fragment

        val tournament = intent.getSerializableExtra(TOURNAMENT_INFO) as Tournament

        val navController = findNavController(R.id.nav_host_fragment)
        navController.setGraph(
            R.navigation.navigation_tournament,
            Bundle().apply {
                putSerializable(TOURNAMENT_INFO, tournament)
            }
        )
        val appBarConfiguration: AppBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}