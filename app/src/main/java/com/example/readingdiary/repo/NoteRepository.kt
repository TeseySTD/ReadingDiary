package com.example.readingdiary.repo

import com.example.readingdiary.models.Note

class NoteRepository private constructor() {

    private val notes = mutableListOf<Note>()

    fun addNote(note: Note) {
        notes.add(note)
    }

    fun getNotes(): List<Note> = notes

    fun removeNote(note: Note) {
        notes.remove(note)
    }

    companion object {
        private var instance: NoteRepository? = null

        fun getInstance(): NoteRepository {
            if (instance == null) {
                instance = NoteRepository()
            }
            return instance!!
        }
    }
}