package com.example.readingdiary.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.readingdiary.R
import com.example.readingdiary.enums.BookRating
import com.example.readingdiary.enums.ReadingStatus
import com.example.readingdiary.models.Book
import com.example.readingdiary.models.ReadingPlan
import com.example.readingdiary.repo.BookRepository
import com.example.readingdiary.repo.ReadingPlanRepository
import com.example.readingdiary.ui.compose.ComposeBookItem
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.roundToInt

class BooksFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookAdapter
    private lateinit var makeReadingPlanButton: ExtendedFloatingActionButton
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
        makeReadingPlanButton = view.findViewById(R.id.makeReadingPlanButton)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = BookAdapter(
            books = emptyList(),
            onDeleteClick = { book -> bookRepository.removeBook(book) },
            onRatingChanged = { book, rating ->
                bookRepository.changeBookRating(book, rating)
            },
            onCheckboxChanged = { selectedCount ->
                makeReadingPlanButton.visibility = if (selectedCount > 0) View.VISIBLE else View.GONE
            }
        )

        recyclerView.adapter = adapter

        makeReadingPlanButton.setOnClickListener {
            val selectedBooks = adapter.getSelectedBooks()
            if (selectedBooks.isNotEmpty()) {
                val readingPlan = ReadingPlan(selectedBooks)
                ReadingPlanRepository.getInstance().addReadingPlan(readingPlan)
                Toast.makeText(context, "Reading plan created", Toast.LENGTH_SHORT).show()
                adapter.clearSelection()
            } else {
                Toast.makeText(context, "No books selected", Toast.LENGTH_SHORT).show()
            }
        }

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
    private val onRatingChanged: (Book, BookRating) -> Unit,
    private val onCheckboxChanged: (selectedCount: Int) -> Unit)
    : RecyclerView.Adapter<BookAdapter.ComposeBookViewHolder>() {

    private val selectedBooks = mutableSetOf<Book>()

    inner class ComposeBookViewHolder(val composeView: ComposeView) : RecyclerView.ViewHolder(composeView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComposeBookViewHolder {
        val composeView = ComposeView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        return ComposeBookViewHolder(composeView)
    }

    override fun onBindViewHolder(holder: ComposeBookViewHolder, position: Int) {
        val book = books[position]
        holder.composeView.setContent {
            ComposeBookItem(
                book = book,
                onDeleteClick = onDeleteClick,
                onRatingChanged = { b, rating ->
                    onRatingChanged(b, rating)
                },
                onCheckboxChanged = { b, isChecked ->
                    if (isChecked) {
                        selectedBooks.add(b)
                    } else {
                        selectedBooks.remove(b)
                    }
                    onCheckboxChanged(selectedBooks.size)
                },
            )
        }
    }
    override fun getItemCount() = books.size

    fun updateBooks(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }

    fun getSelectedBooks(): List<Book> = selectedBooks.toList()

    fun clearSelection() {
        val repository = BookRepository.getInstance()
        selectedBooks.forEach { repository.changeBookStatus(it, ReadingStatus.IN_PROGRESS) }

        selectedBooks.clear()
        onCheckboxChanged(0)
        notifyDataSetChanged()
    }
}
