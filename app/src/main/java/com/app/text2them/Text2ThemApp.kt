package com.app.text2them

import android.app.Application

class Text2ThemApp : Application(){
    override fun onCreate() {
        super.onCreate()
        text2ThemApp = this
    }

    companion object {
        @JvmStatic
        var text2ThemApp: Text2ThemApp? = null
            private set
    }
}