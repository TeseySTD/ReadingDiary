package com.example.readingdiary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.readingdiary.R
import com.example.readingdiary.enums.BookRating
import com.example.readingdiary.models.Book
import com.example.readingdiary.repo.BookRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.roundToInt

class BooksFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookAdapter
    private val bookRepository = BookRepository.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_books, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.booksRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = BookAdapter(
            books = emptyList(),
            onDeleteClick = { book -> bookRepository.removeBook(book) },
            onRatingChanged = { book, rating ->
                bookRepository.changeBookRating(book, rating)
            }
        )

        recyclerView.adapter = adapter

        bookRepository.getBooksLiveData().observe(viewLifecycleOwner, Observer { books ->
            recyclerView.post {
                adapter.updateBooks(books)
            }
        })
    }
}

class BookAdapter(
    private var books: List<Book>,
    private val onDeleteClick: (Book) -> Unit,
    private val onRatingChanged: (Book, BookRating) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.bookTitle)
        val authorText: TextView = itemView.findViewById(R.id.bookAuthor)
        val pagesText: TextView = itemView.findViewById(R.id.bookPages)
        val ratingBar: RatingBar = itemView.findViewById(R.id.bookRating)
        val deleteButton: FloatingActionButton = itemView.findViewById(R.id.deleteBookButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.titleText.text = book.title
        holder.authorText.text = book.author
        holder.pagesText.text = "${book.pages} pages"
        holder.ratingBar.rating = book.getRatingValue().toFloat()

        holder.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            val bookRating = BookRating.fromInt(rating.roundToInt() - 1)
            onRatingChanged(book, bookRating)
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClick(book)
        }
    }

    override fun getItemCount() = books.size

    fun updateBooks(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }
}
