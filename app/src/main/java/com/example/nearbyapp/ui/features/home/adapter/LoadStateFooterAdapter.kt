package com.example.nearbyapp.ui.features.home.adapter

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nearbyapp.databinding.LoadStateFooterBinding
import com.example.nearbyapp.utils.viewBinding

class LoadStateFooterAdapter(
    private val retry: () -> Unit,
) : LoadStateAdapter<LoadStateFooterAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = parent.viewBinding(LoadStateFooterBinding::inflate)
        return LoadStateViewHolder(binding, retry)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class LoadStateViewHolder(
        private val binding: LoadStateFooterBinding,
        private val retry: () -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryBtn.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                retryBtn.isVisible = loadState !is LoadState.Loading
                errorTextMessage.isVisible = loadState !is LoadState.Loading
            }
        }
    }
}
