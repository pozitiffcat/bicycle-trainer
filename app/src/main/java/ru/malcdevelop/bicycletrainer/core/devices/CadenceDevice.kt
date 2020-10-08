package ru.malcdevelop.bicycletrainer.core.devices

import android.content.Context
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeCadencePcc
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc
import com.dsi.ant.plugins.antplus.pccbase.PccReleaseHandle

class CadenceDevice(
    private val context: Context,
    private val number: Int
) {
    private var handle: PccReleaseHandle<AntPlusBikeCadencePcc>? = null
    private var state: DeviceState = DeviceState.UNRECOGNIZED
    private var mutableCadenceRpm: Int = 0

    val cadenceRpm: Int
        get() {
            return when (state) {
                DeviceState.TRACKING -> {
                    mutableCadenceRpm
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

    private val stateChangeReceiver =
        AntPluginPcc.IDeviceStateChangeReceiver { deviceState -> state = deviceState }

    private val dataEventReceiver =
        AntPlusBikeCadencePcc.ICalculatedCadenceReceiver { estTimestamp, eventFlags, calculatedCadence -> mutableCadenceRpm = calculatedCadence.toInt() }

    private val resultReceiver =
        AntPluginPcc.IPluginAccessResultReceiver<AntPlusBikeCadencePcc> { pcc, result, deviceState ->
            state = deviceState
            if (result == RequestAccessResult.SUCCESS) {
                pcc.subscribeCalculatedCadenceEvent(dataEventReceiver)
            }
        }

    fun open() {
        handle = AntPlusBikeCadencePcc.requestAccess(context, number, 0, false, resultReceiver, stateChangeReceiver)
    }

    fun close() {
        handle?.close()
        handle = null
    }
}