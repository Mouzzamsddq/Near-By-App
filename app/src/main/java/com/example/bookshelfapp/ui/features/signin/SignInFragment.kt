package com.example.bookshelfapp.ui.features.signin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookshelfapp.R
import com.example.bookshelfapp.base.BaseFragment
import com.example.bookshelfapp.constants.StringConstant
import com.example.bookshelfapp.databinding.FragmentSignInBinding
import com.example.bookshelfapp.ui.features.signin.viewmodel.SignInViewModel
import com.example.bookshelfapp.ui.features.signup.viewmodel.SignUpViewModel
import com.example.bookshelfapp.utils.findNavControllerSafely
import com.example.bookshelfapp.utils.hide
import com.example.bookshelfapp.utils.hideKeyboard
import com.example.bookshelfapp.utils.navigateSafe
import com.example.bookshelfapp.utils.setBackground
import com.example.bookshelfapp.utils.show
import com.example.bookshelfapp.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>(
    FragmentSignInBinding::inflate,
) {

    private val viewModel: SignInViewModel by viewModels()
    private var isPasswordVisible = false

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
        viewModel.fieldsValidationStatus.observe(viewLifecycleOwner) {
            when (it) {
                is SignUpViewModel.FieldsValidationStatus.Success -> {
                    changeSignInButtonState(isEnable = true)
                    changeStateOfNameError(show = false)
                    changeStateOfPasswordError(show = false)
                }

                is SignUpViewModel.FieldsValidationStatus.NameError -> {
                    changeSignInButtonState()
                    changeStateOfNameError(show = true, it.errorMessage)
                }

                is SignUpViewModel.FieldsValidationStatus.PasswordError -> {
                    changeSignInButtonState()
                    changeStateOfPasswordError(show = true, it.errorMessage)
                }
            }
        }
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
            showHideIv.setOnClickListener {
                if (isPasswordVisible) {
                    isPasswordVisible = false
                    passwordEt.transformationMethod = PasswordTransformationMethod.getInstance()
                    showHideIv.setImageResource(R.drawable.ic_hide_password)
                } else {
                    isPasswordVisible = true
                    passwordEt.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    showHideIv.setImageResource(R.drawable.ic_show_password)
                }
                passwordEt.setSelection(passwordEt.text.length)
            }
        }
    }

    private fun changeStateOfNameError(show: Boolean, errorMessage: String = StringConstant.EMPTY_STRING) {
        binding.apply {
            listOf(warningIv, errorReasonTv).apply {
                if (show) show() else hide()
            }
            errorReasonTv.text = errorMessage
        }
    }

    private fun changeStateOfPasswordError(show: Boolean, errorMessage: String = StringConstant.EMPTY_STRING) {
        binding.apply {
            listOf(pwdWarningIv, pwdErrorReasonTv).apply {
                if (show) show() else hide()
            }
            pwdErrorReasonTv.text = errorMessage
        }
    }

    private fun changeSignInButtonState(isEnable: Boolean = false) {
        context?.let { context ->
            binding.loginBtn.apply {
                setBackground(
                    context,
                    if (isEnable) R.drawable.rounded_login_btn_enabled else R.drawable.rounded_rectangle_for_login_btn,
                )
                isEnabled = isEnable
            }
        }
    }
}
