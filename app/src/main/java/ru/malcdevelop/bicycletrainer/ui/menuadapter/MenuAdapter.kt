package ru.malcdevelop.bicycletrainer.ui.menuadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.malcdevelop.bicycletrainer.R

class MenuAdapter(
    items: List<Item> = listOf()
) : RecyclerView.Adapter<MenuAdapter.ItemViewHolder>() {

    private val mutableItems = ArrayList(items)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(parent)

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.setItem(mutableItems[position])
    }

    override fun getItemCount(): Int =
        mutableItems.size

    fun setNewItems(items: List<Item>) {
        mutableItems.clear()
        mutableItems.addAll(items)
        notifyDataSetChanged()
    }

    fun editItem(id: Int, block: (Item) -> Item) {
        val index = mutableItems.indexOfFirst { it.id == id }

        if (index != -1) {
            mutableItems[index] = block(mutableItems[index])
            notifyItemChanged(index, 0)
        }
    }

    /** ItemViewHolder **/
    class ItemViewHolder(
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_menu_item, parent, false)
    ) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)

        fun setItem(item: Item) {
            nameTextView.text = item.nameLine
            contentTextView.isVisible = item.contentLine.isNotEmpty()
            contentTextView.text = item.contentLine
            itemView.setOnClickListener { item.func() }
        }
    }

    /** Item **/
    data class Item(
        val id: Int,
        val nameLine: String,
        val contentLine: String = "",
        val func: () -> Unit
    )
}
