package ru.malcdevelop.bicycletrainer.ui

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.fragment_menu.*
import ru.malcdevelop.bicycletrainer.R
import ru.malcdevelop.bicycletrainer.ui.menuadapter.MenuAdapter

class MenuFragment : MainFragment() {

    override val layoutRes: Int = R.layout.fragment_menu

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
                    id = R.id.menu_new_ride,
                    nameLine = getString(R.string.menu_name_new_ride),
                    func = { onNewRideMenuSelected() }
                ),
                MenuAdapter.Item(
                    id = R.id.menu_device_settings,
                    nameLine = getString(R.string.menu_name_device_settings),
                    func = { onDeviceSettingsMenuSelected() }
                )
            )
        )
    }

    private fun onNewRideMenuSelected() {

    }

    private fun onDeviceSettingsMenuSelected() {
        mainActivity?.navigateToDeviceSettings(this)
    }
}