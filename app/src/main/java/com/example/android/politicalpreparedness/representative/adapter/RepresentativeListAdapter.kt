package com.example.android.politicalpreparedness.representative.adapter

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.ItemRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Channel
import com.example.android.politicalpreparedness.representative.model.Representative

class RepresentativeListAdapter(private val listener: RepresentativeListener): ListAdapter<Representative, RepresentativeViewHolder>(RepresentativeDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepresentativeViewHolder {
        return RepresentativeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RepresentativeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, listener)
    }
}

class RepresentativeViewHolder private constructor (val binding: ItemRepresentativeBinding): RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): RepresentativeViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<ItemRepresentativeBinding>(inflater, R.layout.item_representative, parent, false)
            return RepresentativeViewHolder(binding)
        }
    }

    fun bind(item: Representative, listener: RepresentativeListener) {
        binding.representative = item
        checkAndShowSocialLinks(item.official.channels)
        checkAndShowWWWLinks(item.official.urls)
        binding.clickListener = listener
        binding.executePendingBindings()
    }

    private fun checkAndShowSocialLinks(channels: List<Channel>?) {
        if (channels.isNullOrEmpty()) {
            binding.facebookButton.visibility = View.INVISIBLE
            binding.twitterButton.visibility = View.INVISIBLE
        } else {
            val facebookUrl = getFacebookUrl(channels)
            if (facebookUrl.isNullOrBlank()) {
                binding.facebookButton.visibility = View.INVISIBLE
            } else {
                enableLink(binding.facebookButton, facebookUrl)
            }

            val twitterUrl = getTwitterUrl(channels)
            if (twitterUrl.isNullOrBlank()) {
                binding.twitterButton.visibility = View.INVISIBLE
            } else {
                if (!twitterUrl.isNullOrBlank()) { enableLink(binding.twitterButton, twitterUrl) }
            }
        }
    }

    private fun checkAndShowWWWLinks(urls: List<String>?) {
        if (urls.isNullOrEmpty()) {
            binding.wwwButton.visibility = View.INVISIBLE
        } else {
            enableLink(binding.wwwButton, urls.first())
        }
    }

    private fun getFacebookUrl(channels: List<Channel>): String? {
        return channels.filter { channel -> channel.type == "Facebook" }
                .map { channel -> "https://www.facebook.com/${channel.id}" }
                .firstOrNull()
    }

    private fun getTwitterUrl(channels: List<Channel>): String? {
        return channels.filter { channel -> channel.type == "Twitter" }
                .map { channel -> "https://www.twitter.com/${channel.id}" }
                .firstOrNull()
    }

    private fun enableLink(view: ImageView, url: String) {
        view.visibility = View.VISIBLE
        view.setOnClickListener { setIntent(url) }
    }

    private fun setIntent(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(ACTION_VIEW, uri)
        itemView.context.startActivity(intent)
    }

}

class RepresentativeDiffCallback: DiffUtil.ItemCallback<Representative>() {
    override fun areItemsTheSame(oldItem: Representative, newItem: Representative): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Representative, newItem: Representative): Boolean {
        return oldItem == newItem
    }
}

class RepresentativeListener(val listener: (String)->Unit) {
    fun onClick(url: String) {
        listener(url)
    }
}