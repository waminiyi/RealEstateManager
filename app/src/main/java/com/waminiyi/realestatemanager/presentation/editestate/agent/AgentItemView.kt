package com.waminiyi.realestatemanager.presentation.editestate.agent

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import coil.load
import coil.transform.CircleCropTransformation
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.data.models.Agent
import com.waminiyi.realestatemanager.presentation.extensions.showAsSelected

class AgentItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val imageView: ImageView
    private val textView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.agent_item_view, this, true)
        orientation = VERTICAL
        imageView = findViewById(R.id.itemAgentImageView)
        textView = findViewById(R.id.itemAgentNameTextView)
        imageView.setBackgroundResource(R.drawable.circular_border_with_transparent_background)
    }

    fun setAgent(agent: Agent) {
        val fullName = "${agent.firstName}  ${agent.lastName[0]}."
        textView.text = fullName
        imageView.load(agent.photoUrl) {
            transformations(CircleCropTransformation())
            placeholder(R.drawable.person)
            error(R.drawable.person_error)
        }
    }

    fun showAsSelected(isSelected: Boolean) {
        textView.showAsSelected(isSelected, R.color.cinnabar, R.color.dim_gray)
        imageView.showAsSelected(isSelected, R.color.cinnabar, R.color.dim_gray)
    }
}