package com.example.minisofascore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.minisofascore.data.models.Team
import com.example.minisofascore.databinding.ActivityTeamDetailsBinding

class TeamDetailsActivity : AppCompatActivity() {

    companion object {
        const val TEAM_DETAILS = "team_details"
        fun newInstance(context: Context, team: Team): Intent {
            return Intent(context, TeamDetailsActivity::class.java).apply {
                putExtra(TEAM_DETAILS, team)
            }
        }
    }

    private lateinit var binding: ActivityTeamDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(Toolbar(this))
        supportActionBar?.hide() // custom toolbar in each fragment

        val team = intent.getSerializableExtra(TEAM_DETAILS) as Team

        val navController = findNavController(R.id.nav_host_fragment)
        navController.setGraph(
            R.navigation.navigation_team_details,
            Bundle().apply {
                putSerializable(TEAM_DETAILS, team)
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