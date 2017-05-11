package com.gocation.gocation_android.app

import android.app.Application
import com.facebook.FacebookSdk

/**
 * Created by dylanlange on 11/05/17.
 */

class GocationApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        FacebookSdk.sdkInitialize(this)
    }
}