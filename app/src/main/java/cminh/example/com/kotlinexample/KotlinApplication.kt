package cminh.example.com.kotlinexample

import android.app.Application

/**
 * Created by cminh on 7/24/17.
 *
 */
class KotlinApplication: Application {
    lateinit var kotlinComponent: KotlinComponent

    constructor() : super()

    override fun onCreate() {
        super.onCreate()
    }
}