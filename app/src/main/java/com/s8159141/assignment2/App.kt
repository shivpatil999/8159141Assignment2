package com.s8159141.assignment2

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class annotated with [HiltAndroidApp] so Hilt can generate the
 * dependency graph and inject into Activities and ViewModels.
 */
@HiltAndroidApp
class App : Application()
