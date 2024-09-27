import androidx.compose.runtime.Composable
import pers.optsimauth.todolist.entity.CalendarTask

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