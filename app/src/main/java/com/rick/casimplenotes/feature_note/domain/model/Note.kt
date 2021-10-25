package com.rick.casimplenotes.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rick.casimplenotes.ui.theme.*
import java.lang.Exception

@Entity
data class Note(
    val title: String,
    val content: String,
    val timeStamp: Long,
    val color: Int,
    @PrimaryKey val id: Int? = null
) {

    companion object {
        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }

}

class InvalidNoteException(message: String): Exception(message)
