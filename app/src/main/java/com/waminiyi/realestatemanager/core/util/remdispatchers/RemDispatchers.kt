package com.waminiyi.realestatemanager.core.util.remdispatchers

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val remDispatchers: RemDispatchers)
enum class RemDispatchers {
    Default,
    IO,
}