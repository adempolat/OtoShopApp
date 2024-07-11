package com.adempolat.otovinnapp.utils

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.adempolat.otovinnapp.MainActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import com.adempolat.otovinnapp.R

fun Context.showCustomToast(message: String, @ColorInt backgroundColor: Int, iconResId: Int? = null) {
    val inflater = LayoutInflater.from(this)
    val layout: View = inflater.inflate(R.layout.custom_toast, null)

    val toastText = layout.findViewById<TextView>(R.id.toast_text)
    toastText.text = message
    toastText.setTextColor(Color.BLACK)

    iconResId?.let {
        val toastIcon = layout.findViewById<ImageView>(R.id.toast_icon)
        toastIcon.setImageResource(it)
        toastIcon.visibility = View.VISIBLE
    }

    layout.setBackgroundColor(backgroundColor)

    val toast = Toast(this)
    toast.duration = Toast.LENGTH_SHORT
    toast.view = layout
    toast.show()
}

fun ImageView.loadImage(url: String, cornerRadius: Int = 16) {
    Glide.with(this.context)
        .load(url)
        .transform(RoundedCorners(cornerRadius))
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

fun ImageView.loadStoryImage(url: String) {
    Glide.with(this.context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

fun ImageView.loadCircularImage(url: String, borderSize: Int = 4, borderColor: Int = Color.LTGRAY) {
    Glide.with(this.context)
        .load(url)
        .transform(CropCircleWithBorderTransformation(borderSize, borderColor))
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}
 fun saveToken(token: String,activity: Context) {
    val sharedPref = activity.getSharedPreferences("app_prefs", Context.MODE_PRIVATE) ?: return
    with(sharedPref.edit()) {
        putString("TOKEN_KEY", token)
        apply()
    }
}
