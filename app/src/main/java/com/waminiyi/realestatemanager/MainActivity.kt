package com.waminiyi.realestatemanager

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.waminiyi.realestatemanager.core.messenger.FirebaseService
import com.waminiyi.realestatemanager.core.messenger.model.Message
import com.waminiyi.realestatemanager.core.messenger.model.NotificationX
import com.waminiyi.realestatemanager.core.messenger.di.RetrofitInstance
import com.waminiyi.realestatemanager.core.messenger.model.TargetMessage
import com.waminiyi.realestatemanager.core.messenger.model.TestMessage
import com.waminiyi.realestatemanager.core.messenger.getServiceAccountAccessToken
import com.waminiyi.realestatemanager.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
            val title = binding.etTitle.text.toString()
            val message = binding.etMessage.text.toString()
            if (title.isNotEmpty() && message.isNotEmpty()) {
                TestMessage(
                    Message(
                        hashMapOf(("title" to "topic$title"), ("message" to "title$message")),
                        NotificationX(title, message),
                        TOPIC
                    )
                )
                    .also {
                        sendNotification(it)
                    }

                TestMessage(
                    TargetMessage(
                        hashMapOf(("title" to "target$title"), ("message" to "target$message")),
                        NotificationX(title, message),
                        token
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