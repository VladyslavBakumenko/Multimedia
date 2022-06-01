package com.example.multimedia.presentation.mediaPlayerActivity

import android.app.Application
import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.multimedia.R
import com.example.multimedia.data.SoundModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val application: Application) : ViewModel() {

    private var itFirstTab = true

    private val _currentSound = MutableLiveData<MediaPlayer>()
    val currentSound: LiveData<MediaPlayer>
        get() = _currentSound

    private val _previousSound = MutableLiveData<MediaPlayer>()

    fun getSoundList(): List<SoundModel> {
        val soundList = mutableListOf<SoundModel>()
        with(soundList) {
            add(SoundModel("Sound 1", MediaPlayer.create(application, R.raw.sound1)))
            add(SoundModel("Sound 2", MediaPlayer.create(application, R.raw.sound2)))
            add(SoundModel("Sound 3", MediaPlayer.create(application, R.raw.sound3)))
        }
        return soundList
    }


    fun clickToRecyclerViewItem(soundModel: SoundModel) {
        _currentSound.value = soundModel.sound
        if (itFirstTab) {
            _currentSound.value?.start()
            _previousSound.value = _currentSound.value
            itFirstTab = false
        } else {
            if (_previousSound.value?.isPlaying == true) {
                _previousSound.value?.pause()
                _previousSound.value?.seekTo(0)
            }
            _currentSound.value?.start()
            _previousSound.value = _currentSound.value
        }
    }
}