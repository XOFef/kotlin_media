package com.example.keqingmedia

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity:AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var runnable:Runnable
    private var handler: Handler = Handler()
    private var pause:Boolean = false
    private lateinit var seekBar: SeekBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val playBtn = findViewById<Button>(R.id.playBtn)
        val pauseBtn = findViewById<Button>(R.id.pauseBtn)
        val stopBtn = findViewById<Button>(R.id.stopBtn)
        seekBar = findViewById(R.id.audio)
// Инициализация MediaPlayer
        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.audio)

        // Установка максимального значения SeekBar
        seekBar.max = mediaPlayer.duration

        // Обновление SeekBar во время воспроизведения
        runnable = Runnable {
            seekBar.progress = mediaPlayer.currentPosition
            handler.postDelayed(runnable, 1000) // Обновление каждую секунду
        }

        // Кнопка Запуска аудиофайла
        playBtn.setOnClickListener {
            if (pause) {
                mediaPlayer.seekTo(mediaPlayer.currentPosition)
                mediaPlayer.start()
                pause = false
                Toast.makeText(this, "Media playing", Toast.LENGTH_SHORT).show()
            } else {
                mediaPlayer.start()
                Toast.makeText(this, "Media playing", Toast.LENGTH_SHORT).show()
            }
            playBtn.isEnabled = false
            pauseBtn.isEnabled = true
            stopBtn.isEnabled = true
            handler.postDelayed(runnable, 1000) // Запуск обновления SeekBar
        }

        // Кнопка паузы аудиофайла
        pauseBtn.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                pause = true
                playBtn.isEnabled = true
                pauseBtn.isEnabled = false
                stopBtn.isEnabled = true
                Toast.makeText(this, "Media paused", Toast.LENGTH_SHORT).show()
            }
        }

        // Кнопка Остановки аудиофайла
        stopBtn.setOnClickListener {
            if (mediaPlayer.isPlaying || pause) {
                pause = false
                mediaPlayer.stop()
                mediaPlayer.reset()
                mediaPlayer.release()
                handler.removeCallbacks(runnable)
                playBtn.isEnabled = true
                pauseBtn.isEnabled = false
                stopBtn.isEnabled = false
                Toast.makeText(this, "Media stopped", Toast.LENGTH_SHORT).show()
            }
        }

        // Установка слушателя для SeekBar
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Слушатель завершения воспроизведения
        mediaPlayer.setOnCompletionListener {
            playBtn.isEnabled = true
            pauseBtn.isEnabled = false
            stopBtn.isEnabled = false
            handler.removeCallbacks(runnable)
            Toast.makeText(this, "End", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}