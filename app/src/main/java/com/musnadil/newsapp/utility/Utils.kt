package com.musnadil.newsapp.utility

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.view.View
import android.view.Window
import android.view.animation.AlphaAnimation
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

fun getApiKey(application: Application): String {
    val applicationInfo: ApplicationInfo = application.packageManager
        .getApplicationInfo(application.packageName, PackageManager.GET_META_DATA)
    val apiKey = applicationInfo.metaData["NEWS_KEY"]
    return apiKey.toString()
}

fun setFullScreen(window: Window, fitSystemWindow: Boolean = false) {
    WindowCompat.setDecorFitsSystemWindows(window, fitSystemWindow)
}

fun appearanceWindow(window: Window, statusBarLight: Boolean = true, bottomNavLight: Boolean = true) {
    val wic = WindowInsetsControllerCompat(window, window.decorView)
    wic.isAppearanceLightStatusBars = statusBarLight
    wic.isAppearanceLightNavigationBars = bottomNavLight
}

fun hideSystemUI(window: Window) {
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
}

fun showSystemUI(window: Window) {
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
}

fun showViewSmoothly(view: View) {
    val fadeIn = AlphaAnimation(0f, 1f)
    fadeIn.duration = 800
    view.startAnimation(fadeIn)
    view.visibility = View.VISIBLE
}

fun hideViewSmoothly(view: View) {
    val fadeOut = AlphaAnimation(1f, 0f)
    fadeOut.duration = 1000
    view.startAnimation(fadeOut)
    view.visibility = View.GONE
}
