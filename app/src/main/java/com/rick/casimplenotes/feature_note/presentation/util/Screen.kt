package com.rick.casimplenotes.feature_note.presentation.util

sealed class Screen (val route: String) {
    object NoteScreen: Screen("notesScreen")
    object AddEditNoteScreen: Screen("addNoteScreen")
}