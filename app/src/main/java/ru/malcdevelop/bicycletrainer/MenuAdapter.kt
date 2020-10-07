package ru.malcdevelop.bicycletrainer

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView

class MenuAdapter(
    private val items: List<Item>
) : RecyclerView.Adapter<MenuAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(parent)

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.setItem(items[position])
    }

    override fun getItemCount(): Int =
        items.size

    /** ItemViewHolder **/
    class ItemViewHolder(
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_menu_item, parent, false)
    ) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)

        fun setItem(item: Item) {
            nameTextView.text = nameTextView.resources.getString(item.nameRes)
            itemView.setOnClickListener { item.func() }
        }
    }

    /** Item **/
    data class Item(
        @StringRes val nameRes: Int,
        val func: () -> Unit
    )
}
