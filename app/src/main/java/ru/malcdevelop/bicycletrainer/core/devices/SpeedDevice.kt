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

    private var lastTimeMillis = 0L
    private var lastTicks: Long = -1L
    private var attempts = 0

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

    private val rawDataEventReceiver =
        AntPlusBikeSpeedDistancePcc.IRawSpeedAndDistanceDataReceiver { ts, e, t, n ->
            val currentTimeMillis = (t.toDouble() * 1000.0).toLong()

            if (lastTimeMillis == 0L)
                lastTimeMillis = currentTimeMillis

            if (lastTicks < 0)
                lastTicks = n

            val deltaTime = currentTimeMillis - lastTimeMillis

            val deltaSeconds = deltaTime * 0.001

            if (deltaSeconds > 0) {
                attempts = 0
                mutableSpeedMps = (n - lastTicks) * wheelMeters / deltaSeconds
            } else {
                attempts++
            }

            if (attempts > 5) {
                mutableSpeedMps = 0.0
            }

            lastTimeMillis = currentTimeMillis
            lastTicks = n
        }

    fun open() {
        handle = AntPlusBikeSpeedDistancePcc.requestAccess(context, number, 0, false, resultReceiver, stateChangeReceiver)
    }

    fun close() {
        handle?.close()
        handle = null
    }
}