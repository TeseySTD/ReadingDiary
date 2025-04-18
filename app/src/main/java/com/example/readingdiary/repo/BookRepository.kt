package com.example.readingdiary.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.readingdiary.enums.BookRating
import com.example.readingdiary.enums.ReadingStatus
import com.example.readingdiary.models.Book
import java.util.UUID

class BookRepository private constructor() {

    private val books = mutableMapOf<UUID,Book>()
    private val booksLiveData = MutableLiveData(books.values.toList())

    fun addBook(book: Book) {
        books.set(book.id, book)
        booksLiveData.value = books.values.toList()
    }

    fun getBooksLiveData(): LiveData<List<Book>> = booksLiveData

    fun removeBook(book: Book) {
        if(!books.containsKey(book.id))
        {
            Log.println(Log.WARN, "Book repository error","Book with id: ${book.id} not found in repository")
            return
        }
        books.remove(book.id)
        booksLiveData.value = books.values.toList()
    }

    fun changeBookStatus(book: Book, status: ReadingStatus){
        book.status = status
        if(!books.containsKey(book.id))
        {
            Log.println(Log.WARN, "Book repository error","Book not found in repository")
            return
        }
        books[book.id] = book
        booksLiveData.value = books.values.toList()
    }

    fun changeBookRating(book: Book, rating: BookRating){
        book.rating = rating
        if(!books.containsKey(book.id))
        {
            Log.println(Log.WARN, "Book repository error","Book not found in repository")
            return
        }
        books[book.id] = book
        booksLiveData.value = books.values.toList()
    }

    companion object {
        private var instance: BookRepository? = null

        fun getInstance(): BookRepository {
            if (instance == null) {
                instance = BookRepository()
            }
            return instance!!
        }
    }
}