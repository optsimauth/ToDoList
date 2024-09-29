import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import pers.optsimauth.todolist.entity.CalendarTask
import pers.optsimauth.todolist.entity.FourQuadrantTask
import pers.optsimauth.todolist.ui.component.dialog.InputTimeDialog

@Composable
fun TaskDialog(
    initialTask: CalendarTask,
    onConfirm: (CalendarTask) -> Unit,
    onDismiss: () -> Unit,
    isEditMode: Boolean = false,
) {
    var taskContent by remember { mutableStateOf(initialTask.content) }
    var taskStartTime by remember { mutableStateOf(initialTask.startTime) }
    var taskEndTime by remember { mutableStateOf(initialTask.endTime) }
    var showInputTimeDialog by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (isEditMode) "编辑任务" else "添加任务") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    text = "$taskStartTime ~ $taskEndTime",
                    modifier = Modifier
                        .clickable { showInputTimeDialog = true }
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp
                )

                OutlinedTextField(
                    value = taskContent,
                    onValueChange = { taskContent = it },
                    label = { Text("任务内容") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val updatedTask = initialTask.copy(
                    content = taskContent,
                    startTime = taskStartTime,
                    endTime = taskEndTime
                )
                onConfirm(updatedTask)
            }) {
                Text(if (isEditMode) "确认" else "添加")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )

    if (showInputTimeDialog) {
        InputTimeDialog(
            initialStartTime = taskStartTime.split(":").let { Pair(it[0].toInt(), it[1].toInt()) },
            initialEndTime = taskEndTime.split(":").let { Pair(it[0].toInt(), it[1].toInt()) },
            onConfirm = { startTime, endTime ->
                taskStartTime = String.format("%02d:%02d", startTime.first, startTime.second)
                taskEndTime = String.format("%02d:%02d", endTime.first, endTime.second)
                showInputTimeDialog = false
            },
            onDismiss = { showInputTimeDialog = false }
        )
    }
}

@Composable
fun TaskDialog(
    initialTask: FourQuadrantTask,
    onConfirm: (FourQuadrantTask) -> Unit,
    onDismiss: () -> Unit,
    isEditMode: Boolean = false,
) {
    var taskContent by remember { mutableStateOf(initialTask.content) }
    val focusRequester = remember { FocusRequester() }


    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (isEditMode) "编辑任务" else "添加任务") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                OutlinedTextField(
                    value = taskContent,
                    onValueChange = { taskContent = it },
                    label = { Text("任务内容") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val updatedTask = initialTask.copy(
                    content = taskContent,
                )
                onConfirm(updatedTask)
            }) {
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