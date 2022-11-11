package com.farms.krushisanjivini.utilities.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.farms.krushisanjivini.R

val PERMISSION_SEND_SMS = 200
fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }

}

fun ImageView.loadImage(uri: String?, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_launcher)
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .into(this)
}


fun loadImage(view: ImageView, url: String?) {
    view.loadImage(url, getProgressDrawable(view.context))
}

fun showSubscriptionAlert(context: Context, message:String){
    val builder = context?.let { AlertDialog.Builder(it) }
    builder?.setTitle("Alert")
    builder?.setMessage(message)
    builder?.setCancelable(false)
    builder?.setPositiveButton(android.R.string.ok) { dialog, which ->
        openAppInGooglePlay(context as Activity)
        dialog.dismiss()
        context.finish()
    }

  /*  builder?.setNegativeButton(android.R.string.no) { dialog, which ->
        dialog.dismiss()
    }*/

    builder?.show()
}


fun showSubscriptionAlertReminder(context: Context, message:String) {
    val builder = context?.let { AlertDialog.Builder(it) }
    builder?.setTitle("Alert")
    builder?.setMessage(message)
    builder?.setPositiveButton(android.R.string.ok) { dialog, which ->

        dialog.dismiss()
    }   /*builder?.setNegativeButton(android.R.string.no) { dialog, which ->
        dialog.dismiss()
    }
*/
    builder?.show()
}

fun closeAppAlert(context: Activity, message:String) {
    val builder = context?.let { AlertDialog.Builder(it) }
    builder?.setTitle("Alert")
    builder?.setMessage(message)
    builder?.setPositiveButton(android.R.string.ok) { dialog, which ->

        dialog.dismiss()
        context!!.finish()
    }
    builder?.setNegativeButton(android.R.string.cancel) { dialog, which ->
        dialog.dismiss()
    }

    builder?.show()
}

fun openAppInGooglePlay(activity: Activity){

    val appId ="com.farms.krushisanjivini"/* BuildConfig.APPLICATION_ID*/
    try {
        activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appId")))
    } catch (anfe: ActivityNotFoundException) {
        activity.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appId")
            )
        )
    }
}