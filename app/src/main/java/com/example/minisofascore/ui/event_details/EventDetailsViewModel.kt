package com.example.minisofascore.ui.event_details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minisofascore.data.models.Incident
import com.example.minisofascore.data.remote.Result
import com.example.minisofascore.data.repository.Repository
import kotlinx.coroutines.launch


class EventDetailsViewModel : ViewModel() {

    private val repo = Repository()

    private val _incidents = MutableLiveData<List<Incident>>()
    val incidents: LiveData<List<Incident>> = _incidents

    fun getIncidents(eventId: Int){
        viewModelScope.launch {
            when (val response = repo.getIncidentsForEvent(eventId)) {
                is Result.Error -> Log.d("aaaa", response.error.toString())
                is Result.Success -> _incidents.value = response.data
            }
        }
    }
}