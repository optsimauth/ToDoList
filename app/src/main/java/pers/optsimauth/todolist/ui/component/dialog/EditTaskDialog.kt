import androidx.compose.runtime.Composable
import pers.optsimauth.todolist.entity.Task


@Composable
fun EditCalendarTaskDialog(
    initialTask: Task.CalendarTask,
    onConfirm: (Task.CalendarTask) -> Unit,
    onDismiss: () -> Unit,
) {

    CalendarTaskDialog(initialTask, onConfirm, onDismiss, isEditMode = true)
}


@Composable
fun EditFourQuadrantTaskDialog(
    initialTask: Task.FourQuadrantTask,
    onConfirm: (Task.FourQuadrantTask) -> Unit,
    onDismiss: () -> Unit,
) {

    FourQuadrantTaskDialog(initialTask, onConfirm, onDismiss, isEditMode = true)
}
