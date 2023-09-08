package com.udacity

import android.app.NotificationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.databinding.ActivityDetailBinding

const val DETAIL_ACTIVITY_STATUS_KEY = "Status"
const val DETAIL_ACTIVITY_FILE_KEY = "File"

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val notifyManager = getSystemService(NotificationManager::class.java)
        notifyManager.cancelAll()

        val fileName = intent.getStringExtra(DETAIL_ACTIVITY_STATUS_KEY)
        binding.contentDetail.fileNameValue.text = fileName

        val status = intent.getStringExtra(DETAIL_ACTIVITY_FILE_KEY)
        binding.contentDetail.statusValue.text = status

        binding.contentDetail.button.setOnClickListener {
            finish()
        }
    }
}
