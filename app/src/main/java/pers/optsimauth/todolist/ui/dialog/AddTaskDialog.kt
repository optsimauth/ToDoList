package pers.optsimauth.todolist.ui.dialog

import TaskDialog
import androidx.compose.runtime.Composable
import pers.optsimauth.todolist.entity.CalendarTask
import java.time.LocalDate


@Composable
fun AddTaskDialog(
    selectedDate: LocalDate,
    onConfirm: (CalendarTask) -> Unit,
    onDismiss: () -> Unit,
) {
    val initialTask = CalendarTask(
        id = 0,
        startTime = "00:00",
        endTime = "00:00",
        content = "",
        day = selectedDate.toString(),
        status = false
    )
    TaskDialog(
        initialTask = initialTask,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        isEditMode = false
    )
}