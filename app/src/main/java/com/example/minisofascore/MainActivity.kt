package com.example.minisofascore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.minisofascore.data.models.Sport
import com.example.minisofascore.data.models.SportType
import com.example.minisofascore.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    companion object {
        // faster since they are constant, could be replaced with API call in the future
        val sports = listOf(
            Sport(1, "Football", "football"),
            Sport(2, "Basketball", "basketball"),
            Sport(3, "Am. Football", "american-football")
        )

        fun getSportTypeFromSport(sport: Sport): SportType {
            return when (sport.slug) {
                "football" -> SportType.FOOTBALL
                "basketball" -> SportType.BASKETBALL
                "american-football" -> SportType.AMERICAN_FOOTBALL
                else -> SportType.FOOTBALL
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(Toolbar(this))
        supportActionBar?.hide() // custom toolbar in each fragment

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration: AppBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()
        setupActionBarWithNavController(navController, appBarConfiguration)
    }


}