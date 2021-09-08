package com.authme.sdk.sample

import android.app.Application

class APP : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: APP

    }
}
