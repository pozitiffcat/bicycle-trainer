package ru.malcdevelop.bicycletrainer.core

import android.content.Context
import com.dsi.ant.plugins.antplus.pcc.MultiDeviceSearch
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult
import com.dsi.ant.plugins.antplus.pccbase.MultiDeviceSearch as MultiDeviceSearchBase
import java.util.*

class DeviceRepository(
    private val context: Context
) : MultiDeviceSearch.SearchCallbacks {

    var isSearchActive = false
        private set

    private var deviceSearch: MultiDeviceSearch? = null

    private val mutableFoundHeartDevices = mutableListOf<FoundDevice>()

    private val openedHeartDevices = mutableMapOf<DeviceId, HeartDevice>()

    val foundHeartDevices: List<FoundDevice>
        get() = mutableFoundHeartDevices

    fun startSearch() {
        if (isSearchActive)
            return

        val deviceTypes = EnumSet.of(
            DeviceType.BIKE_POWER,
            DeviceType.BIKE_SPD,
            DeviceType.HEARTRATE,
            DeviceType.BIKE_CADENCE
        )

        isSearchActive = true
        deviceSearch = MultiDeviceSearch(context, deviceTypes, this)
    }

    fun stopSearch() {
        if (!isSearchActive)
            return

        deviceSearch?.close()
        deviceSearch = null
    }

    fun getHeartDeviceByDeviceId(deviceId: DeviceId): HeartDevice? {
        if (openedHeartDevices.contains(deviceId))
            return openedHeartDevices[deviceId]

        val foundDevice = mutableFoundHeartDevices.find { it.deviceId == deviceId }
            ?: return null

        val number = getNumberFromDeviceId(foundDevice.deviceId)
            ?: return null

        val heartDevice = HeartDevice(
            context = context,
            number = number,
            deviceId = foundDevice.deviceId,
            name = foundDevice.name
        )

        heartDevice.open()
        openedHeartDevices[deviceId] = heartDevice

        return heartDevice
    }

    override fun onSearchStarted(result: MultiDeviceSearch.RssiSupport?) {
    }

    override fun onDeviceFound(result: MultiDeviceSearchBase.MultiDeviceSearchResult) {
        if (result.antDeviceType == DeviceType.HEARTRATE) {
            val deviceId = buildDeviceId(result.antDeviceNumber)
            if (!mutableFoundHeartDevices.any { it.deviceId == deviceId }) {
                mutableFoundHeartDevices += FoundDevice(
                    deviceId = deviceId,
                    name = result.deviceDisplayName
                )
            }
        }
    }

    private fun buildDeviceId(number: Int): DeviceId {
        return DeviceId(number.toString())
    }

    private fun getNumberFromDeviceId(deviceId: DeviceId): Int? {
        return deviceId.data.toIntOrNull()
    }

    override fun onSearchStopped(result: RequestAccessResult) {
        isSearchActive = false
    }

    /** FoundDevice **/
    data class FoundDevice(
        val deviceId: DeviceId,
        val name: String
    )
}