package ru.malcdevelop.bicycletrainer.core.settings

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.malcdevelop.bicycletrainer.core.data.DeviceId

private const val SHARED_PREFERENCES_NAME = "SHARED_PREFERENCES_NAME"
private const val POWER_DEVICE_DATA_PREFERENCE = "POWER_DEVICE_DATA_PREFERENCE"
private const val SPEED_DEVICE_DATA_PREFERENCE = "SPEED_DEVICE_DATA_PREFERENCE"
private const val CADENCE_DEVICE_DATA_PREFERENCE = "CADENCE_DEVICE_DATA_PREFERENCE"
private const val HEART_DEVICE_DATA_PREFERENCE = "HEART_DEVICE_DATA_PREFERENCE"

class SettingsRepository(
    private val context: Context
) {
    private val sharedPreferences = context.getSharedPreferences(
        SHARED_PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    suspend fun getPowerDevice(): DeviceId? {
        return withContext(Dispatchers.IO) {
            if (sharedPreferences.contains(POWER_DEVICE_DATA_PREFERENCE))
                DeviceId(
                    sharedPreferences.getString(POWER_DEVICE_DATA_PREFERENCE, null) ?: ""
                )
            else
                null
        }
    }

    suspend fun updatePowerDevice(id: DeviceId) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit()
                .putString(POWER_DEVICE_DATA_PREFERENCE, id.data)
                .commit()
        }
    }

    suspend fun getSpeedDevice(): DeviceId? {
        return withContext(Dispatchers.IO) {
            if (sharedPreferences.contains(SPEED_DEVICE_DATA_PREFERENCE))
                DeviceId(
                    sharedPreferences.getString(SPEED_DEVICE_DATA_PREFERENCE, null) ?: ""
                )
            else
                null
        }
    }

    suspend fun updateSpeedDevice(id: DeviceId) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit()
                .putString(SPEED_DEVICE_DATA_PREFERENCE, id.data)
                .commit()
        }
    }

    suspend fun getCadenceDevice(): DeviceId? {
        return withContext(Dispatchers.IO) {
            if (sharedPreferences.contains(CADENCE_DEVICE_DATA_PREFERENCE))
                DeviceId(
                    sharedPreferences.getString(CADENCE_DEVICE_DATA_PREFERENCE, null) ?: ""
                )
            else
                null
        }
    }

    suspend fun updateCadenceDevice(id: DeviceId) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit()
                .putString(CADENCE_DEVICE_DATA_PREFERENCE, id.data)
                .commit()
        }
    }

    suspend fun getHeartDevice(): DeviceId? {
        return withContext(Dispatchers.IO) {
            if (sharedPreferences.contains(HEART_DEVICE_DATA_PREFERENCE))
                DeviceId(
                    sharedPreferences.getString(HEART_DEVICE_DATA_PREFERENCE, null) ?: ""
                )
            else
                null
        }
    }

    suspend fun updateHeartDevice(id: DeviceId) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit()
                .putString(HEART_DEVICE_DATA_PREFERENCE, id.data)
                .commit()
        }
    }
}