package ru.malcdevelop.bicycletrainer.core

import android.content.Context
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc
import com.dsi.ant.plugins.antplus.pccbase.PccReleaseHandle

class HeartDevice(
    private val context: Context,
    private val number: Int,
    val deviceId: DeviceId,
    val name: String
) {
    private var handle: PccReleaseHandle<AntPlusHeartRatePcc>? = null
    private var state: DeviceState = DeviceState.UNRECOGNIZED
    private var heartRate: Int = 0

    val heartBpm: Int
        get() {
            return when (state) {
                DeviceState.TRACKING -> {
                    heartRate
                }
                DeviceState.DEAD -> {
                    handle = null
                    open()
                    0
                }
                else -> {
                    0
                }
            }
        }

    private val resultReceiver =
        AntPluginPcc.IPluginAccessResultReceiver<AntPlusHeartRatePcc> { pcc, result, deviceState ->
            state = deviceState
            if (result == RequestAccessResult.SUCCESS) {
                pcc.subscribeHeartRateDataEvent(dataEventReceiver)
            }
        }

    private val stateChangeReceiver =
        AntPluginPcc.IDeviceStateChangeReceiver { deviceState -> state = deviceState }

    private val dataEventReceiver =
        AntPlusHeartRatePcc.IHeartRateDataReceiver { _, _, computedHeartRate, _, _, _ ->
            heartRate = computedHeartRate
        }

    fun open() {
        handle = AntPlusHeartRatePcc.requestAccess(context, number, 0, resultReceiver, stateChangeReceiver)
    }

    fun close() {
        handle?.close()
        handle = null
    }
}
