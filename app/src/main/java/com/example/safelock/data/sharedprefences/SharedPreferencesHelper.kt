package com.example.safelock.data.sharedprefences

import android.content.Context

class SharedPreferencesHelper(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    fun isFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean("is_first_launch", true)
    }

    fun setFirstLaunch(isFirst: Boolean) {
        sharedPreferences.edit().putBoolean("is_first_launch", isFirst).apply()
    }
}