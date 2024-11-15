package pers.optsimauth.todolist.ui.component.dialog

import CalendarTaskDialog
import FourQuadrantTaskDialog
import androidx.compose.runtime.Composable
import pers.optsimauth.todolist.entity.Task
import java.time.LocalDate


@Composable
fun AddCalendarTaskDialog(
    focusedDate: LocalDate,
    onConfirm: (Task.CalendarTaskEntity) -> Unit,
    onDismiss: () -> Unit,
) {
    val initialTask = Task.CalendarTaskEntity(
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
    onConfirm: (Task.FourQuadrantTaskEntity) -> Unit,
    onDismiss: () -> Unit,
) {
    val initialTask = Task.FourQuadrantTaskEntity(
        id = 0,
        content = "",
        quadrant = focusedQuadrant,
        status = false
    )
    FourQuadrantTaskDialog(initialTask, onConfirm, onDismiss)
}