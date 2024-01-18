package com.waminiyi.realestatemanager.features.editestate.agent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.core.model.data.Agent
import com.waminiyi.realestatemanager.features.setAgentSelection

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
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.agent_item, parent, false)
        return AgentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AgentViewHolder, position: Int) {
        val agent = agents[position]
        holder.bind(agent, agent == selectedItem)

        holder.itemView.setOnClickListener {

            if (selectedItem == agent) {
                // Already selected
            } else {
                setCurrentAgent(agent)
                onAgentSelected.invoke(agent)
            }
        }
    }

    override fun getItemCount(): Int = agents.size

    class AgentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(agent: Agent, isSelected: Boolean) {
            val imageView: ImageView = itemView.findViewById(R.id.itemAgentImageView)
            val textView: TextView = itemView.findViewById(R.id.itemAgentNameTextView)
            imageView.load(agent.photoUrl) {
                transformations(CircleCropTransformation())
                placeholder(R.drawable.person)
                error(R.drawable.person_error)
            }
            "${agent.firstName}  ${agent.lastName[0]}.".also { textView.text = it }
            imageView.setAgentSelection(isSelected)
            textView.setAgentSelection(isSelected)

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