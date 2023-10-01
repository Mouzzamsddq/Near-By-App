package com.example.bookshelfapp.ui.features.favourites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bookshelfapp.base.BaseFragment
import com.example.bookshelfapp.databinding.FragmentFavouritesBookBinding
import com.example.bookshelfapp.ui.features.favourites.adapter.FavBooksAdapter
import com.example.bookshelfapp.utils.findNavControllerSafely
import com.example.bookshelfapp.utils.gone
import com.example.bookshelfapp.utils.hide
import com.example.bookshelfapp.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouritesBookFragment : BaseFragment<FragmentFavouritesBookBinding>(
    FragmentFavouritesBookBinding::inflate,
) {
    private val viewModel: FavBookViewModel by viewModels()
    private val favBookAdapter: FavBooksAdapter by lazy {
        FavBooksAdapter(
            clickAction = { book, fromFav, pos ->
                if (fromFav) {
//                    viewModel.addRemoveFavBook(book = book, pos = pos)
                } else {
                    findNavControllerSafely()?.navigate(
                        FavouritesBookFragmentDirections.actionFavBookFragmentToBookDetailsFragment(
                            books = book,
                        ),
                    )
                }
            },
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvFavBooks.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = favBookAdapter
        }
        setObservers()
    }

    private fun setObservers() {
        viewModel.getFavBooks().observe(viewLifecycleOwner) { favBooks ->
            binding.apply {
                if (favBooks.isNotEmpty()) {
                    rvFavBooks.show()
                    listOf(noFavBooksIv, noFavBooksTv).hide()
                    favBookAdapter.updateDataSet(favBooks)
                } else {
                    rvFavBooks.gone()
                    listOf(noFavBooksIv, noFavBooksTv).show()
                }
            }
        }
    }
}
