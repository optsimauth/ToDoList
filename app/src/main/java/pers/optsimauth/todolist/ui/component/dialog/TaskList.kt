package pers.optsimauth.todolist.ui.component.dialog

import EditTaskDialog
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import pers.optsimauth.todolist.basecomponent.TaskItem
import pers.optsimauth.todolist.entity.CalendarTask
import pers.optsimauth.todolist.entity.FourQuadrantTask

@Composable
fun TasksList(tasks: List<CalendarTask>, onTaskChange: (CalendarTask) -> Unit) {
    var selectedTask by remember { mutableStateOf<CalendarTask?>(null) }

    LazyColumn {
        items(tasks) { task ->
            TaskItem(
                task = task,
                onTaskClick = { selectedTask = it },
                onTaskStatusChange = { updatedTask -> onTaskChange(updatedTask) }
            )
        }
    }
    selectedTask?.let { task ->
        EditTaskDialog(task = task, onConfirm = { updatedTask ->
            onTaskChange(updatedTask)
            selectedTask = null
        }, onDismiss = { selectedTask = null })
    }
}

@Composable
fun TasksList(tasks: List<FourQuadrantTask>, onTaskChange: (FourQuadrantTask) -> Unit) {
    var selectedTask by remember { mutableStateOf<FourQuadrantTask?>(null) }

    LazyColumn {
        items(tasks) { task ->
            TaskItem(
                task = task,
                onTaskClick = { selectedTask = it },
                onTaskStatusChange = { updatedTask -> onTaskChange(updatedTask) }
            )
        }
    }
    selectedTask?.let { task ->
        EditTaskDialog(task = task, onConfirm = { updatedTask ->
            onTaskChange(updatedTask)
            selectedTask = null
        }, onDismiss = { selectedTask = null })
    }
}