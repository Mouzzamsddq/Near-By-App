package com.example.bookshelfapp.ui.features.signin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookshelfapp.R
import com.example.bookshelfapp.base.BaseFragment
import com.example.bookshelfapp.databinding.FragmentSignInBinding
import com.example.bookshelfapp.ui.features.signin.viewmodel.SignInViewModel
import com.example.bookshelfapp.utils.findNavControllerSafely
import com.example.bookshelfapp.utils.hideKeyboard
import com.example.bookshelfapp.utils.navigateSafe
import com.example.bookshelfapp.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>(
    FragmentSignInBinding::inflate,
) {

    private val viewModel: SignInViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        setObservers()
        addOnTextChangeListener()
    }

    private fun addOnTextChangeListener() {
        binding.apply {
            nameEt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

                override fun afterTextChanged(name: Editable?) {
                    binding.apply {
                        context?.let {
                            viewModel.checkValidation(
                                name = name.toString(),
                                password = passwordEt.text.toString(),
                                context = it,
                            )
                        }
                    }
                }
            })
            passwordEt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

                override fun afterTextChanged(password: Editable?) {
                    binding.apply {
                        context?.let {
                            viewModel.checkValidation(
                                name = nameEt.text.toString(),
                                password = password.toString(),
                                context = it,
                            )
                        }
                    }
                }
            })
        }
    }

    private fun setObservers() {
        viewModel.signInStatus.observe(viewLifecycleOwner) {
            when (it) {
                is SignInViewModel.SignInStatus.Success -> {
                    showHideLoaderView(show = false)
                    context.showToast(message = getString(R.string.login_successful))
                    findNavControllerSafely()?.navigateSafe(
                        action = R.id.action_sign_in_fragment_to_homeFragment,
                    )
                }

                is SignInViewModel.SignInStatus.Error -> {
                    showHideLoaderView(show = false)
                    context.showToast(message = it.message)
                }

                is SignInViewModel.SignInStatus.Loading -> {
                    showHideLoaderView(show = true)
                }
            }
        }
    }

    private fun setOnClickListeners() {
        binding.apply {
            signUpBtn.setOnClickListener {
                findNavControllerSafely()?.navigateSafe(
                    R.id.action_sign_in_fragment_to_signupFragment,
                )
            }
            loginBtn.setOnClickListener {
                mainActivity?.hideKeyboard()
                viewModel.performSignIn(
                    name = nameEt.text.toString(),
                    password = passwordEt.text.toString(),
                )
            }
        }
    }
}
