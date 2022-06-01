package com.example.multimedia.presentation.exoPlayerActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.multimedia.databinding.ActivityExoPlayerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExoPlayerBinding
    private lateinit var viewModel: ExoPlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ExoPlayerViewModel(application)

        if (savedInstanceState == null) {
            binding.styledPlayerView.player = viewModel.setExoPlayer()
        } else {
            binding.styledPlayerView.player = viewModel.setExoPlayer()
            savedInstanceState.getInt(MEDIA_ITEM).let { restoredMedia ->
                viewModel.exoPlayer.seekTo(restoredMedia, savedInstanceState.getLong(SEEK_TIME))
            }
            if (savedInstanceState.getBoolean(IS_PLAY_BEFORE)) viewModel.exoPlayer.play()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        with(outState) {
            putLong(SEEK_TIME, viewModel.exoPlayer.currentPosition)
            putBoolean(IS_PLAY_BEFORE, viewModel.exoPlayer.isPlaying)
            putInt(MEDIA_ITEM, viewModel.exoPlayer.currentMediaItemIndex)
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.exoPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.exoPlayer.stop()
        viewModel.exoPlayer.release()
    }


    companion object {
        private const val SEEK_TIME = "seek_time"
        private const val IS_PLAY_BEFORE = "is_play_before"
        private const val MEDIA_ITEM = "media_item"
    }
}