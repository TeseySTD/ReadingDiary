package com.example.readingdiary.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.readingdiary.enums.BookRating
import com.example.readingdiary.models.Book

class BookRepository private constructor() {

    private val books = mutableListOf<Book>()
    private val booksLiveData = MutableLiveData<List<Book>>(books)

    fun addBook(book: Book) {
        books.add(book)
        booksLiveData.value = books.toList()
    }

    fun getBooksLiveData(): LiveData<List<Book>> = booksLiveData

    fun removeBook(book: Book) {
        if(books.indexOf(book) == -1)
        {
            Log.println(Log.WARN, "Book repository error","Book not found in repository")
            return
        }
        books.remove(book)
        booksLiveData.value = books.toList()
    }

    fun changeBookRating(book: Book, rating: BookRating){
        val index = books.indexOf(book)
        book.rating = rating
        if(index == -1)
        {
            Log.println(Log.WARN, "Book repository error","Book not found in repository")
            return
        }
        books[index] = book
        booksLiveData.value = books.toList()
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