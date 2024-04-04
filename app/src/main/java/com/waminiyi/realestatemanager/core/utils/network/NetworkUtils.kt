package com.waminiyi.realestatemanager.core.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Class for useful network functions
 */
class NetworkUtils @Inject constructor(@ApplicationContext private val context: Context) {

    /**
     * @return a boolean indicating whether an internet connection is available or not
     */
    fun isInternetAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        if (network != null) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            return networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true ||
                    networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
        }
        return false
    }
}