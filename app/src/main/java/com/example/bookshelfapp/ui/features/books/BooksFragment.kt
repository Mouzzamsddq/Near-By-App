package com.example.bookshelfapp.ui.features.books

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
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
        binding.bookListRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = booksAdapter
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
