package pers.optsimauth.todolist.ui.component

import EditFourQuadrantTaskDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pers.optsimauth.todolist.Utils
import pers.optsimauth.todolist.config.colorscheme.outlineVariant
import pers.optsimauth.todolist.config.colorscheme.primary
import pers.optsimauth.todolist.config.colorscheme.surface
import pers.optsimauth.todolist.config.colorscheme.surfaceBright
import pers.optsimauth.todolist.config.font.romanNumerals
import pers.optsimauth.todolist.entity.Task

@Composable
fun Quadrant(
    quadrantTitle: String,
    tasks: List<Task.FourQuadrantTaskEntity>,
    quadrant: Int,
    isFocused: Boolean,
    onQuadrantClick: (Int) -> Unit,
    onTaskChange: (Task.FourQuadrantTaskEntity) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedTask by remember { mutableStateOf<Task.FourQuadrantTaskEntity?>(null) }


    Box(
        modifier = modifier
            .padding(8.dp)
            .background(surface, RoundedCornerShape(8.dp))
            .fillMaxSize()
            .clickable {
                onQuadrantClick(quadrant)
            }
            .border(3.dp, if (isFocused) primary else outlineVariant, RoundedCornerShape(8.dp))
    ) {
        Column {
            QuadrantTitle(
                title = quadrantTitle,
                quadrant = quadrant,
                titleColor = Utils.getColorOfQuadrant(quadrant)
            )
            FourQuadrantTaskList(tasks, onTaskChange = onTaskChange)
        }
    }
    selectedTask?.let { task ->
        EditFourQuadrantTaskDialog(
            initialTask = task,
            onConfirm = { onTaskChange(task) },
            onDismiss = { selectedTask = null })
    }

}

@Composable
fun QuadrantTitle(
    title: String,
    quadrant: Int,
    titleColor: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(top = 8.dp, start = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    Utils.getColorOfQuadrant(quadrant),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = romanNumerals[quadrant - 1],
                color = surfaceBright,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            color = titleColor,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}