package com.example.readingdiary.models

import java.util.Date

open class DiaryEntry(
    var title: String,
    var date: Date = Date()
) {
    open fun getFormattedInfo(): String = "Entry: $title on ${date}"
}