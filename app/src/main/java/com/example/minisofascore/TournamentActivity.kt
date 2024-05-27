package com.example.minisofascore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.minisofascore.databinding.ActivityTournamentBinding

class TournamentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTournamentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTournamentBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}