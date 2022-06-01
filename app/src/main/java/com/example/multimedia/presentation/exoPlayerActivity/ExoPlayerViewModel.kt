package com.example.multimedia.presentation.exoPlayerActivity

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.multimedia.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.TracksInfo
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExoPlayerViewModel @Inject constructor(private val application: Application) : ViewModel() {

    var exoPlayer = ExoPlayer.Builder(application).build()

    fun setExoPlayer(): ExoPlayer {
//        val mediaItemsList = listOf(
//            MediaItem.fromUri(Uri.parse(VIDEO_URI_RAW_FOLDER + R.raw.video_test1)),
//            MediaItem.fromUri(Uri.parse(VIDEO_URI_RAW_FOLDER + R.raw.video_test2)),
//            MediaItem.fromUri(Uri.parse(VIDEO_URI_RAW_FOLDER + R.raw.video_test3)),
//            MediaItem.fromUri(Uri.parse(VIDEO_URI_RAW_FOLDER + R.raw.sound1)),
//            MediaItem.fromUri(Uri.parse(VIDEO_URI_RAW_FOLDER + R.raw.sound2))
//        )

//        val mediaItem = MediaItem.Builder()
//            .setUri(Uri.parse(VIDEO_URI_RAW_FOLDER + R.raw.video_test1))
//            .setAdsConfiguration(
//                MediaItem.AdsConfiguration.Builder(Uri.parse(VIDEO_URI_RAW_FOLDER + R.raw.video_test2))
//                    .build()
//            ).build()
        val mediaItem =  MediaItem.fromUri(Uri.parse(application.getString(R.string.media_url_mp4)))
       // exoPlayer.setMediaItems(mediaItemsList)
        val secondMediaItem =  MediaItem.fromUri(Uri.parse(application.getString(R.string.media_url_mp3)))
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.setMediaItem(secondMediaItem)
        exoPlayer.prepare()
        setListenerToPlayer()
        return exoPlayer
    }

    private fun setListenerToPlayer() {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                when (playbackState) {
                    Player.STATE_READY -> makeLogD("STATE_READY")
                    Player.STATE_BUFFERING -> makeLogD("STATE_BUFFERING")
                    Player.STATE_IDLE -> makeLogD("STATE_IDLE")
                    Player.STATE_ENDED -> makeLogD("STATE_ENDED")
                }
            }

            override fun onTracksInfoChanged(tracksInfo: TracksInfo) {
                super.onTracksInfoChanged(tracksInfo)
                makeLogD(tracksInfo.trackGroupInfos.size.toString())
                makeLogD(tracksInfo.trackGroupInfos.reverse().toString())
                makeLogD("test")
            }
        })
    }

    private fun createMediaSourceFactory() {
        val mediaSourceFactory = DefaultMediaSourceFactory(application)
    }



//    private fun getVideoFromYouTube(url: String) {
//        object : YouTubeExtractor(application) {
//            override fun onExtractionComplete(
//                ytFiles: SparseArray<YtFile>?,
//                videoMeta: VideoMeta?
//            ) {
//                Log.d("ffdfbcv0", ytFiles.toString())
//
//                if (ytFiles != null) {
//                    val videoTag = 137
//                    val audioTag = 140
//
//                    val videoUrl = ytFiles[videoTag].url
//                    val audioUrl = ytFiles[audioTag].url
//
//                    val audioSource: MediaSource = ProgressiveMediaSource
//                        .Factory(DefaultHttpDataSource.Factory())
//                        .createMediaSource(MediaItem.fromUri(audioUrl))
//
//                    val videoSource: MediaSource = ProgressiveMediaSource
//                        .Factory(DefaultHttpDataSource.Factory())
//                        .createMediaSource(MediaItem.fromUri(videoUrl))
//
//                    exoPlayer.setMediaSource(
//                        MergingMediaSource(
//                            true,
//                            videoSource,
//                            audioSource
//                        ),
//                        true
//                    )
//                }
//            }
//        }.extract(url, true, true)
//    }

    private fun makeLogD(message: String) {
        Log.d("makeLogD", message)
    }

    companion object {
        private const val VIDEO_URI_RAW_FOLDER = "android.resource://raw/"
    }
}