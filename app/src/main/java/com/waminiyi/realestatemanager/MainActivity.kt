package com.waminiyi.realestatemanager

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.waminiyi.realestatemanager.core.data.remote.model.RemoteChange
import com.waminiyi.realestatemanager.core.data.remote.model.RemoteCommit
import com.waminiyi.realestatemanager.core.messenger.FirebaseService
import com.waminiyi.realestatemanager.core.messenger.di.RetrofitInstance
import com.waminiyi.realestatemanager.core.messenger.getServiceAccountAccessToken
import com.waminiyi.realestatemanager.core.messenger.model.RemMessage
import com.waminiyi.realestatemanager.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Timestamp


const val TOPIC = "NEWS"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private lateinit var token: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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

        binding.btnSend.setOnClickListener {
            val remoteChanges = mutableListOf<RemoteChange>()
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
            val gson = Gson()
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
                            ("message" to "topic $message"),
                            ("commit" to gson.toJson(remoteCommit))
                        )
                    )
                    .also {
                        sendNotification(it)
                    }

            }
        }
    }

    private fun sendNotification(notification: Any) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postMessage(
                "Bearer " + getServiceAccountAccessToken(this@MainActivity), notification
            )
            if (response.isSuccessful) {
                Log.d(TAG, " Successful Response: ${Gson().toJson(response)}")
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
}