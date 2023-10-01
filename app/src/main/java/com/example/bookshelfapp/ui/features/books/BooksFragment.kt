package com.example.bookshelfapp.ui.features.books

import android.os.Bundle
import android.view.View
import com.example.bookshelfapp.base.BaseFragment
import com.example.bookshelfapp.databinding.FragmentBooksBinding

class BooksFragment : BaseFragment<FragmentBooksBinding>(
    FragmentBooksBinding::inflate,
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
