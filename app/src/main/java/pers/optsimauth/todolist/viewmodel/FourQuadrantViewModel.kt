package pers.optsimauth.todolist.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import pers.optsimauth.todolist.dao.FourQuadrantTaskDao
import pers.optsimauth.todolist.entity.Task

class FourQuadrantTaskViewModel(private val dao: FourQuadrantTaskDao) : ViewModel() {


    // Get all tasks
    private val _allItems = mutableStateListOf<Task.FourQuadrantTaskEntity>()


    init {
        viewModelScope.launch {
            dao.getAllItems().collectLatest { tasks ->
                _allItems.clear()
                _allItems.addAll(tasks) // Add new task list
            }
        }
    }

    // Get tasks by quadrant
    fun getTasksByQuadrant(quadrant: Int): List<Task.FourQuadrantTaskEntity> {
        return _allItems.filter { it.quadrant == quadrant }

    }

    // Insert a new task
    fun insert(task: Task.FourQuadrantTaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {

            dao.insert(task)
        }
    }

    // Update an existing task
    fun update(task: Task.FourQuadrantTaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {

            dao.update(task)
        }
    }


    // Delete all checked tasks
    fun deleteAllCheckedTasks() {
        viewModelScope.launch(Dispatchers.IO) {

            dao.deleteAllCheckedTasks()
        }
    }


}


class FourQuadrantTaskViewModelFactory(private val dao: FourQuadrantTaskDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FourQuadrantTaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FourQuadrantTaskViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}