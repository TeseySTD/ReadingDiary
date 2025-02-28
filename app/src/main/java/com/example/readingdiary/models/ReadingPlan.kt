package com.example.readingdiary.models

import com.example.readingdiary.models.Book.Companion.toShortBookList
import java.time.LocalDate
import java.util.Date

class ReadingPlan(books: List<Book>, title: String = "Plan of date ${Date()}", date: Date = Date()) : DiaryEntry(title, date) {
    private val plannedBooks = mutableListOf<Book>()

    val calculateReadingTime: Int
        get(){
            var time = 0
            plannedBooks.forEach { time += it.pages * 2 }
            return time
        }

    fun addBook(book: Book) {
        plannedBooks.add(book)
    }

    fun getBooks():List<Book> = plannedBooks

    fun getFormatedBooks(): MutableList<String> = plannedBooks.toShortBookList()

    init {
        plannedBooks.addAll(books)
    }
}