package com.example.musicplayer

import android.content.Intent
import android.os.IBinder
import android.app.Service
import android.media.MediaPlayer
import android.util.Log
import kotlin.math.log

class MusicService : Service() {
    private var tracks = listOf(
        "https://api.hugo.red/api/v2/objects/file/crb305fqt8cqw88zt3.mp3",
        "https://api.hugo.red/api/v2/objects/file/94zdycy5o6sed711ik.mp3"
    )
    private var currentTrackIndex = 0
    private var mediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            "PLAY" -> playMusic()
            "PAUSE" -> pauseMusic()
            "NEXT" -> nextMusic()
        }
        return START_STICKY
    }

    private fun playMusic() {
        if (mediaPlayer == null){
            mediaPlayer = MediaPlayer().apply {
                setDataSource(tracks[currentTrackIndex])
                prepare()
                start()
                Log.i("MusicService", "Start playing music")
                setOnCompletionListener {
                    it.start()
                }
            }
        } else {
            mediaPlayer?.start()
        }
    }

    private fun pauseMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(tracks[currentTrackIndex])
                prepare()
                pause()
                Log.i("MusicService", "Paused")
            }
        } else {
            mediaPlayer?.pause()
        }
    }

    private fun nextMusic(){
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.reset()
            currentTrackIndex = (currentTrackIndex + 1) % tracks.size
            mediaPlayer!!.setDataSource(tracks[currentTrackIndex])
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
            Log.i("MusicService", "Starting playing next")
        }
    }
}