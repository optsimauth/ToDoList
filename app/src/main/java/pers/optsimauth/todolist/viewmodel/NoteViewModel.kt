package pers.optsimauth.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pers.optsimauth.todolist.dao.NoteDao
import pers.optsimauth.todolist.entity.NoteEntity

class NoteViewModel(private val dao: NoteDao) : ViewModel() {

    // Insert a new note
    fun insert(noteEntity: NoteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(noteEntity)
            withContext(Dispatchers.Main) {
                // Handle the result of the insert operation here
            }
        }
    }

    fun update(noteEntity: NoteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(noteEntity)
            withContext(Dispatchers.Main) {
                // Handle the result of the update operation here
            }
        }
    }

    fun delete(noteEntity: NoteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(noteEntity)
            withContext(Dispatchers.Main) {
                // Handle the result of the delete operation here
            }
        }
    }

    // Get all notes
    fun getAllNotes(): Flow<List<NoteEntity>> {
        return dao.getAllItems()
    }
}