package com.example.multimedia.presentation.exoPlayerActivity

import android.app.Notification
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.scheduler.Scheduler
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File


class MediaService : DownloadService(NOTIFICATION_ID) {

    override fun getDownloadManager(): DownloadManager {
        return createDownloadManager()
    }

    override fun getScheduler(): Scheduler? {
        TODO("Not yet implemented")
    }

    override fun getForegroundNotification(
        downloads: MutableList<Download>,
        notMetRequirements: Int
    ): Notification {
        TODO("Not yet implemented")
    }

    private fun createDownloadManager(): DownloadManager {
        val dataBaseProvider = StandaloneDatabaseProvider(this)
        val cacheDir = File(getExternalFilesDir(null), "Test")
        val downloadCache = SimpleCache(cacheDir, NoOpCacheEvictor(), dataBaseProvider)
        val dataSourceFactory = DefaultHttpDataSource.Factory()
        val downloadExecutor = Runnable::run

        return DownloadManager(
            this,
            dataBaseProvider,
            downloadCache,
            dataSourceFactory,
            downloadExecutor
        )
    }

    companion object {
        private const val NOTIFICATION_ID = 8
    }
}