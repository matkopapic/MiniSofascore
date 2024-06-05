@file:Suppress("deprecation")
package com.example.minisofascore.ui.settings

import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.minisofascore.R
import com.example.minisofascore.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val preferences by lazy { PreferenceManager.getDefaultSharedPreferences(requireContext()) }

    companion object {
        const val THEME = "theme"
        const val DATE_FORMAT = "date_format"

        fun setAppTheme(theme: Theme) {
            when (theme) {
                Theme.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                Theme.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                else -> {}
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.toolbarBackArrow.toolbarName.text = requireContext().getString(R.string.settings)
        binding.toolbarBackArrow.toolbarName.alpha = 1f

        binding.toolbarBackArrow.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        val languages = resources.getStringArray(R.array.languages)
        val languageCodes = resources.getStringArray(R.array.language_codes)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, languages)
        binding.languageDropdown.setAdapter(arrayAdapter)
        binding.languageDropdown.setDropDownBackgroundResource(R.drawable.dropdown_background)

        val locale = AppCompatDelegate.getApplicationLocales().toLanguageTags()
        if (locale.isNotEmpty()) {
            val langIndex = languageCodes.indexOf(locale).takeIf { it >= 0 }
            langIndex?.let { binding.languageDropdown.setText(languages[it], false) }
        } else {
            binding.languageDropdown.setText(languages[0], false) // default language is English
        }

        binding.languageDropdown.setOnItemClickListener { _, _, _, _ ->
            val langIndex = languages.indexOf(binding.languageDropdown.text.toString()).takeIf { it >= 0 }
            langIndex?.let {
                val languageCode = languageCodes[it]
                AppCompatDelegate.setApplicationLocales(
                    LocaleListCompat.forLanguageTags(languageCode)
                )
                binding.languageDropdown.clearFocus()
            }

        }

        val theme = preferences.getString(THEME, Theme.SYSTEM_DEFAULT.name)
        when (theme) {
            Theme.LIGHT.name -> {
                binding.buttonLight.isChecked = true
            }
            Theme.DARK.name -> {
                binding.buttonDark.isChecked = true
            }
            else -> {
                val isDarkMode = isSystemDarkTheme()
                binding.buttonDark.isChecked = isDarkMode
                binding.buttonLight.isChecked = !isDarkMode
            }
        }
        
        binding.themeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.button_light -> {
                    preferences.edit().putString(THEME, Theme.LIGHT.name).apply()
                    setAppTheme(Theme.LIGHT)
                    activity?.recreate()
                }
                R.id.button_dark -> {
                    preferences.edit().putString(THEME, Theme.DARK.name).apply()
                    setAppTheme(Theme.DARK)
                    activity?.recreate()
                }
            }
        }

        val dateFormat = preferences.getString(DATE_FORMAT, DateFormat.EUROPEAN.name)
        when (dateFormat) {
            DateFormat.EUROPEAN.name -> binding.buttonFormatEuropean.isChecked = true
            else -> binding.buttonFormatAmerican.isChecked = true
        }

        binding.dateFormatRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.button_format_european -> preferences.edit().putString(DATE_FORMAT, DateFormat.EUROPEAN.formatString).apply()
                R.id.button_format_american -> preferences.edit().putString(DATE_FORMAT, DateFormat.AMERICAN.formatString).apply()
            }
        }

        return binding.root
    }

    private fun isSystemDarkTheme(): Boolean {
        return requireContext().resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}

enum class Theme {
    LIGHT,
    DARK,
    SYSTEM_DEFAULT
}

enum class DateFormat(val formatString: String) {
    EUROPEAN("dd.MM."),
    AMERICAN("MM.dd.")
}