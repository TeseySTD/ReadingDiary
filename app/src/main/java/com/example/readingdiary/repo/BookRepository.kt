package com.example.readingdiary.repo

import com.example.readingdiary.models.Book

class BookRepository private constructor() {

    private val books = mutableListOf<Book>()

    fun addBook(book: Book) {
        books.add(book)
    }

    fun getBooks(): List<Book> = books

    fun removeBook(book: Book) {
        books.remove(book)
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