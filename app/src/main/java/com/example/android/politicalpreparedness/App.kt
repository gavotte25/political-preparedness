package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.utils.repoModule
import com.example.android.politicalpreparedness.utils.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                    viewModelModule,
                    repoModule
            )
        }
    }
}