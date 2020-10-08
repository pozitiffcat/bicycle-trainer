package ru.malcdevelop.bicycletrainer.core.devices

import android.content.Context
import android.util.Log
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc
import com.dsi.ant.plugins.antplus.pccbase.PccReleaseHandle

class HeartDevice(
    private val context: Context,
    private val number: Int
) {
    private var handle: PccReleaseHandle<AntPlusHeartRatePcc>? = null
    private var state: DeviceState = DeviceState.UNRECOGNIZED
    private var mutableHeartBpm: Int = 0

    val heartBpm: Int
        get() {
            return when (state) {
                DeviceState.TRACKING -> {
                    mutableHeartBpm
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
            Log.d("123456", "heart $mutableHeartBpm")
            mutableHeartBpm = computedHeartRate
        }

    fun open() {
        handle = AntPlusHeartRatePcc.requestAccess(context, number, 0, resultReceiver, stateChangeReceiver)
    }

    fun close() {
        handle?.close()
        handle = null
    }
}
