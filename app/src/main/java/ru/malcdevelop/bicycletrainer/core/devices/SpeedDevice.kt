package ru.malcdevelop.bicycletrainer.core.devices

import android.content.Context
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeSpeedDistancePcc
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc
import com.dsi.ant.plugins.antplus.pccbase.PccReleaseHandle

class SpeedDevice(
    private val context: Context,
    private val number: Int
) {
    private var handle: PccReleaseHandle<AntPlusBikeSpeedDistancePcc>? = null
    private var state: DeviceState = DeviceState.UNRECOGNIZED
    private var mutableSpeedMps: Double = 0.0

    var wheelMeters: Double = 2.105 /** 700Cx25 default **/

    val speedMps: Double
        get() {
            return when (state) {
                DeviceState.TRACKING -> {
                    mutableSpeedMps
                }
                DeviceState.DEAD -> {
                    handle = null
                    open()
                    0.0
                }
                else -> {
                    0.0
                }
            }
        }

    private val resultReceiver =
        AntPluginPcc.IPluginAccessResultReceiver<AntPlusBikeSpeedDistancePcc> { pcc, result, deviceState ->
            state = deviceState
            if (result == RequestAccessResult.SUCCESS) {
                pcc.subscribeRawSpeedAndDistanceDataEvent(rawDataEventReceiver)
            }
        }

    private val stateChangeReceiver =
        AntPluginPcc.IDeviceStateChangeReceiver { deviceState -> state = deviceState }

    private var lastTimeMillis = 0L
    private var ticks: Long = -1L

    private val rawDataEventReceiver =
        AntPlusBikeSpeedDistancePcc.IRawSpeedAndDistanceDataReceiver { _, _, t, n ->
            val currentTimeMillis = System.currentTimeMillis()

            if (lastTimeMillis == 0L)
                lastTimeMillis = System.currentTimeMillis()

            if (ticks < 0)
                ticks = n

            val deltaTime = currentTimeMillis - lastTimeMillis

            if (deltaTime >= 3000) {
                val seconds = deltaTime * 0.001
                val factor = 1.0 / seconds

                mutableSpeedMps = (n - ticks) * wheelMeters * factor

                lastTimeMillis = currentTimeMillis
                ticks = n
            }
        }

    fun open() {
        handle = AntPlusBikeSpeedDistancePcc.requestAccess(context, number, 0, false, resultReceiver, stateChangeReceiver)
    }

    fun close() {
        handle?.close()
        handle = null
    }
}