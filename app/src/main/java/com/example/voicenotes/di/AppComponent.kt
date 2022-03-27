package com.example.voicenotes.di

import android.content.Context
import com.example.voicenotes.ui.auth.AuthFragment
import com.example.voicenotes.ui.notes.NotesFragment
import com.example.voicenotes.ui.record.RecordFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(recordFragment: RecordFragment)
    fun inject(notesFragment: NotesFragment)
    fun inject(authFragment: AuthFragment)
}