package com.example.readingdiary.models

import com.example.readingdiary.enums.BookRating
import com.example.readingdiary.enums.ReadingStatus
import java.util.Date
import java.util.UUID

class Book(
    title: String,
    var author: String,
    var pages: Int,
    date: Date = Date(),
    var status: ReadingStatus = ReadingStatus.NOT_STARTED,
    var rating: BookRating? = null,
) : DiaryEntry(title, date) {

    constructor(title: String, author: String) : this(title, author, 0)

    override fun getFormattedInfo(): String = "Book: $title by $author"

    fun markAsRead() {
        status = ReadingStatus.COMPLETED
        date = Date()
    }

    fun getRatingValue(): Int = rating?.ordinal?.plus(1) ?: 0

    companion object{
        fun MutableList<Book>.toShortBookList() : MutableList<String> {
            var result = mutableListOf<String>();
            for(b in this){
                result.add(b.title)
            }
            return result
        }
    }
}
