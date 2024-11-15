package pers.optsimauth.todolist.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pers.optsimauth.todolist.DatabaseUtils
import pers.optsimauth.todolist.dao.CalendarTaskDao
import pers.optsimauth.todolist.database.AppDatabase
import pers.optsimauth.todolist.entity.Task

class CalendarTaskViewModel(private val dao: CalendarTaskDao) : ViewModel() {


    // Get tasks for a specific day
    fun getTasksByDay(day: String): Flow<List<Task.CalendarTaskEntity>> {
        return dao.getTasksByDay(day)
    }

    // Insert a new task
    fun insert(task: Task.CalendarTaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(task)
            withContext(Dispatchers.Main) {
                // 在这里处理插入操作的结果

                DatabaseUtils.exportDatabaseJson(
                    context = ContextHolder.appContext,
                    database = AppDatabase.getInstance(ContextHolder.appContext)
                )

//                DatabaseUtils.exportDatabase(
//                    ContextHolder.appContext,
//                )


            }
        }
    }

    // Update an existing task
    fun update(task: Task.CalendarTaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(task)
            withContext(Dispatchers.Main) {
                // 在这里处理更新操作的结果
            }

        }
    }

    // Delete a task
    fun delete(task: Task.CalendarTaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(task)
            withContext(Dispatchers.Main) {
                // 在这里处理删除操作的结果
            }
        }
    }


    fun deleteAllCheckedTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteAllCheckedTasks()
            withContext(Dispatchers.Main) {
                // 在这里处理删除操作的结果
            }
        }
    }
}

// Factory for creating pers.optsimauth.todolist.viewmodel.CalendarTaskViewModel
class CalendarTaskViewModelFactory(private val dao: CalendarTaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalendarTaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CalendarTaskViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

object ContextHolder {
    lateinit var appContext: Context

    fun initialize(context: Context) {
        appContext = context
    }
}