import androidx.compose.runtime.Composable
import pers.optsimauth.todolist.entity.CalendarTask
import pers.optsimauth.todolist.entity.FourQuadrantTask

@Composable
fun EditTaskDialog(
    task: CalendarTask,
    onConfirm: (CalendarTask) -> Unit,
    onDismiss: () -> Unit,
) {
    TaskDialog(
        initialTask = task,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        isEditMode = true
    )
}

@Composable
fun EditTaskDialog(
    task: FourQuadrantTask,
    onConfirm: (FourQuadrantTask) -> Unit,
    onDismiss: () -> Unit,
) {
    TaskDialog(
        initialTask = task,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        isEditMode = true
    )
}