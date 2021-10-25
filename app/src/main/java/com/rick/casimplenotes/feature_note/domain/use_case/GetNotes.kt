package com.rick.casimplenotes.feature_note.domain.use_case

import com.rick.casimplenotes.feature_note.domain.model.Note
import com.rick.casimplenotes.feature_note.domain.repository.NoteRepository
import com.rick.casimplenotes.feature_note.domain.util.NoteOrder
import com.rick.casimplenotes.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotes (
    private val repository: NoteRepository
){
    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)
    ): Flow<List<Note>> {
        return repository.getNotes().map { notes ->
            when(noteOrder.orderType){
                OrderType.Ascending -> {
                    when(noteOrder){
                        is NoteOrder.Title -> notes.sortedBy { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedBy { it.timeStamp }
                        is NoteOrder.Colour -> notes.sortedBy { it.color }
                    }
                }
                OrderType.Descending -> {
                    when(noteOrder){
                        is NoteOrder.Title -> notes.sortedByDescending { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedByDescending { it.timeStamp }
                        is NoteOrder.Colour -> notes.sortedByDescending { it.color }
                    }
                }
            }
        }
    }
}