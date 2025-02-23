package com.example.readingdiary.models

import java.util.Date

class Note(
    title: String,
    var content: String,
    date: Date = Date()
) : DiaryEntry(title, date) {
    val getWordCount: () -> Int = { content.split("\\s+".toRegex()).size }
}