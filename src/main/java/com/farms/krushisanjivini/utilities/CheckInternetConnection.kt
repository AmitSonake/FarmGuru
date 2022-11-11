package com.farms.krushisanjivini.utilities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AlertDialog
import com.farms.krushisanjivini.login.LoginActivity

object CheckInternetConnection {

    public fun checkForInternet(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {

                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    public fun showAlertDialog(infoMessage:String?,context:Context){
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage(infoMessage)
            .setCancelable(false)
            .setPositiveButton("OK"){dialog,_->dialog.cancel()}
        val alert =dialogBuilder.create()
        alert.setTitle("Information")
        alert.show()
    }
    public fun showRegisteredDialog(infoMessage:String?,context:Context){
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage(infoMessage)
            .setCancelable(false)
            .setPositiveButton("OK"){dialog,_->
                navigateLogin(context)
                dialog.cancel()
            }
        val alert =dialogBuilder.create()
        alert.setTitle("Information")
        alert.show()
    }

    public fun showSessionTimeOutDialog(infoMessage:String?,context:Context){
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage(infoMessage)
            .setCancelable(false)
            .setPositiveButton("OK"){dialog,_->dialog.cancel()
            navigateLogin(context)}
        val alert =dialogBuilder.create()
        alert.setTitle("Information")
        alert.show()
    }
    private fun navigateLogin(applicationContext: Context){
        val intent = Intent(applicationContext, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        applicationContext.startActivity(intent)
    }
}