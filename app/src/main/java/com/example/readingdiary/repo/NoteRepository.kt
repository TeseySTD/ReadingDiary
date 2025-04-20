package com.example.readingdiary.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.readingdiary.models.Book
import com.example.readingdiary.models.Note
import com.example.readingdiary.repo.BookRepository.Companion

class NoteRepository private constructor() {

    private var notes = arrayOf<Note>()
    private val notesLiveData = MutableLiveData<List<Note>>(notes.toList())

    init {
        val exampleNote = Note("Example", "Example")
        notes = notes.plus(exampleNote)
        notesLiveData.value = notes.toList()
    }

    fun addNote(note: Note) {
        notes = notes.plus(note)
        notesLiveData.value = notes.toList()
    }

    fun getNotesLiveData(): LiveData<List<Note>> = notesLiveData

    fun removeNote(note: Note) {
        if(notes.indexOf(note) == -1)
        {
            Log.println(Log.WARN, "Note repository error","Note not found in repository")
            return
        }
        notes = notes.filterNot { it == note }.toTypedArray()
        notesLiveData.value = notes.toList()
    }

    companion object {
        private var instance: NoteRepository? = null

        fun getInstance(): NoteRepository {
            if (instance == null) {
                instance = NoteRepository()
            }
            return instance!!
        }

        fun reset() {
            instance = null
        }
    }
}