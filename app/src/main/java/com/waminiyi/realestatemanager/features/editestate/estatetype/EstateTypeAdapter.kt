package com.waminiyi.realestatemanager.features.editestate.estatetype

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waminiyi.realestatemanager.core.model.data.EstateType

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
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.estate_type_item_view, parent, false)
//        return EstateTypeViewHolder(view)
        val view = EstateTypeItemView(parent.context)
        return EstateTypeAdapter.EstateTypeViewHolder(view)
    }

    override fun onBindViewHolder(holder: EstateTypeViewHolder, position: Int) {
        val estateType = estateTypes[position]
        holder.bind(estateType, estateType == selectedItem)

        holder.itemView.setOnClickListener {

            if (selectedItem == estateType) {
                // Already selected, do nothing
            } else {
                //setCurrentType(estateType)
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
//            val imageView: ImageView = itemView.findViewById(R.id.itemEstateTypeImageView)
//            val textView: TextView = itemView.findViewById(R.id.itemEstateTypeNameTextView)
//            textView.text = estateType.asUiEstateType(itemView.context).name
//            imageView.setImageResource(estateType.asUiEstateType(itemView.context).iconResId)
//            textView.showAsSelected(isSelected)
//            imageView.showAsSelected(isSelected)
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