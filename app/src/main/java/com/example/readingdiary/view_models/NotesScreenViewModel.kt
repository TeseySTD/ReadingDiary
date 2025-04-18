package com.example.readingdiary.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.readingdiary.models.Note
import com.example.readingdiary.repo.NoteRepository

class NotesScreenViewModel : ViewModel() {
    private val noteRepository = NoteRepository.getInstance()

    val notes: LiveData<List<Note>> = noteRepository.getNotesLiveData()

    fun deleteNote(note: Note) {
        noteRepository.removeNote(note)
    }
}