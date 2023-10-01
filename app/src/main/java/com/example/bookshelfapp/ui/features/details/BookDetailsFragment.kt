package com.example.bookshelfapp.ui.features.details

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.bookshelfapp.R
import com.example.bookshelfapp.base.BaseFragment
import com.example.bookshelfapp.constants.StringConstant
import com.example.bookshelfapp.data.features.books.repository.remote.model.BooksItem
import com.example.bookshelfapp.databinding.FragmentBookDetailsBinding
import com.example.bookshelfapp.utils.findNavControllerSafely

class BookDetailsFragment : BaseFragment<FragmentBookDetailsBinding>(
    FragmentBookDetailsBinding::inflate,
) {

    private val bookDetailsArgs: BookDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBookDetails(bookDetailsArgs.books)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.apply {
            backIconIv.setOnClickListener {
                findNavControllerSafely()?.navigateUp()
            }
        }
    }

    private fun setupBookDetails(book: BooksItem?) {
        binding.apply {
            book?.let {
                context?.let { context ->
                    Glide.with(context)
                        .load(book.image ?: "")
                        .placeholder(R.drawable.ic_default_book)
                        .error(R.drawable.ic_default_book)
                        .into(ivBook)
                    favIconIv.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            if (book.isFav == true) R.drawable.ic_like else R.drawable.ic_unlike,
                        ),
                    )
                }
                tvTitle.text = it.title ?: StringConstant.EMPTY_STRING
                tvHits.text = it.hits.toString()
            }
        }
    }
}
