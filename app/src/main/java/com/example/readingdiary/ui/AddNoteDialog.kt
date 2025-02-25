package com.example.readingdiary.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.readingdiary.R
import com.example.readingdiary.models.Note
import com.example.readingdiary.repo.NoteRepository

class AddNoteDialog : DialogFragment() {
    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add_note, null)

        titleEditText = dialogView.findViewById(R.id.editTextTitle)
        contentEditText = dialogView.findViewById(R.id.editTextContent)

        builder.setView(dialogView)
            .setTitle("Add New Note")
            .setPositiveButton("Add") { _, _ ->
                val title = titleEditText.text.toString()
                val content = contentEditText.text.toString()

                if (title.isNotEmpty() && content.isNotEmpty()) {
                    val note = Note(title, content)
                    val repo = NoteRepository.getInstance()
                    repo.addNote(note)
                }
            }
            .setNegativeButton("Cancel", null)

        return builder.create()
    }
}