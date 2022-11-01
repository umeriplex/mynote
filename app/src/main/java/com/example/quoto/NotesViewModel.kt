package com.example.quoto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cheezycode.notesample.models.NoteRequest
import com.example.quoto.repo.NoteRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val noteRepo: NoteRepo) : ViewModel() {

    val notesLiveData get() = noteRepo.notesLivedata
    val statusLiveData get() = noteRepo.statusLivedata

    fun getNote() {
        viewModelScope.launch {
            noteRepo.getNotes()
        }
    }

    fun createNote(noteRequest: NoteRequest) {
        viewModelScope.launch {
            noteRepo.createNote(noteRequest)
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            noteRepo.deleteNote(noteId)
        }
    }

    fun updateNote(noteId: String, noteRequest: NoteRequest) {
        viewModelScope.launch {
            noteRepo.updateNote(noteId, noteRequest )
        }
    }
}