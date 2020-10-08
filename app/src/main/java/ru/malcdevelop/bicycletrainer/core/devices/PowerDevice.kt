package ru.malcdevelop.bicycletrainer.core.devices

import android.content.Context
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikePowerPcc
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc
import com.dsi.ant.plugins.antplus.pccbase.PccReleaseHandle

class PowerDevice(
    private val context: Context,
    private val number: Int
) {
    private var handle: PccReleaseHandle<AntPlusBikePowerPcc>? = null
    private var state: DeviceState? = null
    private var mutablePowerWatts = 0

    val powerWatts: Int
        get() {
            return when (state) {
                DeviceState.TRACKING -> {
                    mutablePowerWatts
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
        AntPluginPcc.IPluginAccessResultReceiver<AntPlusBikePowerPcc> { pcc, result, deviceState ->
            state = deviceState
            when (result) {
                RequestAccessResult.SUCCESS -> pcc?.subscribeCalculatedPowerEvent(dataEventReceiver)
                else -> open()
            }
        }

    private val dataEventReceiver =
        AntPlusBikePowerPcc.ICalculatedPowerReceiver { estTimestamp, eventFlags, dataSource, calculatedPower ->
            if (dataSource == AntPlusBikePowerPcc.DataSource.POWER_ONLY_DATA)
                mutablePowerWatts = calculatedPower?.toInt() ?: 0
        }

    private val stateChangeReceiver =
        AntPluginPcc.IDeviceStateChangeReceiver { deviceState -> state = deviceState }

    fun open() {
        handle = AntPlusBikePowerPcc.requestAccess(context, number, 0, resultReceiver, stateChangeReceiver)
    }

    fun close() {
        handle?.close()
        handle = null
    }
}