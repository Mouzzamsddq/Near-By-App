package com.example.bookshelfapp.ui.features.books.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookshelfapp.R
import com.example.bookshelfapp.data.features.books.repository.remote.model.BooksItem
import com.example.bookshelfapp.databinding.BookItemBinding
import com.example.bookshelfapp.utils.setBackground

class BooksAdapter(
    private val clickAction: (BooksItem, Boolean, Int) -> Unit,
) : RecyclerView.Adapter<BooksAdapter.VH>() {

    private var books = emptyList<BooksItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = BookItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(
            binding = binding,
            clickAction = clickAction,
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        books.getOrNull(position)?.let {
            holder.bindBook(it)
        }
    }

    override fun getItemCount() = books.size

    fun updateDataSet(bookSet: List<BooksItem>) {
        books = bookSet
        notifyDataSetChanged()
    }

    inner class VH(
        val binding: BookItemBinding,
        val clickAction: (BooksItem, Boolean, Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindBook(book: BooksItem) {
            Glide.with(itemView.context)
                .load(book.image ?: "")
                .placeholder(R.drawable.ic_default_book)
                .error(R.drawable.ic_default_book)
                .into(binding.ivBook)
            binding.apply {
                bookTitleTv.text = book.title ?: ""
                hitsTv.text = book.hits.toString()
                favIconIv.setBackground(itemView.context, if(book.isFav == true) R.drawable.ic_like else R.drawable.ic_unlike)
                favIconIv.setOnClickListener {
                    clickAction(book, true, adapterPosition)
                }
                itemView.setOnClickListener {
                    clickAction(book, false, 0)
                }
            }
        }
    }
}
