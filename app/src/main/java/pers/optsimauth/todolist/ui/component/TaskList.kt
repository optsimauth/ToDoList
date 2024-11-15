package pers.optsimauth.todolist.ui.component

import EditCalendarTaskDialog
import EditFourQuadrantTaskDialog
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import pers.optsimauth.todolist.basecomponent.CalendarTaskItem
import pers.optsimauth.todolist.basecomponent.FourQuadrantTaskItem
import pers.optsimauth.todolist.entity.Task

@Composable
fun <T : Task> TaskList(
    tasks: List<T>,
    onTaskChange: (T) -> Unit,
    taskItem: @Composable (task: T, onTaskClick: (T) -> Unit, onTaskStatusChange: (T) -> Unit) -> Unit,
    editDialog: @Composable (initialTask: T, onConfirm: (T) -> Unit, onDismiss: () -> Unit) -> Unit,
) {
    var selectedTask by remember { mutableStateOf<T?>(null) }

    LazyColumn {
        items(items = tasks,
            key = { it.id }) { task ->

            taskItem(
                task = task,
                onTaskClick = { selectedTask = it },
                onTaskStatusChange = { updatedTask -> onTaskChange(updatedTask) }
            )

        }
    }

    selectedTask?.let { task ->
        editDialog(
            initialTask = task,
            onConfirm = { updatedTask ->
                onTaskChange(updatedTask)
                selectedTask = null
            },
            onDismiss = { selectedTask = null }
        )
    }
}

@Composable
fun CalendarTasksList(
    tasks: List<Task.CalendarTaskEntity>,
    onTaskChange: (Task.CalendarTaskEntity) -> Unit,
) {
    TaskList(
        tasks = tasks,
        onTaskChange = onTaskChange,
        taskItem = { task, onTaskClick, onTaskStatusChange ->
            CalendarTaskItem(
                task = task,
                onTaskClick = onTaskClick,
                onTaskStatusChange = onTaskStatusChange
            )
        },
        editDialog = { initialTask, onConfirm, onDismiss ->
            EditCalendarTaskDialog(
                initialTask = initialTask,
                onConfirm = onConfirm,
                onDismiss = onDismiss
            )
        }
    )
}

@Composable
fun FourQuadrantTaskList(
    tasks: List<Task.FourQuadrantTaskEntity>,
    onTaskChange: (Task.FourQuadrantTaskEntity) -> Unit,
) {
    TaskList(
        tasks = tasks,
        onTaskChange = onTaskChange,
        taskItem = { task, onTaskClick, onTaskStatusChange ->
            FourQuadrantTaskItem(
                task = task,
                onTaskClick = onTaskClick,
                onTaskStatusChange = onTaskStatusChange
            )
        },
        editDialog = { initialTask, onConfirm, onDismiss ->
            EditFourQuadrantTaskDialog(
                initialTask = initialTask,
                onConfirm = onConfirm,
                onDismiss = onDismiss
            )
        }
    )
}
