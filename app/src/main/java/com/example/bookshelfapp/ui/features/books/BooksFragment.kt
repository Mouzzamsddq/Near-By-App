package com.example.bookshelfapp.ui.features.books

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.CompoundButton.OnCheckedChangeListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookshelfapp.R
import com.example.bookshelfapp.base.BaseFragment
import com.example.bookshelfapp.databinding.FragmentBooksBinding
import com.example.bookshelfapp.ui.features.books.adapter.BooksAdapter
import com.example.bookshelfapp.ui.features.books.viewmodel.BooksViewModel
import com.example.bookshelfapp.utils.findNavControllerSafely
import com.example.bookshelfapp.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BooksFragment : BaseFragment<FragmentBooksBinding>(
    FragmentBooksBinding::inflate,
) {

    private val viewModel: BooksViewModel by viewModels()
    private val booksAdapter: BooksAdapter by lazy {
        BooksAdapter(
            clickAction = { book, fromFav, pos ->
                if (fromFav) {
                    viewModel.addRemoveFavBook(book = book, pos = pos)
                } else {
                    findNavControllerSafely()?.navigate(
                        BooksFragmentDirections.actionBooksFragmentToBookDetailsFragment(
                            books = book,
                        ),
                    )
                }
            },
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.refreshFavData()
        setObservers()
        setupSortSpinner()
        binding.bookListRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = booksAdapter
        }
        binding.orderSwitch.setOnCheckedChangeListener { p0, isChecked ->
            viewModel.performSort(binding.sortSpinner.selectedItemPosition, isChecked)
        }
    }

    private fun setupSortSpinner() {
        binding.sortSpinner.apply {
            val sortList = resources.getStringArray(R.array.sort_list)
            val arrayAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, sortList)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            adapter = arrayAdapter

            onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View?,
                    position: Int,
                    id: Long,
                ) {
                    if (position != 0) {
                        viewModel.performSort(position, binding.orderSwitch.isChecked)
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                }
            }
        }
    }

    private fun setObservers() {
        viewModel.booksDataStatus.observe(viewLifecycleOwner) {
            when (it) {
                is BooksViewModel.BooksDataStatus.Success -> {
                    showHideLoaderView(show = false)
                    booksAdapter.updateDataSet(bookSet = it.books)
                }

                is BooksViewModel.BooksDataStatus.Error -> {
                    showHideLoaderView(show = false)
                    context.showToast(it.errorMessage)
                }

                is BooksViewModel.BooksDataStatus.Loading -> {
                    showHideLoaderView(show = true)
                }
            }
        }
    }
}
