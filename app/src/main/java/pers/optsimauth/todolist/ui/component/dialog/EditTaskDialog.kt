import androidx.compose.runtime.Composable
import pers.optsimauth.todolist.entity.Task


@Composable
fun EditCalendarTaskDialog(
    initialTask: Task.CalendarTaskEntity,
    onConfirm: (Task.CalendarTaskEntity) -> Unit,
    onDismiss: () -> Unit,
) {

    CalendarTaskDialog(initialTask, onConfirm, onDismiss, isEditMode = true)
}


@Composable
fun EditFourQuadrantTaskDialog(
    initialTask: Task.FourQuadrantTaskEntity,
    onConfirm: (Task.FourQuadrantTaskEntity) -> Unit,
    onDismiss: () -> Unit,
) {

    FourQuadrantTaskDialog(initialTask, onConfirm, onDismiss, isEditMode = true)
}
