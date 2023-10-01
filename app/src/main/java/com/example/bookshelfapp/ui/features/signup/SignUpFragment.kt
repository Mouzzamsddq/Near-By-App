package com.example.bookshelfapp.ui.features.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.example.bookshelfapp.R
import com.example.bookshelfapp.base.BaseFragment
import com.example.bookshelfapp.constants.StringConstant
import com.example.bookshelfapp.constants.StringConstant.EMPTY_STRING
import com.example.bookshelfapp.databinding.FragmentSignUpBinding
import com.example.bookshelfapp.ui.features.signup.viewmodel.SignUpViewModel
import com.example.bookshelfapp.utils.JsonUtils
import com.example.bookshelfapp.utils.findNavControllerSafely
import com.example.bookshelfapp.utils.hide
import com.example.bookshelfapp.utils.hideKeyboard
import com.example.bookshelfapp.utils.navigateSafe
import com.example.bookshelfapp.utils.setBackground
import com.example.bookshelfapp.utils.show
import com.example.bookshelfapp.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(
    FragmentSignUpBinding::inflate,
) {

    private val viewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateSelectedCountry(selectedCountry = getString(R.string.select_a_country))
        setupSpinner()
        setOnClickListener()
        addOnTextChangeListener()
        setObservers()
    }

    private fun setObservers() {
        viewModel.fieldsValidationStatus.observe(viewLifecycleOwner) {
            when (it) {
                is SignUpViewModel.FieldsValidationStatus.Success -> {
                    changeSignUpButtonState(isEnable = true)
                    changeStateOfNameError(show = false)
                    changeStateOfPasswordError(show = false)
                }

                is SignUpViewModel.FieldsValidationStatus.NameError -> {
                    changeSignUpButtonState()
                    changeStateOfNameError(show = true, it.errorMessage)
                }

                is SignUpViewModel.FieldsValidationStatus.PasswordError -> {
                    changeSignUpButtonState()
                    changeStateOfPasswordError(show = true, it.errorMessage)
                }
            }
        }

        viewModel.signUpStatus.observe(viewLifecycleOwner) {
            when (it) {
                is SignUpViewModel.SignUpStatus.Success -> {
                    showHideLoaderView(show = false)
                    context.showToast(message = getString(R.string.signup_successful))
                    findNavControllerSafely()?.navigateSafe(
                        action = R.id.action_sign_up_fragment_to_signInFragment,
                        args = null,
                    )
                }

                is SignUpViewModel.SignUpStatus.Error -> {
                    showHideLoaderView(show = false)
                    context.showToast(it.errorMessage)
                }

                is SignUpViewModel.SignUpStatus.Loading -> {
                    showHideLoaderView(show = true)
                }
            }
        }
    }

    private fun changeSignUpButtonState(isEnable: Boolean = false) {
        context?.let { context ->
            binding.signUpBtn.setBackground(
                context,
                if (isEnable) R.drawable.rounded_login_btn_enabled else R.drawable.rounded_rectangle_for_login_btn,
            )
        }
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

    private fun setupSpinner() {
        binding.countrySpinner.apply {
            val countryJsonData =
                JsonUtils.loadJSONFromAsset(context = requireContext(), "countryData.json")
            countryJsonData?.let {
                val countriesList = extractCountryList(it)
                val arrayAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    countriesList,
                )
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                adapter = arrayAdapter
            }

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View,
                    position: Int,
                    id: Long,
                ) {
                    if (position != 0) {
                        setSelection(position)
                        viewModel.updateSelectedCountry(
                            selectedCountry = parentView.getItemAtPosition(
                                position,
                            ).toString(),
                        )
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                }
            }
        }
    }

    private fun extractCountryList(jsonObject: JSONObject): List<String> {
        val countryList = mutableListOf<String>()
        countryList.add(getString(R.string.select_a_country))
        jsonObject.optJSONObject(StringConstant.DATA)?.let { dataObject ->
            for (countryCode in dataObject.keys()) {
                val countryObject = dataObject.optJSONObject(countryCode)
                val countryName = countryObject?.optString(StringConstant.COUNTRY)
                countryName?.let { countryList.add(it) }
            }
        }
        return countryList
    }

    private fun setOnClickListener() {
        binding.apply {
            loginHereTv.setOnClickListener {
                findNavControllerSafely()?.navigateSafe(
                    action = R.id.action_sign_up_fragment_to_signInFragment,
                    args = null,
                )
            }
            signUpBtn.setOnClickListener {
                mainActivity?.hideKeyboard()
                val selectedCountryPos = countrySpinner.selectedItemPosition
                if (selectedCountryPos == 0) {
                    context.showToast(message = getString(R.string.select_a_country))
                    return@setOnClickListener
                }
                viewModel.performSignUp(
                    name = nameEt.text.toString(),
                    password = passwordEt.text.toString(),
                )
            }
        }
    }

    private fun changeStateOfNameError(show: Boolean, errorMessage: String = EMPTY_STRING) {
        binding.apply {
            listOf(warningIv, errorReasonTv).apply {
                if (show) show() else hide()
            }
            errorReasonTv.text = errorMessage
        }
    }

    private fun changeStateOfPasswordError(show: Boolean, errorMessage: String = EMPTY_STRING) {
        binding.apply {
            listOf(passwordWarningIv, passwordErrorReasonTv).apply {
                if (show) show() else hide()
            }
            passwordErrorReasonTv.text = errorMessage
        }
    }
}
