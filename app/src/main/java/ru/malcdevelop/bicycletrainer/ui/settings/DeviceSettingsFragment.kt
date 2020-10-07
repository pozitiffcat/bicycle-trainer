package ru.malcdevelop.bicycletrainer.ui.settings

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.fragment_menu.menuRecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.malcdevelop.bicycletrainer.R
import ru.malcdevelop.bicycletrainer.ui.MainFragment
import ru.malcdevelop.bicycletrainer.ui.menuadapter.MenuAdapter

class DeviceSettingsFragment : MainFragment() {

    override val layoutRes: Int = R.layout.fragment_device_settings

    private val menuAdapter: MenuAdapter?
        get() = menuRecyclerView.adapter as? MenuAdapter

    override fun onResume() {
        super.onResume()
        mainActivity?.deviceRepository?.startSearch()
    }

    override fun onPause() {
        super.onPause()
        mainActivity?.deviceRepository?.stopSearch()
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

        updateHeartDeviceItem()
    }

    private fun updateHeartDeviceItem() {
        lifecycleScope.launch {
            while (isActive) {
                val heartDevice = mainActivity?.settingsRepository?.getHeartDevice()?.let { deviceId ->
                    mainActivity?.deviceRepository?.getHeartDeviceByDeviceId(deviceId)
                }

                val contentLine = heartDevice?.heartBpm?.let { "$it BPM" }
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

    }

    private fun onSelectSpeedDeviceMenuSelected() {

    }

    private fun onSelectCadenceDeviceMenuSelected() {

    }

    private fun onSelectHeartDeviceMenuSelected() {
        mainActivity?.showDialogFragment(HeartDeviceMenuFragment())
    }
}
