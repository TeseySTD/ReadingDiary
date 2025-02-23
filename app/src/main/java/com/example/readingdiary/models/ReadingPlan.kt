package com.example.readingdiary.models

class ReadingPlan {
    private val plannedBooks = mutableListOf<Book>()

    val calculateReadingTime: (Book) -> Int = { book ->
        (book.pages * 2)
    }

    fun addBook(book: Book) {
        plannedBooks.add(book)
    }

}