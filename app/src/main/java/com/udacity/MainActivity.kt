package com.udacity

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.udacity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager

    private var selectedLink: String? = null
    private var selectedFileNameURL: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        val abc = binding.mainLayout

        abc.customButton.setOnClickListener {
            if (selectedLink != null) {
                abc.customButton.buttonState = ButtonState.Loading
                download()
            }
        }
        createNotifyChannel(CHANNEL_ID, "Notify Channel Name")
    }

    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("Range")
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            binding.mainLayout.customButton.buttonState = ButtonState.Completed

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

            val query = DownloadManager.Query()
            query.setFilterById(id!!)

            val cursor = downloadManager.query(query)
            if (cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))

                var statusDownload = "Fail"
                if (DownloadManager.STATUS_SUCCESSFUL == status) {
                    statusDownload = "Success"
                }

                notificationManager.sendNotification(
                    applicationContext,
                    statusDownload,
                    selectedFileNameURL!!,
                    CHANNEL_ID,
                    getString(R.string.notification_description)
                )
            }
        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(selectedLink))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    private fun createNotifyChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notifyChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            notifyChannel.enableLights(true)
            notifyChannel.lightColor = Color.RED
            notifyChannel.enableVibration(true)
            notifyChannel.description = getString(R.string.notification_description)

            notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notifyChannel)
        }
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton && view.isChecked) {

            when (view.getId()) {
                R.id.radio_glide ->
                    selectedLink = LINK_GLIDE

                R.id.radio_load_app ->
                    selectedLink = LINK_LOAD_APP

                R.id.radio_retrofit ->
                    selectedLink = LINK_RETROFIT
            }

            selectedFileNameURL = view.text.toString()
        }
    }

    companion object {
        private const val LINK_GLIDE =
            "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
        private const val LINK_LOAD_APP =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/refs/heads/master.zip"
        private const val LINK_RETROFIT =
            "https://github.com/square/retrofit/archive/refs/heads/master.zip"
        private const val CHANNEL_ID = "channelId"
    }
}