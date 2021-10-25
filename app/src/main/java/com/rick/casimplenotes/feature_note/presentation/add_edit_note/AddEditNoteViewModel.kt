package com.rick.casimplenotes.feature_note.presentation.add_edit_note

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rick.casimplenotes.feature_note.domain.model.InvalidNoteException
import com.rick.casimplenotes.feature_note.domain.model.Note
import com.rick.casimplenotes.feature_note.domain.use_case.NoteUseCases
import com.rick.casimplenotes.feature_note.presentation.NoteTextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _noteTitle = mutableStateOf(NoteTextFieldState("", "Enter title...", true))
    val noteTitle get() = _noteTitle

    private val _noteColour = mutableStateOf(Note.noteColors.random().toArgb())
    val noteColour get() = _noteColour

    private val _noteContent = mutableStateOf(NoteTextFieldState("", "Enter some content", true))
    val noteContent get() = _noteContent

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow get() = _eventFlow

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        _noteColour.value = note.color
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.ChangeColor -> _noteColour.value = event.color

            is AddEditNoteEvent.ChangeTitleFocus -> _noteTitle.value = noteTitle.value.copy(
                isHintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank()
            )

            is AddEditNoteEvent.EnteredTitle -> _noteTitle.value =
                noteTitle.value.copy(text = event.value)

            is AddEditNoteEvent.ChangeContentFocus -> _noteContent.value = noteContent.value.copy(
                isHintVisible = !event.focusState.isFocused && noteContent.value.text.isBlank()
            )

            is AddEditNoteEvent.EnteredContent -> _noteContent.value =
                noteContent.value.copy(text = event.value)

            AddEditNoteEvent.SaveNote -> viewModelScope.launch {
                try {
                    noteUseCases.addNote(
                        Note(
                            title = noteTitle.value.text,
                            content = noteContent.value.text,
                            timeStamp = System.currentTimeMillis(),
                            color = noteColour.value,
                            id = currentNoteId
                        )
                    )
                    _eventFlow.emit(UIEvent.SaveNote)
                } catch (e: InvalidNoteException){
                    _eventFlow.emit(UIEvent.ShowSnackbar(e.message ?: "Failed to save note"))
                }
            }
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
        object SaveNote : UIEvent()
    }
}