package com.rick.casimplenotes.di

import android.app.Application
import androidx.room.Room
import com.rick.casimplenotes.feature_note.data.data_source.NoteDatabase
import com.rick.casimplenotes.feature_note.data.data_source.NoteDatabase.Companion.DATABASE_NAME
import com.rick.casimplenotes.feature_note.data.repository.NoteRepositoryImpl
import com.rick.casimplenotes.feature_note.domain.repository.NoteRepository
import com.rick.casimplenotes.feature_note.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesNoteDatabase(app: Application): NoteDatabase =
        Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            DATABASE_NAME
        ).build()

    @Provides
    @Singleton
    fun providesNoteRepository(db: NoteDatabase): NoteRepository{
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun providesNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository),
            getNote = GetNote(repository)
        )
    }

}