package com.example.minisofascore.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.remote.Result
import com.example.minisofascore.data.repository.Repository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repo = Repository()

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is home Fragment"
//    }
//    val text: LiveData<String> = _text
//
//    val image = MutableLiveData<Bitmap>()

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events

    init {
        viewModelScope.launch {
//            val imageBitmap = repo.getTeamLogoById(1000)
//            if (imageBitmap != null) {
//                image.value = imageBitmap!!
//            } else {
//                Log.d("aaaa", "couldnt load image ")
//            }
            when(val response = repo.getEventsByDate()) {
                is Result.Error -> Log.d("aaaa", "error:${response.error}")
                is Result.Success -> _events.value = response.data
            }
        }
    }
}