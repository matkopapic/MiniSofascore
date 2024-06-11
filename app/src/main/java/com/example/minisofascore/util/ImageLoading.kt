package com.example.minisofascore.util

import android.widget.ImageView
import coil.load
import com.example.minisofascore.R
import com.example.minisofascore.data.repository.PlayerRepository
import com.example.minisofascore.data.repository.TeamRepository
import com.example.minisofascore.data.repository.TournamentRepository
import java.util.Locale


fun ImageView.loadTeamLogo(teamId: Int){
    load(TeamRepository.getTeamLogoUrl(teamId))
}

fun ImageView.loadTournamentLogo(tournamentId: Int) {
    load(TournamentRepository.getTournamentLogoUrl(tournamentId))
}

fun ImageView.loadPlayerImage(playerId: Int) {
    load(PlayerRepository.getPlayerImageUrl(playerId)) {
        placeholder(R.drawable.ic_anonymous)
        error(R.drawable.ic_anonymous)
    }
}

fun ImageView.loadFlag(countryName: String) {
    val countryCode = FlagUtil.getCountryCode(countryName) ?: ""
    val flagUrl = "https://flagsapi.com/${countryCode}/flat/64.png"

    load(flagUrl){
        error(R.drawable.ic_question_mark)
    }

}

object FlagUtil {
    private val countries = mutableMapOf<String,String>()
    init {
        // trying to get iso codes from country name, mostly inaccurate because
        // of different ways to write country names but works for this project
        for (iso in Locale.getISOCountries()) {
            val l = Locale("", iso)
            countries[l.displayCountry] = iso
        }
        countries["England"] = "GB"
        countries["USA"] = "US"
    }

    fun getCountryCode(countryName: String) = countries[countryName]
}