package pers.optsimauth.todolist.viewmodel

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pers.optsimauth.todolist.DatabaseUtils.importDatabaseJson
import pers.optsimauth.todolist.database.AppDatabase
import pers.optsimauth.todolist.entity.NoteEntity
import pers.optsimauth.todolist.entity.Task
import java.time.LocalDate

class MainViewModel(
    val calendarTaskViewModel: CalendarTaskViewModel,
    val fourQuadrantTaskViewModel: FourQuadrantTaskViewModel,
    val noteViewModel: NoteViewModel,
) : ViewModel() {
    private var pickFileLauncher: ActivityResultLauncher<Intent>? = null


    var focusedDate by mutableStateOf(LocalDate.now())
        private set

    var focusedQuadrant by mutableIntStateOf(1)
        private set

    var showToDoCalendarAddTaskDialog by mutableStateOf(false)
        private set

    var showFourQuadrantAddTaskDialog by mutableStateOf(false)
        private set

//    val backup = RoomBackup(ContextHolder.appContext)


    private var _noteEntity by mutableStateOf<NoteEntity?>(null)
    val noteEntity get() = _noteEntity

    fun setNoteEntity(noteEntity: NoteEntity?) {
        _noteEntity = noteEntity
    }


    fun saveNote(noteEntity: NoteEntity) {
        if (_noteEntity == null) {
            noteViewModel.insert(noteEntity)
        } else {
            noteViewModel.update(noteEntity)
        }
    }


    fun onDateFocused(date: LocalDate) {
        focusedDate = date
    }

    fun onQuadrantFocused(quadrant: Int) {
        focusedQuadrant = quadrant
    }

    fun showCalendarDialog() {
        showToDoCalendarAddTaskDialog = true
    }

    fun showQuadrantDialog() {
        showFourQuadrantAddTaskDialog = true
    }

    fun hideCalendarDialog() {
        showToDoCalendarAddTaskDialog = false
    }

    fun hideQuadrantDialog() {
        showFourQuadrantAddTaskDialog = false
    }

    fun addCalendarTask(task: Task.CalendarTaskEntity) {
        calendarTaskViewModel.insert(task)
        hideCalendarDialog()
    }

    fun addQuadrantTask(task: Task.FourQuadrantTaskEntity) {
        fourQuadrantTaskViewModel.insert(task)
        hideQuadrantDialog()

    }


    fun initializeAndCleanup(activity: ComponentActivity) {
        calendarTaskViewModel.deleteAllCheckedTasks()
        fourQuadrantTaskViewModel.deleteAllCheckedTasks()
        initializeFilePicker(activity)

    }

    private fun initializeFilePicker(activity: ComponentActivity) {
        pickFileLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    viewModelScope.launch {
                        val importResult = importDatabaseJson(
                            ContextHolder.appContext,
                            uri,
                            AppDatabase.getInstance(ContextHolder.appContext)
                        )
                        when {
                            importResult.isSuccess -> {
                                Toast.makeText(
                                    ContextHolder.appContext,
                                    "导入成功",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            importResult.isFailure -> {
                                AlertDialog.Builder(activity)
                                    .setTitle("Import Failed")
                                    .setMessage("导入失败: ${importResult.exceptionOrNull()?.message}")
                                    .setPositiveButton("OK") { dialog, _ ->
                                        dialog.dismiss()
                                    }
                                    .show()
                                println("导入失败: ${importResult.exceptionOrNull()?.message}")
                            }
                        }
                    }
                }
            }
        }
    }

    fun launchFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
        }
        pickFileLauncher?.launch(intent)
    }


}

class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            val database = AppDatabase.getInstance(application)

            val calendarTaskViewModel = CalendarTaskViewModel(database.calendarTaskDao())
            val fourQuadrantTaskViewModel =
                FourQuadrantTaskViewModel(database.fourQuadrantTaskDao())
            val noteViewModel = NoteViewModel(database.noteDao())
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                calendarTaskViewModel,
                fourQuadrantTaskViewModel,
                noteViewModel
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}