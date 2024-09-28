package com.waminiyi.realestatemanager.presentation.editestate.estatetype

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waminiyi.realestatemanager.data.models.EstateType

class EstateTypeAdapter(
    private val estateTypes: List<EstateType>,
    private val onTypeSelected: (EstateType) -> Unit
) : ListAdapter<EstateType, EstateTypeAdapter.EstateTypeViewHolder>(EstateTypeComparator()) {

    private var selectedItem: EstateType? = null

    fun setCurrentType(estateType: EstateType?) {
        val previousSelectedPosition = estateTypes.indexOf(this.selectedItem)
        this.selectedItem = estateType

        val newSelectedPosition = estateTypes.indexOf(selectedItem)
        if (newSelectedPosition != -1) {
            notifyItemChanged(newSelectedPosition)
        }
        if (previousSelectedPosition != -1) {
            notifyItemChanged(previousSelectedPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstateTypeViewHolder {
        val view = EstateTypeItemView(parent.context)
        return EstateTypeViewHolder(view)
    }

    override fun onBindViewHolder(holder: EstateTypeViewHolder, position: Int) {
        val estateType = estateTypes[position]
        holder.bind(estateType, estateType == selectedItem)

        holder.itemView.setOnClickListener {

            if (selectedItem == estateType) {
                // Already selected, do nothing
            } else {
                onTypeSelected.invoke(estateType)
            }
        }
    }

    override fun getItemCount(): Int = estateTypes.size

    class EstateTypeViewHolder(
        private val estateTypeItemView: EstateTypeItemView
    ) : RecyclerView.ViewHolder(estateTypeItemView) {
        fun bind(estateType: EstateType, isSelected: Boolean) {
            estateTypeItemView.setEstateType(estateType)
            estateTypeItemView.showAsSelected(isSelected)
        }
    }

    class EstateTypeComparator : DiffUtil.ItemCallback<EstateType>() {

        override fun areItemsTheSame(oldItem: EstateType, newItem: EstateType): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: EstateType, newItem: EstateType): Boolean {
            return oldItem.name == newItem.name
        }
    }
}