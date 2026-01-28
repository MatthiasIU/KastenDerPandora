package com.example.kastenderpandora

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.math.log10
import kotlin.math.sqrt
import java.util.ArrayDeque

class DecibelMeterActivity : BaseToolActivity() {

    override val toolTitle = R.string.decibel_meter
    override val layoutResId = R.layout.activity_decibel_meter
    override val enableSwipeBack = true

    private lateinit var tvCurrentDb: TextView
    private lateinit var tvRange1: TextView
    private lateinit var tvRange2: TextView
    private lateinit var tvRange3: TextView
    private lateinit var tvRange4: TextView
    private lateinit var tvRange5: TextView
    private lateinit var tvStats: TextView

    private var audioRecord: AudioRecord? = null
    private var recordingThread: Thread? = null
    private var isRecording = false

    private val handler = Handler(Looper.getMainLooper())
    private val dbWindow = ArrayDeque<Pair<Long, Float>>() // timestamp, db

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tvCurrentDb = findViewById(R.id.tvCurrentDb)
        tvRange1 = findViewById(R.id.tvRange1)
        tvRange2 = findViewById(R.id.tvRange2)
        tvRange3 = findViewById(R.id.tvRange3)
        tvRange4 = findViewById(R.id.tvRange4)
        tvRange5 = findViewById(R.id.tvRange5)
        tvStats = findViewById(R.id.tvStats)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            == PackageManager.PERMISSION_GRANTED
        ) {
            startMeter()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                1001
            )
        }
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1001 &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            startMeter()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopMeter()
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    private fun startMeter() {
        val sampleRate = 44100
        val bufferSize = AudioRecord.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )

        audioRecord?.startRecording()
        isRecording = true

        recordingThread = Thread {
            val buffer = ShortArray(bufferSize)

            while (isRecording) {
                val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                if (read > 0) {
                    val rms = sqrt(
                        buffer.take(read)
                            .map { it.toDouble() * it }
                            .average()
                            .coerceAtLeast(1.0)
                    )

                    val db = (20 * log10(rms)).toFloat()

                    handler.post {
                        updateUi(db)
                    }
                }
            }
        }

        recordingThread?.start()
    }

    private fun stopMeter() {
        isRecording = false
        recordingThread?.interrupt()
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }

    private fun updateUi(db: Float) {
        tvCurrentDb.text = getString(
            R.string.db_current,
            db.toInt()
        )

        val now = System.currentTimeMillis()
        dbWindow.addLast(now to db)

        while (dbWindow.isNotEmpty() && now - dbWindow.first().first > 10_000) {
            dbWindow.removeFirst()
        }

        val values = dbWindow.map { it.second }
        val min = values.minOrNull()?.toInt() ?: 0
        val max = values.maxOrNull()?.toInt() ?: 0
        val avg = if (values.isNotEmpty()) values.average().toInt() else 0

        tvStats.text = getString(
            R.string.db_stats,
            min,
            max,
            avg
        )

        highlightRange(db)
    }

    private fun highlightRange(db: Float) {
        listOf(tvRange1, tvRange2, tvRange3, tvRange4, tvRange5)
            .forEach { it.alpha = 0.4f }

        when (db.toInt()) {
            in 0..45 -> tvRange1.alpha = 1f
            in 46..60 -> tvRange2.alpha = 1f
            in 61..80 -> tvRange3.alpha = 1f
            in 81..115 -> tvRange4.alpha = 1f
            else -> tvRange5.alpha = 1f
        }
    }
}
