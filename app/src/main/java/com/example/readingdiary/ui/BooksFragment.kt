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
    private val onCheckboxChanged: (selectedCount: Int) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private val selectedBooks = mutableSetOf<Book>()

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.bookTitle)
        val authorText: TextView = itemView.findViewById(R.id.bookAuthor)
        val bookStatus: TextView = itemView.findViewById(R.id.bookStatus)
        val pagesText: TextView = itemView.findViewById(R.id.bookPages)
        val ratingBar: RatingBar = itemView.findViewById(R.id.bookRating)
        val checkBox: CheckBox = itemView.findViewById(R.id.bookCheckBox)
        val deleteButton: FloatingActionButton = itemView.findViewById(R.id.deleteBookButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.titleText.text = book.title
        holder.authorText.text = "Author: ${book.author}"
        holder.bookStatus.text = "Status: ${book.status.name }"
        holder.pagesText.text = "${book.pages} pages"
        holder.ratingBar.rating = book.getRatingValue().toFloat()

        holder.checkBox.visibility = if (book.status == ReadingStatus.NOT_STARTED) View.VISIBLE else View.GONE

        holder.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            val bookRating = BookRating.fromInt(rating.roundToInt() - 1)
            onRatingChanged(book, bookRating)
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClick(book)
        }

        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = selectedBooks.contains(book)
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedBooks.add(book)
            } else {
                selectedBooks.remove(book)
            }
            onCheckboxChanged(selectedBooks.size)
        }
    }

    override fun getItemCount() = books.size

    fun updateBooks(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }

    fun getSelectedBooks(): List<Book> {
        return selectedBooks.toList()
    }

    fun clearSelection() {
        val repository = BookRepository.getInstance()
        selectedBooks.forEach { repository.changeBookStatus(it, ReadingStatus.IN_PROGRESS) }

        selectedBooks.clear()
        onCheckboxChanged(0)
        notifyDataSetChanged()
    }
}
