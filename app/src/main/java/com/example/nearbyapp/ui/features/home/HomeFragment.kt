package com.example.nearbyapp.ui.features.home

import QuotePagingAdapter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nearbyapp.base.BaseFragment
import com.example.nearbyapp.databinding.FragmentHomeBinding
import com.example.nearbyapp.ui.features.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate,
) {

    private val viewModel: HomeViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(requireContext(), "show", Toast.LENGTH_SHORT).show()

        binding.venueRv.apply {
            val venueAdapter = QuotePagingAdapter()
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = venueAdapter

            lifecycleScope.launch {
                viewModel.venues.collectLatest {
                    venueAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                }
            }
        }
    }
}
