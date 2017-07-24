package cminh.example.com.kotlinexample

import dagger.Module
import dagger.Provides

/**
 * Created by cminh on 7/24/17.
 *
 */
@Module
class KotlinModule(val context: KotlinApplication) {
    @Provides
    fun provideContext() = context
}