package com.example.readingdiary.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.readingdiary.models.Book
import com.example.readingdiary.models.Note

class NoteRepository private constructor() {

    private val notes = mutableListOf<Note>()
    private val notesLiveData = MutableLiveData<List<Note>>(notes)


    fun addNote(note: Note) {
        notes.add(note)
        notesLiveData.value = notes
    }

    fun getNotesLiveData(): LiveData<List<Note>> = notesLiveData

    fun removeNote(note: Note) {
        if(notes.indexOf(note) == -1)
        {
            Log.println(Log.WARN, "Note repository error","Note not found in repository")
            return
        }
        notes.remove(note)
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
    }
}