package com.example.minisofascore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.minisofascore.data.models.Sport
import com.example.minisofascore.databinding.ActivityMainBinding


const val NUM_OF_DATE_TABS = 7 + 1 + 7 // 1 week before + today + 1 week after
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        val sports = listOf(
            Sport(1, "Football", "football"),
            Sport(2, "Basketball", "basketball"),
            Sport(3, "Am. Football", "american-football")
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(Toolbar(this))
        supportActionBar?.hide()
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration: AppBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()
        setupActionBarWithNavController(navController, appBarConfiguration)
    }


}