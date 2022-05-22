package com.example.multimedia.presentation.mediaPlayerActivity

import android.media.MediaPlayer

data class MainViewModelState(
    var currentSound: MediaPlayer? = null,
    var previousSound: MediaPlayer? = null
)