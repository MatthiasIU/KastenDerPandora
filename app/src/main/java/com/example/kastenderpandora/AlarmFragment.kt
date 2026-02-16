package com.example.kastenderpandora

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar

data class Alarm(
    val id: Int,
    val hour: Int,
    val minute: Int,
    var enabled: Boolean = true
)

class AlarmFragment : Fragment() {

    private lateinit var rvAlarms: RecyclerView
    private lateinit var tvNoAlarms: TextView
    private lateinit var btnAddAlarm: FloatingActionButton
    private lateinit var alarmAdapter: AlarmAdapter

    private val alarms = mutableListOf<Alarm>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_alarm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvAlarms = view.findViewById(R.id.rvAlarms)
        tvNoAlarms = view.findViewById(R.id.tvNoAlarms)
        btnAddAlarm = view.findViewById(R.id.btnAddAlarm)

        loadAlarms()

        alarmAdapter = AlarmAdapter(
            alarms,
            onToggle = { alarm, enabled ->
                alarm.enabled = enabled
                saveAlarms()
                if (enabled) {
                    scheduleAlarm(alarm)
                } else {
                    cancelAlarm(alarm)
                }
            },
            onDelete = { alarm ->
                cancelAlarm(alarm)
                alarms.remove(alarm)
                saveAlarms()
                alarmAdapter.notifyDataSetChanged()
                updateEmptyState()
            }
        )

        rvAlarms.layoutManager = LinearLayoutManager(requireContext())
        rvAlarms.adapter = alarmAdapter

        btnAddAlarm.setOnClickListener {
            showTimePicker()
        }

        updateEmptyState()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                val alarm = Alarm(
                    id = System.currentTimeMillis().toInt(),
                    hour = hourOfDay,
                    minute = minute,
                    enabled = true
                )
                alarms.add(alarm)
                saveAlarms()
                scheduleAlarm(alarm)
                alarmAdapter.notifyItemInserted(alarms.size - 1)
                updateEmptyState()
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun scheduleAlarm(alarm: Alarm) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java).apply {
            putExtra("alarm_id", alarm.id)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            alarm.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, alarm.hour)
            set(Calendar.MINUTE, alarm.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    private fun cancelAlarm(alarm: Alarm) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            alarm.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    private fun saveAlarms() {
        val prefs = requireContext().getSharedPreferences("alarms", Context.MODE_PRIVATE)
        val jsonArray = JSONArray()
        alarms.forEach { alarm ->
            jsonArray.put(JSONObject().apply {
                put("id", alarm.id)
                put("hour", alarm.hour)
                put("minute", alarm.minute)
                put("enabled", alarm.enabled)
            })
        }
        prefs.edit().putString("alarms_list", jsonArray.toString()).apply()
    }

    private fun loadAlarms() {
        val prefs = requireContext().getSharedPreferences("alarms", Context.MODE_PRIVATE)
        val jsonString = prefs.getString("alarms_list", "[]") ?: "[]"
        val jsonArray = JSONArray(jsonString)
        alarms.clear()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            alarms.add(Alarm(
                id = obj.getInt("id"),
                hour = obj.getInt("hour"),
                minute = obj.getInt("minute"),
                enabled = obj.getBoolean("enabled")
            ))
        }
    }

    private fun updateEmptyState() {
        tvNoAlarms.visibility = if (alarms.isEmpty()) View.VISIBLE else View.GONE
    }
}

class AlarmAdapter(
    private val alarms: List<Alarm>,
    private val onToggle: (Alarm, Boolean) -> Unit,
    private val onDelete: (Alarm) -> Unit
) : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvAlarmTime: TextView = view.findViewById(R.id.tvAlarmTime)
        val switchAlarm: SwitchCompat = view.findViewById(R.id.switchAlarm)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDeleteAlarm)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alarm, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val alarm = alarms[position]
        holder.tvAlarmTime.text = String.format("%02d:%02d", alarm.hour, alarm.minute)
        holder.switchAlarm.setOnCheckedChangeListener(null)
        holder.switchAlarm.isChecked = alarm.enabled
        holder.switchAlarm.setOnCheckedChangeListener { _, isChecked ->
            onToggle(alarm, isChecked)
        }
        holder.btnDelete.setOnClickListener {
            onDelete(alarm)
        }
    }

    override fun getItemCount() = alarms.size
}

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        var mediaPlayer: MediaPlayer? = null

        fun stopAlarm() {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        try {
            val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            mediaPlayer = MediaPlayer.create(context, alarmUri)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
