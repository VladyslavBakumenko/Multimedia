package com.example.multimedia.presentation.exoPlayerActivity

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.multimedia.R
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.util.MimeTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExoPlayerViewModel @Inject constructor(private val application: Application) : ViewModel() {

    private val trackSelector = DefaultTrackSelector(application).apply {
        setParameters(buildUponParameters().setMaxVideoSizeSd())
    }

    var exoPlayer = ExoPlayer.Builder(application)
        .setTrackSelector(trackSelector)
        .build()


    fun setExoPlayer(): ExoPlayer {

        addMediaItemsToPlayer(exoPlayer)
        setListenersToPlayer()
        exoPlayer.prepare()
        return exoPlayer
    }

    private fun setListenersToPlayer() {
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

            override fun onEvents(player: Player, events: Player.Events) {
                if (events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED) ||
                    events.contains(Player.EVENT_IS_PLAYING_CHANGED)
                ) {
                    makeLogD("do something")
                }
            }

            override fun onTracksInfoChanged(tracksInfo: TracksInfo) {
                super.onTracksInfoChanged(tracksInfo)
                makeLogD(tracksInfo.trackGroupInfos.size.toString())
                makeLogD(tracksInfo.trackGroupInfos.reverse().toString())
                makeLogD("test")
            }

            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                Log.d("testMetadata", mediaMetadata.artist.toString())
                Log.d("testMetadata", mediaMetadata.albumArtist.toString())
                Log.d("testMetadata", mediaMetadata.genre.toString())
                Log.d("testMetadata", mediaMetadata.releaseDay.toString())
                Log.d("testMetadata", mediaMetadata.releaseMonth.toString())
                Log.d("testMetadata", mediaMetadata.releaseYear.toString())
            }
        })


    }

    private fun addMediaItemsToPlayer(exoPlayer: ExoPlayer): ExoPlayer {
        val testMediaMetadata = MediaMetadata.Builder()
            .setAlbumArtist("testAlbumArtist")
            .setArtist("testArtist")
            .setGenre("testGenre")
            .setReleaseDay(27)
            .setReleaseMonth(12)
            .setRecordingYear(4)
            .build()

        val testLiveConfig = MediaItem.LiveConfiguration.Builder()
            .setMaxPlaybackSpeed(1.2f)
            .setMaxOffsetMs(45000)
            .build()

        val testAdsUri = Uri.parse(application.getString(R.string.first_media_url_mp3))

        val firstMediaItem =
            MediaItem.fromUri(Uri.parse(application.getString(R.string.first_media_url_mp3)))
        val secondMediaItem =
            MediaItem.fromUri(Uri.parse(application.getString(R.string.second_media_url_mp3)))
        val thirdMediaItem =
            MediaItem.Builder()
                .setAdsConfiguration(MediaItem.AdsConfiguration.Builder(testAdsUri).build())
                .setMediaMetadata(testMediaMetadata)
                .setLiveConfiguration(testLiveConfig)
                .setUri(Uri.parse(application.getString(R.string.media_url_mp4)))
                .build()

        val fourthMediaItem = MediaItem.fromUri(Uri.parse(VIDEO_URI_RAW_FOLDER + R.raw.video_test1))

        val testMediaItem = MediaItem.Builder()
            .setUri(application.getString(R.string.media_url_dash))
            .setMimeType(MimeTypes.APPLICATION_MPD)
            .build()

        with(exoPlayer) {
            setMediaItem(firstMediaItem)
            addMediaItem(secondMediaItem)
            addMediaItem(thirdMediaItem)
            addMediaItem(fourthMediaItem)
            addMediaItem(testMediaItem)
        }
        return exoPlayer
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