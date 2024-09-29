package pers.optsimauth.todolist.ui.component.dialog

import CalendarTaskDialog
import FourQuadrantTaskDialog
import androidx.compose.runtime.Composable
import pers.optsimauth.todolist.entity.Task
import java.time.LocalDate


@Composable
fun AddCalendarTaskDialog(
    focusedDate: LocalDate,
    onConfirm: (Task.CalendarTask) -> Unit,
    onDismiss: () -> Unit,
) {
    val initialTask = Task.CalendarTask(
        id = 0,
        startTime = "00:00",
        endTime = "00:00",
        content = "",
        day = focusedDate.toString(),
        status = false
    )
    CalendarTaskDialog(initialTask, onConfirm, onDismiss)
}


@Composable
fun AddFourQuadrantTaskDialog(
    focusedQuadrant: Int,
    onConfirm: (Task.FourQuadrantTask) -> Unit,
    onDismiss: () -> Unit,
) {
    val initialTask = Task.FourQuadrantTask(
        id = 0,
        content = "",
        quadrant = focusedQuadrant,
        status = false
    )
    FourQuadrantTaskDialog(initialTask, onConfirm, onDismiss)
}