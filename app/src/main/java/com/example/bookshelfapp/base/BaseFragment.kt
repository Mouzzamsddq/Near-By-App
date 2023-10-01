package com.example.bookshelfapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.bookshelfapp.ui.MainActivity

abstract class BaseFragment<T : ViewBinding>(
    val inflate: (LayoutInflater, ViewGroup?, Boolean) -> T,
) : Fragment() {
    private var _binding: T? = null
    private var mainActivity: MainActivity? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = inflate(inflater, container, false)
        mainActivity = activity as? MainActivity
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun isBindingNull() = _binding == null
}
