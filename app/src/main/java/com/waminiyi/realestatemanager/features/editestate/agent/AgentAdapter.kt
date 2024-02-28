package com.waminiyi.realestatemanager.features.editestate.agent

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waminiyi.realestatemanager.core.model.data.Agent

class AgentAdapter(
    private val agents: List<Agent>,
    private val onAgentSelected: (Agent) -> Unit
) : ListAdapter<Agent, AgentAdapter.AgentViewHolder>(AgentComparator()) {

    private var selectedItem: Agent? = null

    fun setCurrentAgent(agent: Agent?) {
        val previousSelectedPosition = agents.indexOf(this.selectedItem)
        this.selectedItem = agent

        val newSelectedPosition = agents.indexOf(selectedItem)
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
        val agent = agents[position]
        holder.bind(agent, agent == selectedItem)

        holder.itemView.setOnClickListener {
            if (selectedItem == agent) {
                // Already selected
            } else {
                onAgentSelected.invoke(agent)
            }
        }
    }

    override fun getItemCount(): Int = agents.size

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