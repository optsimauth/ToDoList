package pers.optsimauth.todolist.ui.component

import EditTaskDialog
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pers.optsimauth.todolist.entity.FourQuadrantTask
import pers.optsimauth.todolist.ui.colorscheme.outlineVariant
import pers.optsimauth.todolist.ui.colorscheme.primary
import pers.optsimauth.todolist.ui.component.dialog.TasksList

@Composable
fun Quadrant(
    title: String,
    tasks: List<FourQuadrantTask>,
    quadrant: Int,
    isFocused: Boolean,
    onQuadrantClick: (Int) -> Unit,
    onTaskChange: (FourQuadrantTask) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedTask by remember { mutableStateOf<FourQuadrantTask?>(null) }

    Box(
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onQuadrantClick(quadrant)
            }
            .border(2.dp, if (isFocused) primary else outlineVariant)
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TasksList(tasks = tasks, onTaskChange = onTaskChange)
        }
    }
    selectedTask?.let { task ->
        EditTaskDialog(
            task = task,
            onConfirm = { onTaskChange(task) },
            onDismiss = { selectedTask = null })
    }

}