package com.example.readingdiary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.readingdiary.R
import com.example.readingdiary.models.Note
import com.example.readingdiary.repo.NoteRepository
import com.example.readingdiary.ui.compose.ComposeNoteItem

class NotesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter
    private val noteRepository = NoteRepository.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.notesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = NoteAdapter(
            notes = emptyList(),
            onDeleteClick = { note -> noteRepository.removeNote(note) },
        )

        recyclerView.adapter = adapter

        noteRepository.getNotesLiveData().observe(viewLifecycleOwner, Observer { notes ->
            recyclerView.post {
                adapter.updateNotes(notes)
            }
        })
    }
}

class NoteAdapter(
    private var notes: List<Note>,
    private val onDeleteClick: (Note) -> Unit,
) : RecyclerView.Adapter<NoteAdapter.ComposeNoteViewHolder>() {

    inner class ComposeNoteViewHolder(val composeView: ComposeView) : RecyclerView.ViewHolder(composeView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComposeNoteViewHolder {
        val composeView = ComposeView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        return ComposeNoteViewHolder(composeView)
    }

    override fun onBindViewHolder(holder: ComposeNoteViewHolder, position: Int) {
        val note = notes[position]
        holder.composeView.setContent {
            ComposeNoteItem (
                note = note,
                onDeleteClick = onDeleteClick,
            )
        }
    }

    override fun getItemCount() = notes.size

    fun updateNotes(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }
}
