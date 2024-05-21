package com.example.minisofascore.ui.main_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minisofascore.MainActivity
import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.remote.Result
import com.example.minisofascore.data.repository.Repository
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainListViewModel : ViewModel() {

    private val repo = Repository()

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events

    var selectedSport = MainActivity.sports[0]
    var selectedDate: LocalDate = LocalDate.now()

    init {
        getEventsBySportAndDate(selectedSport.slug, LocalDate.now())
    }


    fun getEventsBySportAndDate(sportSlug: String = selectedSport.slug, date: LocalDate = selectedDate) {
        viewModelScope.launch {
            when(val response = repo.getEventsBySportAndDate(sportSlug, date)) {
                is Result.Error -> Log.d("aaaa", "error:${response.error}")
                is Result.Success -> _events.value = response.data
            }
        }
    }

}

