package com.waminiyi.realestatemanager.features.splashScreen

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.waminiyi.realestatemanager.databinding.ActivityOrganizerBinding
import com.waminiyi.realestatemanager.features.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrganizerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrganizerBinding

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var permissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrganizerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                permissionGranted = true
                navigateToHome()
            } else {
                showNotificationDeniedDialog()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !isNotificationPermissionGranted()) {
            requestPermission()
        } else {
            permissionGranted = true
        }

    }

    override fun onStart() {
        super.onStart()
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            if (permissionGranted) {
                navigateToHome()
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                !isNotificationPermissionGranted()
            ) {
                requestPermission()
            }
        }, 3000)
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    fun requestPermission() {
        requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    fun isNotificationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showNotificationDeniedDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Notification permission")
            .setMessage("You need to enable notification to use this app. Go to app settings if needed")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .show()
    }
}