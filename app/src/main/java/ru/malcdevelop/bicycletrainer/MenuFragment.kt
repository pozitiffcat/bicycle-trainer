package ru.malcdevelop.bicycletrainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.fragment_menu.*

class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
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
                    nameRes = R.string.menu_name_new_ride,
                    func = { onNewRideMenuSelected() }
                ),
                MenuAdapter.Item(
                    nameRes = R.string.menu_name_device_settings,
                    func = { onSettingsMenuSelected() }
                )
            )
        )
    }

    private fun onNewRideMenuSelected() {

    }

    private fun onSettingsMenuSelected() {

    }
}