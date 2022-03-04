package com.lahsuak.fitnesstracker.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.lahsuak.fitnesstracker.R

private const val READ_EXTERNAL_STORAGE_REQUEST = 0x1045

class ScreenActivity : AppCompatActivity() {

    private lateinit var screenshotDetector: ScreenshotDetector
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen)
        screenshotDetector = ScreenshotDetector(baseContext)
    }

    override fun onStart() {
        super.onStart()
        detectScreenshots()
    }

    override fun onStop() {
        super.onStop()
        screenshotDetector.stop()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            READ_EXTERNAL_STORAGE_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    detectScreenshots()
                }
                return
            }
        }
    }

    private fun haveStoragePermission() =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission() {
        if (!haveStoragePermission()) {
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(this, permissions, READ_EXTERNAL_STORAGE_REQUEST)
        }
    }

    private fun detectScreenshots() {
        if (haveStoragePermission()) {
            screenshotDetector.start()
        } else {
            requestPermission()
        }
    }
}