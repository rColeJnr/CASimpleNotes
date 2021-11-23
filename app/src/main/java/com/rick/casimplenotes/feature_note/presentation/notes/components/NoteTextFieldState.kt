package com.rick.casimplenotes.feature_note.presentation.notes.components

data class NoteTextFieldState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true
)
