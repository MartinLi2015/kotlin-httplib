package com.zxkj.httplib

import android.app.Application
import android.content.Context

class MyApplication : Application() {



    companion object{
        private lateinit var application: MyApplication
        fun getApp(): Application {
            return application
        }
    }
    override fun onCreate() {
        super.onCreate()
        application = this
    }

}