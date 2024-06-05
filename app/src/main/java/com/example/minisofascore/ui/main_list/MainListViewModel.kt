package com.example.minisofascore.ui.main_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.models.SportType
import com.example.minisofascore.data.remote.Result
import com.example.minisofascore.data.repository.Repository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainListViewModel : ViewModel() {

    private val repo = Repository()

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events

    var selectedSport = SportType.FOOTBALL
    var selectedDate: LocalDate = LocalDate.now()

    private var currentlyLoadingJob: Job? = null

    init {
        getEventsBySportAndDate(selectedSport.slug, LocalDate.now())
    }


    fun getEventsBySportAndDate(sportSlug: String = selectedSport.slug, date: LocalDate = selectedDate) {
        currentlyLoadingJob?.cancel()
        currentlyLoadingJob = viewModelScope.launch {
            when(val response = repo.getEventsBySportAndDate(sportSlug, date)) {
                is Result.Error -> Log.d("aaaa", "error:${response.error}")
                is Result.Success -> _events.value = response.data
            }
        }
    }

    fun cancelCurrentLoading() {
        currentlyLoadingJob?.cancel()
        currentlyLoadingJob = null
    }

}

