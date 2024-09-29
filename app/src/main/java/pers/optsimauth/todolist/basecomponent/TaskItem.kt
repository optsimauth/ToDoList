package pers.optsimauth.todolist.basecomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import pers.optsimauth.todolist.Utils
import pers.optsimauth.todolist.entity.CalendarTask
import pers.optsimauth.todolist.entity.FourQuadrantTask
import pers.optsimauth.todolist.ui.colorscheme.onSurface
import pers.optsimauth.todolist.ui.colorscheme.surface

@Composable
fun TaskItem(
    task: CalendarTask,
    onTaskClick: (CalendarTask) -> Unit,
    onTaskStatusChange: (CalendarTask) -> Unit,
) {
    var checkStatus by remember { mutableStateOf(task.status) } // 这里是每个复选框的状态
    val checkBoxColor = Utils.getColorOfCalendarTask(task)
    val text = when {
        task.startTime == "00:00" && task.endTime == "00:00" -> "今日完成任务"
        task.startTime == "00:00" -> "deadline: ${task.endTime}"
        task.endTime == "00:00" -> "开始时间: ${task.startTime}"
        else -> "${task.startTime} ~ ${task.endTime}"
    }

    Card(
        modifier = Modifier
            .background(surface)
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onTaskClick(task) }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = checkStatus,
                    onCheckedChange = { isChecked ->
                        checkStatus = isChecked
                        val taskTmp = task.copy(status = isChecked)
                        onTaskStatusChange(taskTmp)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = checkBoxColor,
                        uncheckedColor = checkBoxColor
                    )
                )
                Text(
                    text = text,
                    color = onSurface
                )
            }
            Text(
                text = task.content,
                textDecoration = if (checkStatus) TextDecoration.LineThrough else TextDecoration.None
            )
        }
    }
}

@Composable
fun TaskItem(
    task: FourQuadrantTask,
    onTaskClick: (FourQuadrantTask) -> Unit,
    onTaskStatusChange: (FourQuadrantTask) -> Unit,
) {
    var checkStatus by remember { mutableStateOf(task.status) } // 这里是每个复选框的状态
    val checkBoxColor = Utils.getColorOfFourQuadrantTask(task)


    Card(
        modifier = Modifier
            .background(surface)
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onTaskClick(task) }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = checkStatus,
                    onCheckedChange = { isChecked ->
                        checkStatus = isChecked
                        val taskTmp = task.copy(status = isChecked)
                        onTaskStatusChange(taskTmp)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = checkBoxColor,
                        uncheckedColor = checkBoxColor
                    )
                )
                Text(
                    text = task.content,
                    color = onSurface
                )
            }
            Text(
                text = task.content,
                textDecoration = if (checkStatus) TextDecoration.LineThrough else TextDecoration.None
            )
        }
    }
}