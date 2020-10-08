package ru.malcdevelop.bicycletrainer.ui.settings

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.fragment_device_settings.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.malcdevelop.bicycletrainer.R
import ru.malcdevelop.bicycletrainer.core.devices.DeviceRepository
import ru.malcdevelop.bicycletrainer.ui.MainFragment
import ru.malcdevelop.bicycletrainer.ui.menuadapter.MenuAdapter

private const val POWER_DEVICE_REQUEST_CODE = 1
private const val SPEED_DEVICE_REQUEST_CODE = 2
private const val CADENCE_DEVICE_REQUEST_CODE = 3
private const val HEART_DEVICE_REQUEST_CODE = 4

class DeviceSettingsFragment : MainFragment(), DeviceAdapterMenuFragment.Callback {

    override val layoutRes: Int = R.layout.fragment_device_settings

    override val title: String?
        get() = getString(R.string.device_settings_title)

    private val menuAdapter: MenuAdapter?
        get() = menuRecyclerView?.adapter as? MenuAdapter

    override fun onResume() {
        super.onResume()
        mainApp?.deviceRepository?.startSearch()
    }

    override fun onPause() {
        super.onPause()
        mainApp?.deviceRepository?.stopSearch()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuRecyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            ).apply {
                setDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.default_vertical_delimiter
                    )!!
                )
            }
        )

        menuRecyclerView.adapter = MenuAdapter(
            listOf(
                MenuAdapter.Item(
                    id = R.id.device_settings_menu_power,
                    nameLine = getString(R.string.device_settings_menu_name_select_power_device),
                    contentLine = getString(R.string.device_settings_not_set),
                    func = { onSelectPowerDeviceMenuSelected() }
                ),
                MenuAdapter.Item(
                    id = R.id.device_settings_menu_speed,
                    nameLine = getString(R.string.device_settings_menu_name_select_speed_device),
                    contentLine = getString(R.string.device_settings_not_set),
                    func = { onSelectSpeedDeviceMenuSelected() }
                ),
                MenuAdapter.Item(
                    id = R.id.device_settings_menu_cadence,
                    nameLine = getString(R.string.device_settings_menu_name_select_cadence_device),
                    contentLine = getString(R.string.device_settings_not_set),
                    func = { onSelectCadenceDeviceMenuSelected() }
                ),
                MenuAdapter.Item(
                    id = R.id.device_settings_menu_heart,
                    nameLine = getString(R.string.device_settings_menu_name_select_heart_device),
                    contentLine = getString(R.string.device_settings_not_set),
                    func = { onSelectHeartDeviceMenuSelected() }
                )
            )
        )

        updatePowerDeviceItem()
        updateSpeedDeviceItem()
        updateCadenceDeviceItem()
        updateHeartDeviceItem()
    }

    private fun updatePowerDeviceItem() {
        viewLifecycleOwner.lifecycleScope.launch {
            while (isActive) {
                val device = mainApp?.settingsRepository?.getPowerDevice()?.let { deviceId ->
                    mainApp?.deviceRepository?.getPowerDeviceByDeviceId(deviceId)
                }

                val contentLine = device?.powerWatts?.let { "$it W" }
                    ?: getString(R.string.device_settings_not_set)

                menuAdapter?.editItem(R.id.device_settings_menu_power) { item ->
                    MenuAdapter.Item(
                        id = item.id,
                        nameLine = item.nameLine,
                        contentLine = contentLine,
                        func = { onSelectPowerDeviceMenuSelected() }
                    )
                }

                delay(1000)
            }
        }
    }

    private fun updateSpeedDeviceItem() {
        viewLifecycleOwner.lifecycleScope.launch {
            while (isActive) {
                val device = mainApp?.settingsRepository?.getSpeedDevice()?.let { deviceId ->
                    mainApp?.deviceRepository?.getSpeedDeviceByDeviceId(deviceId)
                }

                // TODO: set from settings
//                device?.wheelMeters

                val contentLine = device?.speedMps?.let { "${it * 3.6} Km/h" }
                    ?: getString(R.string.device_settings_not_set)

                menuAdapter?.editItem(R.id.device_settings_menu_speed) { item ->
                    MenuAdapter.Item(
                        id = item.id,
                        nameLine = item.nameLine,
                        contentLine = contentLine,
                        func = { onSelectSpeedDeviceMenuSelected() }
                    )
                }

                delay(1000)
            }
        }
    }

    private fun updateCadenceDeviceItem() {
        viewLifecycleOwner.lifecycleScope.launch {
            while (isActive) {
                val device = mainApp?.settingsRepository?.getCadenceDevice()?.let { deviceId ->
                    mainApp?.deviceRepository?.getCadenceDeviceByDeviceId(deviceId)
                }

                val contentLine = device?.cadenceRpm?.let { "$it RPM" }
                    ?: getString(R.string.device_settings_not_set)

                menuAdapter?.editItem(R.id.device_settings_menu_cadence) { item ->
                    MenuAdapter.Item(
                        id = item.id,
                        nameLine = item.nameLine,
                        contentLine = contentLine,
                        func = { onSelectCadenceDeviceMenuSelected() }
                    )
                }

                delay(1000)
            }
        }
    }

    private fun updateHeartDeviceItem() {
        viewLifecycleOwner.lifecycleScope.launch {
            while (isActive) {
                val device = mainApp?.settingsRepository?.getHeartDevice()?.let { deviceId ->
                    mainApp?.deviceRepository?.getHeartDeviceByDeviceId(deviceId)
                }

                val contentLine = device?.heartBpm?.let { "$it BPM" }
                    ?: getString(R.string.device_settings_not_set)

                menuAdapter?.editItem(R.id.device_settings_menu_heart) { item ->
                    MenuAdapter.Item(
                        id = item.id,
                        nameLine = item.nameLine,
                        contentLine = contentLine,
                        func = { onSelectHeartDeviceMenuSelected() }
                    )
                }

                delay(1000)
            }
        }
    }

    private fun onSelectPowerDeviceMenuSelected() {
        showDeviceAdapterMenuFragment(POWER_DEVICE_REQUEST_CODE)
    }

    private fun onSelectSpeedDeviceMenuSelected() {
        showDeviceAdapterMenuFragment(SPEED_DEVICE_REQUEST_CODE)
    }

    private fun onSelectCadenceDeviceMenuSelected() {
        showDeviceAdapterMenuFragment(CADENCE_DEVICE_REQUEST_CODE)
    }

    private fun onSelectHeartDeviceMenuSelected() {
        showDeviceAdapterMenuFragment(HEART_DEVICE_REQUEST_CODE)
    }

    private fun showDeviceAdapterMenuFragment(requestCode: Int) {
        val deviceAdapterMenuFragment = DeviceAdapterMenuFragment()
        deviceAdapterMenuFragment.setTargetFragment(this, requestCode)
        mainActivity?.showDialogFragment(deviceAdapterMenuFragment)
    }

    override fun getFoundDevices(requestCode: Int): List<DeviceRepository.FoundDevice> {
        return when (requestCode) {
            POWER_DEVICE_REQUEST_CODE -> {
                mainApp?.deviceRepository?.foundPowerDevices ?: listOf()
            }
            SPEED_DEVICE_REQUEST_CODE -> {
                mainApp?.deviceRepository?.foundSpeedDevices ?: listOf()
            }
            CADENCE_DEVICE_REQUEST_CODE -> {
                mainApp?.deviceRepository?.foundCadenceDevices ?: listOf()
            }
            HEART_DEVICE_REQUEST_CODE -> {
                mainApp?.deviceRepository?.foundHeartDevices ?: listOf()
            }
            else -> {
                listOf()
            }
        }
    }

    override fun onSelectedDevice(requestCode: Int, foundDevice: DeviceRepository.FoundDevice) {
        viewLifecycleOwner.lifecycleScope.launch {
            when (requestCode) {
                POWER_DEVICE_REQUEST_CODE -> {
                    mainApp?.settingsRepository?.updatePowerDevice(foundDevice.deviceId)
                }
                SPEED_DEVICE_REQUEST_CODE -> {
                    mainApp?.settingsRepository?.updateSpeedDevice(foundDevice.deviceId)
                }
                CADENCE_DEVICE_REQUEST_CODE -> {
                    mainApp?.settingsRepository?.updateCadenceDevice(foundDevice.deviceId)
                }
                HEART_DEVICE_REQUEST_CODE -> {
                    mainApp?.settingsRepository?.updateHeartDevice(foundDevice.deviceId)
                }
                else -> {
                    /** unknown code **/
                }
            }
        }
        mainActivity?.hideDialog()
    }
}
