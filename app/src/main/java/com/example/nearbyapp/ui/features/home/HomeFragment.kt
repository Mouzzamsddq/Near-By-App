package com.example.nearbyapp.ui.features.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.nearbyapp.base.BaseFragment
import com.example.nearbyapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate,
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(requireContext(), "show", Toast.LENGTH_SHORT).show()
    }
}
