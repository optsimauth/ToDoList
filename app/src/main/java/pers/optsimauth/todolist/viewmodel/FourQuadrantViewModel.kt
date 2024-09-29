package pers.optsimauth.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import pers.optsimauth.todolist.dao.FourQuadrantTaskDao
import pers.optsimauth.todolist.entity.Task

class FourQuadrantTaskViewModel(private val dao: FourQuadrantTaskDao) : ViewModel() {


    // Get tasks by quadrant
    fun getTasksByQuadrant(quadrant: Int): Flow<List<Task.FourQuadrantTask>> {
        return dao.getTasksByQuadrant(quadrant)

    }

    // Insert a new task
    fun insert(task: Task.FourQuadrantTask) {
        viewModelScope.launch(Dispatchers.IO) {

            dao.insert(task)
        }
    }

    // Update an existing task
    fun update(task: Task.FourQuadrantTask) {
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