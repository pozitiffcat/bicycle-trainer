package ru.malcdevelop.bicycletrainer.ui.settings

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.malcdevelop.bicycletrainer.R
import ru.malcdevelop.bicycletrainer.core.devices.DeviceRepository
import ru.malcdevelop.bicycletrainer.ui.menuadapter.MenuAdapter
import ru.malcdevelop.bicycletrainer.ui.menuadapter.MenuAdapterFragment

class DeviceAdapterMenuFragment : MenuAdapterFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            while (isActive) {
                menuAdapter.setNewItems(generateItems())
                delay(1000)
            }
        }
    }

    private fun generateItems(): List<MenuAdapter.Item> {
        val callback = (targetFragment as? Callback)

        val foundDevices = callback?.getFoundDevices(targetRequestCode)
            ?: listOf()

        val items = foundDevices.map {
            MenuAdapter.Item(
                id = 0,
                nameLine = it.name,
                contentLine = it.name,
                func = {
                    lifecycleScope.launch {
                        callback?.onSelectedDevice(targetRequestCode, it)
                    }
                }
            )
        }.toMutableList()

        if (mainApp?.deviceRepository?.isSearchActive == true) {
            items += MenuAdapter.Item(
                id = 0,
                nameLine = getString(R.string.device_settings_search_in_progress_line_1),
                contentLine = getString(R.string.device_settings_search_in_progress_line_2),
                func = { }
            )
        } else if (items.isEmpty()) {
            items += MenuAdapter.Item(
                id = 0,
                nameLine = getString(R.string.device_settings_search_no_devices_found),
                func = { }
            )
        }

        return items
    }

    /** Callback **/
    interface Callback {
        fun getFoundDevices(requestCode: Int): List<DeviceRepository.FoundDevice>
        fun onSelectedDevice(requestCode: Int, foundDevice: DeviceRepository.FoundDevice)
    }
}