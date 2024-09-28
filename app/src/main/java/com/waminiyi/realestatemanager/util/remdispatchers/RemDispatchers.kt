package com.waminiyi.realestatemanager.util.remdispatchers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Dispatcher(val remDispatchers: RemDispatchers)
enum class RemDispatchers {
    Main,
    Default,
    IO,
}