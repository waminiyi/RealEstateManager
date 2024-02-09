package com.waminiyi.realestatemanager.features.agentList.adapters

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

class AgentAdapter(
    private val onAgentClicked: (String) -> Unit
) : ListAdapter<Agent, AgentAdapter.AgentViewHolder>(AgentComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.agent_list_item, parent, false)
        return AgentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AgentViewHolder, position: Int) {
        val agent = getItem(position)
        holder.bind(agent)

        holder.itemView.setOnClickListener {
            onAgentClicked(agent.uuid)
        }
    }

    class AgentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(agent: Agent) {
            val imageView: ImageView = itemView.findViewById(R.id.agentImageView)
            val textView: TextView = itemView.findViewById(R.id.agentNameTextView)
            imageView.load(agent.photoUrl) {
                transformations(CircleCropTransformation())
                placeholder(R.drawable.person)
                error(R.drawable.person_error)
            }
            "${agent.firstName}  ${agent.lastName}".also { textView.text = it }
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