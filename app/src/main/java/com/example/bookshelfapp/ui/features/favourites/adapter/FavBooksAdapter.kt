package com.example.bookshelfapp.ui.features.favourites.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookshelfapp.R
import com.example.bookshelfapp.data.features.books.repository.remote.model.BooksItem
import com.example.bookshelfapp.data.features.favourites.repository.local.FavBook
import com.example.bookshelfapp.databinding.CardDiscoverBookBinding

class FavBooksAdapter(
    private val clickAction: (BooksItem, Boolean, Int) -> Unit,
) : RecyclerView.Adapter<FavBooksAdapter.FavBookVH>() {

    private var favBooks = emptyList<FavBook>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavBookVH {
        val binding = CardDiscoverBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavBookVH(
            binding = binding,
            clickAction = clickAction,
        )
    }

    override fun onBindViewHolder(holder: FavBookVH, position: Int) {
        favBooks.getOrNull(position)?.let {
            holder.bindBook(it)
        }
    }

    override fun getItemCount() = favBooks.size

    fun updateDataSet(bookSet: List<FavBook>) {
        favBooks = bookSet
        notifyDataSetChanged()
    }

    inner class FavBookVH(
        val binding: CardDiscoverBookBinding,
        val clickAction: (BooksItem, Boolean, Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindBook(favBook: FavBook) {
            val book = favBook.book
            Glide.with(itemView.context)
                .load(book.image ?: "")
                .placeholder(R.drawable.ic_default_book)
                .error(R.drawable.ic_default_book)
                .into(binding.ivBook)
            binding.apply {
                tvBookName.text = book.title ?: ""
                tvHits.text = book.hits.toString()
//                favIconIv.setBackground(
//                    itemView.context,
//                    if (book.isFav == true) R.drawable.ic_like else R.drawable.ic_unlike,
//                )
//                favIconIv.setOnClickListener {
//                    clickAction(book, true, adapterPosition)
//                }
                itemView.setOnClickListener {
                    clickAction(book, false, 0)
                }
            }
        }
    }
}
