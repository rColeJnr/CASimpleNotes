package com.rick.casimplenotes.feature_note.domain.util

sealed class NoteOrder(val orderType: OrderType) {
    class Title(orderType: OrderType): NoteOrder(orderType)
    class Date(orderType: OrderType): NoteOrder(orderType)
    class Colour(orderType: OrderType): NoteOrder(orderType)

    fun dude(orderType: OrderType): NoteOrder {
        return when(this){
            is Colour -> Colour(orderType)
            is Date -> Date(orderType)
            is Title -> Title(orderType)
        }
    }
}
