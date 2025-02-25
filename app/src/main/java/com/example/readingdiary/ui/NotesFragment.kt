package com.example.readingdiary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.readingdiary.R
import com.example.readingdiary.models.Note
import com.example.readingdiary.repo.NoteRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.noteTitle)
        val contentText: TextView = itemView.findViewById(R.id.noteContent)
        val deleteButton: FloatingActionButton = itemView.findViewById(R.id.deleteNoteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.titleText.text = note.title
        holder.contentText.text = note.content

        holder.deleteButton.setOnClickListener {
            onDeleteClick(note)
        }
    }

    override fun getItemCount() = notes.size

    fun updateNotes(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }
}
