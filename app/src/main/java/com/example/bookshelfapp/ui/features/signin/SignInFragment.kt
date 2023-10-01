package com.example.bookshelfapp.ui.features.signin

import android.os.Bundle
import android.view.View
import com.example.bookshelfapp.R
import com.example.bookshelfapp.base.BaseFragment
import com.example.bookshelfapp.databinding.FragmentSignInBinding
import com.example.bookshelfapp.utils.findNavControllerSafely
import com.example.bookshelfapp.utils.navigateSafe

class SignInFragment : BaseFragment<FragmentSignInBinding>(
    FragmentSignInBinding::inflate,
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            signUpBtn.setOnClickListener {
                findNavControllerSafely()?.navigateSafe(
                    R.id.action_sign_in_fragment_to_signupFragment,
                )
            }
        }
    }
}
