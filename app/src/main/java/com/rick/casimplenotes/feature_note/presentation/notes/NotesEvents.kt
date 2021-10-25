package com.rick.casimplenotes.feature_note.presentation.notes

import com.rick.casimplenotes.feature_note.domain.model.Note
import com.rick.casimplenotes.feature_note.domain.util.NoteOrder

sealed class NotesEvents{

    data class Order(val noteOrder: NoteOrder): NotesEvents()
    data class DeleteNote(val note: Note): NotesEvents()
    object RestoreNote: NotesEvents()
    object ToggleOrderSection: NotesEvents()

}
