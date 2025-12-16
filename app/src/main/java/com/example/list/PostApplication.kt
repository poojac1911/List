package com.example.list

import android.app.Application
import com.example.list.data.AppContainer
import com.example.list.data.AppDataContainer

class PostApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}