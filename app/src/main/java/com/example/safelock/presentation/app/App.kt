package com.example.safelock.presentation.app

import android.app.Application
import android.content.SharedPreferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App:Application() {


    override fun onCreate() {
        super.onCreate()
    }

}