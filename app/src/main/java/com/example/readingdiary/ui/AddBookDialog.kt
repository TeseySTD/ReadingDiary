package com.example.readingdiary.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.readingdiary.R
import com.example.readingdiary.models.Book
import com.example.readingdiary.repo.BookRepository

class AddBookDialog : DialogFragment() {
    private lateinit var titleEditText: EditText
    private lateinit var authorEditText: EditText
    private lateinit var pagesEditText: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add_book, null)

        titleEditText = dialogView.findViewById(R.id.editTextTitle)
        authorEditText = dialogView.findViewById(R.id.editTextAuthor)
        pagesEditText = dialogView.findViewById(R.id.editTextPages)

        builder.setView(dialogView)
            .setTitle("Add New Book")
            .setPositiveButton("Add") { _, _ ->
                val title = titleEditText.text.toString()
                val author = authorEditText.text.toString()
                val pages = pagesEditText.text.toString().toIntOrNull() ?: 0

                if (title.isNotEmpty() && author.isNotEmpty()) {
                    val book = Book(title, author, pages)
                    val repo = BookRepository.getInstance()
                    repo.addBook(book)
                }
            }
            .setNegativeButton("Cancel", null)

        return builder.create()
    }
}