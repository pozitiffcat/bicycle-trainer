package ru.malcdevelop.bicycletrainer.core.devices

import android.content.Context
import com.dsi.ant.plugins.antplus.pcc.MultiDeviceSearch
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult
import ru.malcdevelop.bicycletrainer.core.data.DeviceId
import com.dsi.ant.plugins.antplus.pccbase.MultiDeviceSearch as MultiDeviceSearchBase
import java.util.*

class DeviceRepository(
    private val context: Context
) : MultiDeviceSearch.SearchCallbacks {

    var isSearchActive = false
        private set

    private var deviceSearch: MultiDeviceSearch? = null

    private val mutableFoundPowerDevices = mutableListOf<FoundDevice>()
    private val mutableFoundSpeedDevices = mutableListOf<FoundDevice>()
    private val mutableFoundCadenceDevices = mutableListOf<FoundDevice>()
    private val mutableFoundHeartDevices = mutableListOf<FoundDevice>()

    private val openedPowerDevices = mutableMapOf<DeviceId, PowerDevice>()
    private val openedSpeedDevices = mutableMapOf<DeviceId, SpeedDevice>()
    private val openedCadenceDevices = mutableMapOf<DeviceId, CadenceDevice>()
    private val openedHeartDevices = mutableMapOf<DeviceId, HeartDevice>()

    val foundPowerDevices: List<FoundDevice>
        get() = mutableFoundPowerDevices

    val foundSpeedDevices: List<FoundDevice>
        get() = mutableFoundSpeedDevices

    val foundCadenceDevices: List<FoundDevice>
        get() = mutableFoundCadenceDevices

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

    fun getPowerDeviceByDeviceId(deviceId: DeviceId): PowerDevice? {
        val openedDevice = openedPowerDevices[deviceId]

        if (openedDevice != null)
            return openedDevice

        val foundDevice = mutableFoundPowerDevices.find { it.deviceId == deviceId }
            ?: return null

        val number = getNumberFromDeviceId(foundDevice.deviceId)
            ?: return null

        val device = PowerDevice(
            context = context,
            number = number
        )

        device.open()
        openedPowerDevices[deviceId] = device

        return device
    }

    fun getSpeedDeviceByDeviceId(deviceId: DeviceId): SpeedDevice? {
        val openedDevice = openedSpeedDevices[deviceId]

        if (openedDevice != null)
            return openedDevice

        val foundDevice = mutableFoundSpeedDevices.find { it.deviceId == deviceId }
            ?: return null

        val number = getNumberFromDeviceId(foundDevice.deviceId)
            ?: return null

        val device = SpeedDevice(
            context = context,
            number = number
        )

        device.open()
        openedSpeedDevices[deviceId] = device

        return device
    }

    fun getCadenceDeviceByDeviceId(deviceId: DeviceId): CadenceDevice? {
        val openedDevice = openedCadenceDevices[deviceId]

        if (openedDevice != null)
            return openedDevice

        val foundDevice = mutableFoundCadenceDevices.find { it.deviceId == deviceId }
            ?: return null

        val number = getNumberFromDeviceId(foundDevice.deviceId)
            ?: return null

        val device = CadenceDevice(
            context = context,
            number = number
        )

        device.open()
        openedCadenceDevices[deviceId] = device

        return device
    }

    fun getHeartDeviceByDeviceId(deviceId: DeviceId): HeartDevice? {
        val openedDevice = openedHeartDevices[deviceId]

        if (openedDevice != null)
            return openedDevice

        val foundDevice = mutableFoundHeartDevices.find { it.deviceId == deviceId }
            ?: return null

        val number = getNumberFromDeviceId(foundDevice.deviceId)
            ?: return null

        val device = HeartDevice(
            context = context,
            number = number
        )

        device.open()
        openedHeartDevices[deviceId] = device

        return device
    }

    override fun onSearchStarted(result: MultiDeviceSearch.RssiSupport?) {
    }

    override fun onDeviceFound(result: MultiDeviceSearchBase.MultiDeviceSearchResult) {
        val foundDevice = FoundDevice(
            deviceId = buildDeviceId(result.antDeviceNumber),
            name = result.deviceDisplayName
        )

        when (result.antDeviceType) {
            DeviceType.BIKE_POWER -> {
                if (!mutableFoundPowerDevices.any { it.deviceId == foundDevice.deviceId })
                    mutableFoundPowerDevices += foundDevice
            }
            DeviceType.BIKE_SPD -> {
                if (!mutableFoundSpeedDevices.any { it.deviceId == foundDevice.deviceId })
                    mutableFoundSpeedDevices += foundDevice
            }
            DeviceType.BIKE_CADENCE -> {
                if (!mutableFoundCadenceDevices.any { it.deviceId == foundDevice.deviceId })
                    mutableFoundCadenceDevices += foundDevice
            }
            DeviceType.HEARTRATE -> {
                if (!mutableFoundHeartDevices.any { it.deviceId == foundDevice.deviceId })
                    mutableFoundHeartDevices += foundDevice
            }
            else -> {
                /** not supported **/
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