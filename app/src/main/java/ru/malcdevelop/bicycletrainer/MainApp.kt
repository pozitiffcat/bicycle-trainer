package ru.malcdevelop.bicycletrainer

import android.app.Application
import ru.malcdevelop.bicycletrainer.core.devices.DeviceRepository
import ru.malcdevelop.bicycletrainer.core.settings.SettingsRepository

class MainApp : Application() {

    val settingsRepository: SettingsRepository by lazy {
        SettingsRepository(this)
    }

    val deviceRepository: DeviceRepository by lazy {
        DeviceRepository(this)
    }

    override fun onCreate() {
        super.onCreate()
    }
}