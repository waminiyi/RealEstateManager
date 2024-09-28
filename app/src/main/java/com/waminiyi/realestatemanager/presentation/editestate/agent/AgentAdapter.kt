package com.waminiyi.realestatemanager.presentation.editestate.agent

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waminiyi.realestatemanager.data.models.Agent

class AgentAdapter(
    private val onAgentSelected: (Agent) -> Unit
) : ListAdapter<Agent, AgentAdapter.AgentViewHolder>(AgentComparator()) {

    private var selectedItem: Agent? = null

    fun setCurrentAgent(agent: Agent?) {
        val previousSelectedPosition = currentList.indexOf(this.selectedItem)
        this.selectedItem = agent

        val newSelectedPosition = currentList.indexOf(selectedItem)
        if (newSelectedPosition != -1) {
            notifyItemChanged(newSelectedPosition)
        }
        if (previousSelectedPosition != -1) {
            notifyItemChanged(previousSelectedPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgentViewHolder {
        val view = AgentItemView(parent.context)
        return AgentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AgentViewHolder, position: Int) {
        val agent = currentList[position]
        holder.bind(agent, agent == selectedItem)

        holder.itemView.setOnClickListener {
            if (selectedItem == agent) {
                // Already selected
            } else {
                onAgentSelected.invoke(agent)
            }
        }
    }

    class AgentViewHolder(private val agentView: AgentItemView) : RecyclerView.ViewHolder(agentView) {
        fun bind(agent: Agent, isSelected: Boolean) {
            agentView.setAgent(agent)
            agentView.showAsSelected(isSelected)
        }
    }

    class AgentComparator : DiffUtil.ItemCallback<Agent>() {

        override fun areItemsTheSame(oldItem: Agent, newItem: Agent): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Agent, newItem: Agent): Boolean {
            return oldItem.uuid == newItem.uuid
                    && oldItem.firstName == newItem.firstName
                    && oldItem.lastName == newItem.lastName
                    && oldItem.photoUrl == newItem.photoUrl
        }
    }
}