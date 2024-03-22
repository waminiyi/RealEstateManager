package com.waminiyi.realestatemanager

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.waminiyi.realestatemanager.core.messenger.FirebaseService
import com.waminiyi.realestatemanager.core.messenger.getServiceAccountAccessToken
import com.waminiyi.realestatemanager.core.messenger.network.FirebaseMessengerAPI
import com.waminiyi.realestatemanager.databinding.ActivityOrganizerBinding
import com.waminiyi.realestatemanager.features.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


const val TOPIC = "NEWS"
private const val PERMISSION_REQUEST_CODE = 123

@AndroidEntryPoint
class OrganizerActivity : AppCompatActivity() {
    val TAG = "OrganizerActivity"
    private lateinit var binding: ActivityOrganizerBinding
    private lateinit var token: String
    private lateinit var firebaseMessengerAPI: FirebaseMessengerAPI

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    var permissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrganizerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        val options: FirebaseOptions =
//            FirebaseOptions.Builder().setApiKey(GoogleCredentials.getApplicationDefault().toString()).build()

        FirebaseApp.initializeApp(this)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            token = task.result

            binding.etToken.setText(token)
        })
        FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
        Log.d("<TOPIC>", "suscribed")

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                permissionGranted = true
            } else {
                // Permission denied
                showNotificationDeniedDialog()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !isNotificationPermissionGranted()) {
            requestPermission()
        }else{
            permissionGranted = true

        }


        binding.btnSend.setOnClickListener {
            if (permissionGranted) navigateToHome()
            /*val remoteChanges = mutableListOf<RemoteChange>()
            for (i in 1..20) {
                remoteChanges.add(
                    RemoteChange(
                        id = "object_id_$i",
                        classTag = "ObjectClass",
                        version = i.toLong(),
                        isDeleted = false
                    )
                )
            }
            // Creating RemoteCommit with 20 changes
            val remoteCommit = RemoteCommit(
                commitId = "commit_id_123",
                remoteChanges = remoteChanges,
                timestamp = Timestamp(System.currentTimeMillis()).time,
                authorId = "author123"
            )
            val title = binding.etTitle.text.toString()
            val message = binding.etMessage.text.toString()
            if (title.isNotEmpty() && message.isNotEmpty()) {
                RemMessage()
                    .setTopic(TOPIC)
                    .setData(
                        hashMapOf(
                            ("title" to "topic $title"),
                            ("message" to "topic $message")
                        )
                    )
                    .also {
                        sendNotification(it)
                    }

            }
        }*/
        }


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

    private fun sendNotification(notification: Any) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = firebaseMessengerAPI.postMessage(
                "Bearer " + getServiceAccountAccessToken(this@OrganizerActivity), notification
            )
            if (response.isSuccessful) {
                Log.d(TAG, " Successful Response: $response")
            } else {
                Log.e(TAG, response.errorBody()?.string().orEmpty())
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

//    private fun sendMessage(token: String, title: String, body: String) {
//
//        val message: Message = Message.builder()
//            .putData("title", title)
//            .putData("body", body)
//            .setToken(token)
//            .build()
//        val remoteMessage=RemoteMessage()
//
//        val response: String = FirebaseMessaging.getInstance().
//// Response is a message ID string.
//// Response is a message ID string.
//        println("Successfully sent message: $response")
//
//    }


    private fun navigateToHome() {
        // Skip signing in, navigate to the main activity
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish() // Finish the current activity to prevent going back to it
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