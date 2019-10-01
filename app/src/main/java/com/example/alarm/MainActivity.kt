package com.example.alarm

import android.app.*
import android.content.Context
import android.content.Intent
import android.database.DataSetObserver
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.widget.TextViewCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.activity_main.*
import java.nio.channels.Channel
import java.text.DateFormat
import java.util.*

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener ,TimePickerDialog.OnTimeSetListener{


    private lateinit var textView1:TextView
    private val CHANNEL_1_ID = "channel 1"
    private var text1 = ""
    private var text = ""
    private lateinit var notificationbutton: AnimationDrawable


    private fun extractStringFromSpinner(text: String) {

        if (text == "Shopping" || text == "Drink Water" || text == "Exercise" || text == "Move up from the Bed" || text == "Take A Walk" || text == "Bath")

            text1 = text
    }

   private fun startAlarm(calendar: Calendar) {

        val alarmManager:AlarmManager =getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent= Intent(this,AlertDetails::class.java)
        val pendingIntent:PendingIntent= PendingIntent.getBroadcast(this,123,intent,0)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent)
        Toast.makeText(this,"Alarm will Triggered at the set Time ",Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView1=findViewById(R.id.textView2)

        val textView: TextView = findViewById(R.id.textView1)
        val spinner: Spinner = findViewById(R.id.notificationSpinner)
        val button = findViewById<Button>(R.id.button1)
        val button1=findViewById<Button>(R.id.button)



        spinner.onItemSelectedListener = this
        spinner.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.notificationNames,
            android.R.layout.simple_dropdown_item_1line
        ).also { adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_item) }


        val notification = findViewById<ImageView>(R.id.imageView).apply {
            setBackgroundResource(R.drawable.notificationchanges)
            notificationbutton = background as AnimationDrawable
            notificationbutton.start()

        }

        button.setOnClickListener {

            val intent = Intent(this, AlertDetails::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

            val descriptionText = getString(R.string.channel_description)
            val channel: NotificationChannel =
                NotificationChannel(
                    CHANNEL_1_ID, "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = descriptionText
                }
            channel.enableLights(true)
            channel.lightColor = Color.BLUE
            channel.enableVibration(true)
            channel.vibrationPattern


            val builder = NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_add_alert_black_24dp)
                .setContentTitle("Your Alarm")
                .setContentText(text1)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .build()

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            notificationManager.notify(1234, builder)
        }

        button1.setOnClickListener{
          startAlarm(calendar = Calendar.getInstance())
        }



    }



    override fun onNothingSelected(p0: AdapterView<*>?) {

        Toast.makeText(this, "Select", Toast.LENGTH_SHORT).show()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        this.text = p0?.getItemAtPosition(p2).toString()
        extractStringFromSpinner(text)

        val calendar = Calendar.getInstance()
        val timePicker = TimePickerDialog(
            this, TimePickerDialog.OnTimeSetListener { view, hourofday, minute -> },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
        timePicker.show()
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        val calendar:Calendar= Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,p1)
        calendar.set(Calendar.MINUTE,p2)
    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.notificationlist, menu)
        return true
    }
}
