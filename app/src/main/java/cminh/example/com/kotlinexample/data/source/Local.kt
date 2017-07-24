package cminh.example.com.kotlinexample.data.source

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Qualifier

/**
 * Created by cminh on 7/24/17.
 *
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
annotation class Local
