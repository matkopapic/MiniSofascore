package com.example.minisofascore.util

import android.widget.ImageView
import coil.load
import com.example.minisofascore.data.repository.Repository

fun ImageView.loadTeamLogo(teamId: Int){
    load(Repository.getTeamLogoUrl(teamId))
}

fun ImageView.loadTournamentLogo(tournamentId: Int) {
    load(Repository.getTournamentLogoUrl(tournamentId))
}