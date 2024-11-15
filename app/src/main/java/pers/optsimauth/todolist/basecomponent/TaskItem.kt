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
    showTaskContent: Boolean = true,
    customContent: @Composable (() -> Unit)? = null,
) {
    // 使用 task.status 作为 key
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
            Row(verticalAlignment = if (showTaskContent) Alignment.CenterVertically else Alignment.Top) {
                Checkbox(
                    checked = task.status, // 直接使用 task.status 而不是本地状态
                    onCheckedChange = { isChecked ->
                        val taskTmp = when (task) {
                            is Task.CalendarTaskEntity -> task.copy(status = isChecked) as T
                            is Task.FourQuadrantTaskEntity -> task.copy(status = isChecked) as T
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
                    modifier = Modifier.padding(top = if (showTaskContent) 0.dp else 7.dp),
                    textDecoration = if (task.status) TextDecoration.LineThrough else TextDecoration.None
                )
            }
            if (showTaskContent) {
                Text(
                    text = task.content,
                    color = onSurface,
                    modifier = Modifier.padding(start = 11.dp),
                    textDecoration = if (task.status) TextDecoration.LineThrough else TextDecoration.None
                )
            }
            customContent?.invoke()
        }
    }
}

@Composable
fun CalendarTaskItem(
    task: Task.CalendarTaskEntity,
    onTaskClick: (Task.CalendarTaskEntity) -> Unit,
    onTaskStatusChange: (Task.CalendarTaskEntity) -> Unit,
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
    task: Task.FourQuadrantTaskEntity,
    onTaskClick: (Task.FourQuadrantTaskEntity) -> Unit,
    onTaskStatusChange: (Task.FourQuadrantTaskEntity) -> Unit,
) {
    TaskItem(
        task = task,
        onTaskClick = onTaskClick,
        onTaskStatusChange = onTaskStatusChange,
        getColor = { Utils.getColorOfQuadrant(it.quadrant) },
        getText = { it.content },
        showTaskContent = false  // 不显示额外的内容文本
    )
}

