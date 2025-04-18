package com.example.readingdiary.models

import java.util.Date
import java.util.UUID

open class DiaryEntry(
    var title: String,
    var date: Date = Date(),
    var id: UUID = UUID.randomUUID()
) {
    open fun getFormattedInfo(): String = "Entry: $title on ${date}"
}