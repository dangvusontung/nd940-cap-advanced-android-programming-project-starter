package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ItemElectionBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val clickListener: ElectionListener) :
    ListAdapter<Election, ElectionViewHolder>(ElectionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent, clickListener)
    }

    //TODO: Bind ViewHolder
    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

//TODO: Create ElectionViewHolder
class ElectionViewHolder(
    private val binding: ItemElectionBinding,
    private val clickListener: ElectionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Election) {
        binding.model = item
        binding.electionListener = clickListener
        binding.executePendingBindings()
    }

    //TODO: Add companion object to inflate ViewHolder (from)
    companion object {
        fun from(parent: ViewGroup, clickListener: ElectionListener): ElectionViewHolder {
            val binding: ItemElectionBinding = ItemElectionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ElectionViewHolder(binding, clickListener)
        }
    }
}

//TODO: Create ElectionDiffCallback
class ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {

    override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem == newItem
    }

}

//TODO: Create ElectionListener
class ElectionListener(private val itemClickListener: (Election) -> Unit) {
    fun onClick(item: Election) = itemClickListener(item)
}