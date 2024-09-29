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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import pers.optsimauth.todolist.Utils
import pers.optsimauth.todolist.config.colorscheme.onSurface
import pers.optsimauth.todolist.config.colorscheme.surface
import pers.optsimauth.todolist.entity.Task


@Composable
fun <T : Task> TaskItem(
    task: T,
    onTaskClick: (T) -> Unit,
    onTaskStatusChange: (T) -> Unit,
    getColor: (T) -> Color,
    getText: (T) -> String,
    showContent: Boolean = true,
    customContent: @Composable (() -> Unit)? = null,
) {
    var checkStatus by remember { mutableStateOf(task.status) }
    val checkBoxColor = getColor(task)
    val text = getText(task)
    Card(
        modifier = Modifier
            .background(surface)
            .padding(4.dp)
            .fillMaxWidth()
            .clickable { onTaskClick(task) }
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            Row(verticalAlignment = if (showContent) Alignment.CenterVertically else Alignment.Top) {
                Checkbox(
                    checked = checkStatus,
                    onCheckedChange = { isChecked ->
                        checkStatus = isChecked
                        val taskTmp = when (task) {
                            is Task.CalendarTask -> task.copy(status = isChecked) as T
                            is Task.FourQuadrantTask -> task.copy(status = isChecked) as T
                            else -> {
                                throw IllegalArgumentException("Unsupported task type")
                            }
                        }
                        onTaskStatusChange(taskTmp)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = checkBoxColor,
                        uncheckedColor = checkBoxColor
                    )
                )
                Text(
                    text = text,
                    color = onSurface,
                    modifier = Modifier.padding(top = if (showContent) 0.dp else 7.dp),
                    textDecoration = if (checkStatus) TextDecoration.LineThrough else TextDecoration.None
                )
            }
            if (showContent) {
                Text(
                    text = task.content,
                    color = onSurface,
                    modifier = Modifier.padding(start = 11.dp),
                    textDecoration = if (checkStatus) TextDecoration.LineThrough else TextDecoration.None
                )
            }
            customContent?.invoke()
        }
    }
}

@Composable
fun CalendarTaskItem(
    task: Task.CalendarTask,
    onTaskClick: (Task.CalendarTask) -> Unit,
    onTaskStatusChange: (Task.CalendarTask) -> Unit,
) {
    TaskItem(
        task = task,
        onTaskClick = onTaskClick,
        onTaskStatusChange = onTaskStatusChange,
        getColor = { Utils.getColorOfCalendarTask(it) },
        getText = {
            when {
                it.startTime == "00:00" && it.endTime == "00:00" -> "今日完成任务"
                it.startTime == "00:00" -> "deadline: ${it.endTime}"
                it.endTime == "00:00" -> "开始时间: ${it.startTime}"
                else -> "${it.startTime} ~ ${it.endTime}"
            }
        }
    )
}

@Composable
fun FourQuadrantTaskItem(
    task: Task.FourQuadrantTask,
    onTaskClick: (Task.FourQuadrantTask) -> Unit,
    onTaskStatusChange: (Task.FourQuadrantTask) -> Unit,
) {
    TaskItem(
        task = task,
        onTaskClick = onTaskClick,
        onTaskStatusChange = onTaskStatusChange,
        getColor = { Utils.getColorOfQuadrant(it.quadrant) },
        getText = { it.content },
        showContent = false  // 不显示额外的内容文本
    )
}

