package pers.optsimauth.todolist.ui.component.dialog

import TaskDialog
import androidx.compose.runtime.Composable
import pers.optsimauth.todolist.entity.CalendarTask
import pers.optsimauth.todolist.entity.FourQuadrantTask
import java.time.LocalDate


@Composable
fun AddTaskDialog(
    focusedDate: LocalDate,
    onConfirm: (CalendarTask) -> Unit,
    onDismiss: () -> Unit,
) {
    val initialTask = CalendarTask(
        id = 0,
        startTime = "00:00",
        endTime = "00:00",
        content = "",
        day = focusedDate.toString(),
        status = false
    )
    TaskDialog(
        initialTask = initialTask,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        isEditMode = false
    )
}

@Composable
fun AddTaskDialog(
    focusedQuadrant: Int,
    onConfirm: (FourQuadrantTask) -> Unit,
    onDismiss: () -> Unit,
) {
    val initialTask = FourQuadrantTask(
        id = 0,
        content = "",
        quadrant = focusedQuadrant,
        status = false
    )
    TaskDialog(
        initialTask = initialTask,
        onConfirm = { onConfirm(it) },
        onDismiss = { onDismiss() })
}