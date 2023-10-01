package com.example.bookshelfapp.ui.features.signup

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.bookshelfapp.R
import com.example.bookshelfapp.base.BaseFragment
import com.example.bookshelfapp.constants.StringConstant
import com.example.bookshelfapp.databinding.FragmentSignUpBinding
import com.example.bookshelfapp.utils.JsonUtils
import com.example.bookshelfapp.utils.findNavControllerSafely
import com.example.bookshelfapp.utils.navigateSafe
import org.json.JSONObject

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(
    FragmentSignUpBinding::inflate,
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinner()
        setOnClickListener()
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
        }
    }
}
