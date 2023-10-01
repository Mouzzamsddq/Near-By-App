package com.example.bookshelfapp.ui.features.details

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.bookshelfapp.R
import com.example.bookshelfapp.base.BaseFragment
import com.example.bookshelfapp.constants.StringConstant
import com.example.bookshelfapp.data.features.books.repository.remote.model.BooksItem
import com.example.bookshelfapp.databinding.FragmentBookDetailsBinding
import com.example.bookshelfapp.utils.findNavControllerSafely
import com.example.bookshelfapp.utils.setDominantBackground

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
                    Glide.with(context).asBitmap().listener(object :
                        RequestListener<Bitmap> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            isFirstResource: Boolean,
                        ): Boolean {
                            return true
                        }

                        override fun onResourceReady(
                            resource: Bitmap?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean,
                        ): Boolean {
                            binding.bookBackground.setDominantBackground(resource)
                            resource?.let { ivBook.setImageBitmap(resource) }
                            return true
                        }
                    }).load(book.image ?: "").placeholder(R.drawable.ic_default_book)
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
