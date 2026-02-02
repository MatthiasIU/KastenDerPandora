package com.example.kastenderpandora

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

class SirenActivity : BaseToolActivity() {

    override val toolTitle = R.string.siren
    override val layoutResId = R.layout.activity_siren
    override val toolIcon = R.mipmap.ic_siren

    private lateinit var bnToggle: Button
    private lateinit var tvStatus: TextView
    private lateinit var radioGroupPattern: RadioGroup
    private lateinit var seekBarVolume: SeekBar
    private lateinit var seekBarFrequency: SeekBar
    private lateinit var tvVolumeValue: TextView
    private lateinit var tvFrequencyValue: TextView

    private var audioTrack: AudioTrack? = null
    private var audioJob: Job? = null
    private val audioScope = CoroutineScope(Dispatchers.IO)

    private var isSirenOn = false
    private var volume = 0.8f
    private var frequency = 1000f
    private var sirenPattern = SirenPattern.LOUD

    private enum class SirenPattern {
        LOUD,
        SOS
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
        initializeDisplayValues()
        setupListeners()
        updateUI()
    }

    private fun setupViews() {
        bnToggle = findViewById(R.id.bnToggle)
        tvStatus = findViewById(R.id.tvStatus)
        radioGroupPattern = findViewById(R.id.radioGroupPattern)
        seekBarVolume = findViewById(R.id.seekBarVolume)
        seekBarFrequency = findViewById(R.id.seekBarFrequency)
        tvVolumeValue = findViewById(R.id.tvVolumeValue)
        tvFrequencyValue = findViewById(R.id.tvFrequencyValue)
    }

    private fun initializeDisplayValues() {
        tvVolumeValue.text = getString(R.string.siren_volume_value, seekBarVolume.progress)
        tvFrequencyValue.text = getString(R.string.siren_frequency_value, seekBarFrequency.progress)
    }

    private fun setupListeners() {
        bnToggle.setOnClickListener {
            toggleSiren()
        }

        radioGroupPattern.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbLoud -> sirenPattern = SirenPattern.LOUD
                R.id.rbSos -> sirenPattern = SirenPattern.SOS
            }
            if (isSirenOn) {
                stopSiren()
                startSiren()
            }
        }

        seekBarVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                volume = progress / 100f
                tvVolumeValue.text = getString(R.string.siren_volume_value, progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        seekBarFrequency.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                frequency = progress.toFloat()
                tvFrequencyValue.text = getString(R.string.siren_frequency_value, progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun toggleSiren() {
        if (isSirenOn) {
            stopSiren()
        } else {
            startSiren()
        }
        updateUI()
    }

    private fun startSiren() {
        isSirenOn = true
        audioTrack = createAudioTrack()
        audioTrack?.play()

        audioJob = audioScope.launch {
            when (sirenPattern) {
                SirenPattern.LOUD -> playLoudTone()
                SirenPattern.SOS -> playSOS()
            }
        }
    }

    private fun stopSiren() {
        isSirenOn = false
        audioJob?.cancel()
        audioJob = null
        audioTrack?.stop()
        audioTrack?.release()
        audioTrack = null
    }

    private fun createAudioTrack(): AudioTrack {
        val sampleRate = 44100
        val bufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        return AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(bufferSize)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()
    }

    private fun playLoudTone() {
        val sampleRate = 44100
        val buffer = ShortArray(sampleRate / 10)

        while (audioJob?.isActive == true) {
            for (i in buffer.indices) {
                val t = i.toDouble() / sampleRate
                val amplitude = sin(2 * PI * frequency * t) * Short.MAX_VALUE * volume
                buffer[i] = amplitude.toInt().toShort()
            }

            audioTrack?.write(buffer, 0, buffer.size)
        }
    }

    private suspend fun playSOS() {
        val sampleRate = 44100
        val shortDotDuration = 200L
        val dashDuration = 400L
        val gapDuration = 200L
        val letterGap = 400L
        val wordGap = 800L

        val sosPattern = listOf(
            shortDotDuration, gapDuration, shortDotDuration, gapDuration, shortDotDuration, letterGap,
            dashDuration, gapDuration, dashDuration, gapDuration, dashDuration, letterGap,
            shortDotDuration, gapDuration, shortDotDuration, gapDuration, shortDotDuration, wordGap
        )

        while (audioJob?.isActive == true) {
            for ((index, duration) in sosPattern.withIndex()) {
                if (audioJob?.isActive != true) break

                if (index % 2 == 0) {
                    playToneForDuration(duration, sampleRate)
                } else {
                    playSilenceForDuration(duration, sampleRate)
                }
            }
        }
    }

    private suspend fun playToneForDuration(durationMs: Long, sampleRate: Int) {
        val numSamples = ((durationMs * sampleRate) / 1000).toInt()
        val buffer = ShortArray(numSamples)

        for (i in buffer.indices) {
            val t = i.toDouble() / sampleRate
            val amplitude = sin(2 * PI * frequency * t) * Short.MAX_VALUE * volume
            buffer[i] = amplitude.toInt().toShort()
        }

        audioTrack?.write(buffer, 0, buffer.size)
        delay(durationMs)
    }

    private suspend fun playSilenceForDuration(durationMs: Long, sampleRate: Int) {
        val numSamples = ((durationMs * sampleRate) / 1000).toInt()
        val buffer = ShortArray(numSamples)

        audioTrack?.write(buffer, 0, buffer.size)
        delay(durationMs)
    }

    private fun updateUI() {
        if (isSirenOn) {
            bnToggle.setText(R.string.siren_stop)
            bnToggle.setBackgroundColor(ContextCompat.getColor(this, com.google.android.material.R.color.material_dynamic_primary40))
            bnToggle.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            tvStatus.setText(R.string.siren_status_on)
            tvStatus.alpha = 1f
        } else {
            bnToggle.setText(R.string.siren_start)
            bnToggle.setBackgroundColor(ContextCompat.getColor(this, com.google.android.material.R.color.material_dynamic_neutral90))
            bnToggle.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            tvStatus.setText(R.string.siren_status_off)
            tvStatus.alpha = 0.7f
        }
    }

    override fun onPause() {
        super.onPause()
        if (isSirenOn) {
            stopSiren()
            updateUI()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSiren()
    }
}
