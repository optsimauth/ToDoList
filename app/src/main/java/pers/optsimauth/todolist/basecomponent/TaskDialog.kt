import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import pers.optsimauth.todolist.entity.Task
import pers.optsimauth.todolist.ui.component.dialog.InputTimeDialog

@Composable
fun <T : Task> TaskDialog(
    initialTask: T,
    onConfirm: (T) -> Unit,
    onDismiss: () -> Unit,
    isEditMode: Boolean = false,
    customContent: @Composable (T, (T) -> Unit) -> Unit,
) {
    var task by remember { mutableStateOf(initialTask) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (isEditMode) "编辑任务" else "添加任务") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                customContent(task) { task = it }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(task) }) {
                Text(if (isEditMode) "确认" else "添加")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

@Composable
fun CalendarTaskDialog(
    initialTask: Task.CalendarTask,
    onConfirm: (Task.CalendarTask) -> Unit,
    onDismiss: () -> Unit,
    isEditMode: Boolean = false,
) {
    TaskDialog(
        initialTask = initialTask,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        isEditMode = isEditMode
    ) { task, onTaskChange ->
        var showInputTimeDialog by remember { mutableStateOf(false) }

        Text(
            text = "${task.startTime} ~ ${task.endTime}",
            modifier = Modifier
                .clickable { showInputTimeDialog = true }
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )

        OutlinedTextField(
            value = task.content,
            onValueChange = { onTaskChange(task.copy(content = it)) },
            label = { Text("任务内容") },
            modifier = Modifier.fillMaxWidth()
        )

        if (showInputTimeDialog) {
            InputTimeDialog(
                initialStartTime = task.startTime.split(":")
                    .let { Pair(it[0].toInt(), it[1].toInt()) },
                initialEndTime = task.endTime.split(":").let { Pair(it[0].toInt(), it[1].toInt()) },
                onConfirm = { startTime, endTime ->
                    onTaskChange(
                        task.copy(
                            startTime = String.format(
                                "%02d:%02d",
                                startTime.first,
                                startTime.second
                            ),
                            endTime = String.format("%02d:%02d", endTime.first, endTime.second)
                        )
                    )
                    showInputTimeDialog = false
                },
                onDismiss = { showInputTimeDialog = false }
            )
        }
    }
}

@Composable
fun FourQuadrantTaskDialog(
    initialTask: Task.FourQuadrantTask,
    onConfirm: (Task.FourQuadrantTask) -> Unit,
    onDismiss: () -> Unit,
    isEditMode: Boolean = false,
) {
    TaskDialog(
        initialTask = initialTask,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        isEditMode = isEditMode
    ) { task, onTaskChange ->
        OutlinedTextField(
            value = task.content,
            onValueChange = { onTaskChange(task.copy(content = it)) },
            label = { Text("任务内容") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}