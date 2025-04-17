package com.example.readingdiary.ui.compose.screens

import com.example.readingdiary.ui.compose.components.ComposeNoteItem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.readingdiary.models.Note
import com.example.readingdiary.repo.NoteRepository


@Composable
fun NotesScreen() {
    val noteRepository = NoteRepository.getInstance()
    val notes by noteRepository.getNotesLiveData().observeAsState(initial = emptyList())

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NotesList(
                notes = notes,
                onDeleteClick = { note ->
                    noteRepository.removeNote(note)
                }
            )
        }
    }
}

@Composable
fun NotesList(
    notes: List<Note>,
    onDeleteClick: (Note) -> Unit
) {
    LazyColumn {
        items(notes) { note ->
            ComposeNoteItem(
                note = note,
                onDeleteClick = { onDeleteClick(note) }
            )
        }
    }
}