package com.example.bookshelfapp.ui.features.account

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookshelfapp.R
import com.example.bookshelfapp.base.BaseFragment
import com.example.bookshelfapp.databinding.ConfirmBottomSheetBinding
import com.example.bookshelfapp.databinding.FragmentMyAccountBinding
import com.example.bookshelfapp.ui.features.account.viewmodel.MyAccountViewModel
import com.example.bookshelfapp.utils.findNavControllerSafely
import com.example.bookshelfapp.utils.navigateSafe
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyAccountFragment : BaseFragment<FragmentMyAccountBinding>(
    FragmentMyAccountBinding::inflate,
) {
    private val viewModel: MyAccountViewModel by viewModels()
    private var logoutBottomSheet: BottomSheetDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeBottomSheet()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.logoutCl.setOnClickListener {
            showLogoutBottomSheet()
        }
    }

    private fun showLogoutBottomSheet() {
        if (logoutBottomSheet?.isShowing == true) {
            dismissBottomSheet()
        } else {
            logoutBottomSheet?.show()
        }
    }

    private fun dismissBottomSheet() {
        logoutBottomSheet?.dismiss()
    }

    private fun initializeBottomSheet() {
        logoutBottomSheet = BottomSheetDialog(requireContext())
        val binding = ConfirmBottomSheetBinding.inflate(layoutInflater)
        binding.noTv.setOnClickListener {
            dismissBottomSheet()
        }
        binding.yesTv.setOnClickListener {
            dismissBottomSheet()
            viewModel.logout()
            findNavControllerSafely()?.navigateSafe(
                action = R.id.action_my_account_fragment_to_SignInFragment,
            )
        }
        logoutBottomSheet?.setContentView(binding.root)
    }
}
