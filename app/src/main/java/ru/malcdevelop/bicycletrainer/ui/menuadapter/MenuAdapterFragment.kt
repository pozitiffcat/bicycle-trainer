package ru.malcdevelop.bicycletrainer.ui.menuadapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.malcdevelop.bicycletrainer.R
import ru.malcdevelop.bicycletrainer.ui.BaseFragment

open class MenuAdapterFragment : BaseFragment() {

    val menuAdapter = MenuAdapter()

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return RecyclerView(inflater.context).apply {
            addItemDecoration(
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

            layoutManager = LinearLayoutManager(inflater.context)
            adapter = menuAdapter
        }
    }
}