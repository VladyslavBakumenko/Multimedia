package com.example.multimedia.presentation.exoPlayerActivity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.multimedia.R
import com.example.multimedia.databinding.ActivityExoPlayerBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player


class ExoPlayerActivity: AppCompatActivity(), Player.Listener {

    private lateinit var binding: ActivityExoPlayerBinding
    private lateinit var exoPlayer: ExoPlayer

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        binding = ActivityExoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
      //  setPlayer()
    //    addMp3Files()
  //      addMp4Files()
    }
/*
    private fun setPlayer() {
        exoPlayer = ExoPlayer.Builder(this).build()
        binding.videoView.player = exoPlayer
        exoPlayer.addListener(this)
    }

    private fun addMp4Files() {
        val mediaItem = MediaItem.fromUri(getString(R.string.mp4))
        exoPlayer.addMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    private fun addMp3Files() {
        val mediaItem = MediaItem.fromUri(getString(R.string.mp3))
        exoPlayer.addMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        when(playbackState) {
            Player.STATE_BUFFERING -> binding.progressBar.visibility = View.VISIBLE

            Player.STATE_READY -> binding.progressBar.visibility = View.INVISIBLE
        }
    }

    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
        super.onMediaMetadataChanged(mediaMetadata)

        binding.title.text = mediaMetadata.title
    }*/
}