@file:Suppress("deprecation")
package com.example.minisofascore

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.minisofascore.databinding.ActivityMainBinding
import com.example.minisofascore.ui.settings.SettingsFragment
import com.example.minisofascore.ui.settings.Theme


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val preferences by lazy { PreferenceManager.getDefaultSharedPreferences(this) }

    override fun onCreate(savedInstanceState: Bundle?) {

        updateAppTheme()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(Toolbar(this))
        supportActionBar?.hide() // custom toolbar in each fragment

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration: AppBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun updateAppTheme() {
        when (preferences.getString(SettingsFragment.THEME, Theme.SYSTEM_DEFAULT.name)) {
            Theme.LIGHT.name -> SettingsFragment.setAppTheme(Theme.LIGHT)
            Theme.DARK.name -> SettingsFragment.setAppTheme(Theme.DARK)
            else -> SettingsFragment.setAppTheme(Theme.SYSTEM_DEFAULT)
        }
    }


}