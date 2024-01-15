package com.example.appstory.ui.splashscreen

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.appstory.R
import com.example.appstory.ui.authenticate.AuthActivity
import com.example.appstory.ui.dashboard.MainActivity
import com.example.appstory.utils.SharedPreferencesHelper
import com.example.appstory.utils.SharedPreferencesHelper.Companion.KEY_LOGIN_STATUS
import com.google.android.material.snackbar.Snackbar

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var loadingBar: ProgressBar
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        sharedPreferencesHelper = SharedPreferencesHelper(this)

        loadingBar = findViewById(R.id.loadingBar)
        animateProgressBar(loadingBar)

        Handler(Looper.getMainLooper()).postDelayed({
            checkInternetConnection()
        }, 3000)
    }

    private fun animateProgressBar(progressBar: ProgressBar) {
        val animation = ObjectAnimator.ofInt(progressBar, "progress", 0, 100)
        animation.duration = 3000
        animation.interpolator = AccelerateDecelerateInterpolator()
        animation.start()
    }

    private fun checkInternetConnection() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
            startMainActivity()
        } else {
            Snackbar.make(loadingBar, "Tidak ada koneksi internet", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun startMainActivity() {
        val isLoggedIn = sharedPreferencesHelper.getBoolean(KEY_LOGIN_STATUS, false)
        if (isLoggedIn) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }

        finish()

    }
}
