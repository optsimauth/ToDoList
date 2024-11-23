package pers.optsimauth.todolist.ui.screen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pers.optsimauth.todolist.ui.component.Quadrant
import pers.optsimauth.todolist.viewmodel.FourQuadrantTaskViewModel

@Composable
fun FourQuadrant(
    onQuadrantFocused: (quadrant: Int) -> Unit,
    fourQuadrantTaskViewModel: FourQuadrantTaskViewModel,
) {
    var focusedQuadrant by remember { mutableIntStateOf(1) }

    //onQuadrantFocused是为了让FloatingActionButton知道当前是哪个象限被选中，用于添加任务
    //focusedQuadrant是为了让FourQuadrant组件知道当前是哪个象限被选中，用于高亮显示


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "四象限",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(8.dp)
        )


        Row(modifier = Modifier.weight(1f)) {
            Quadrant(
                quadrantTitle = "重要且紧急",
                tasks = fourQuadrantTaskViewModel.getTasksByQuadrant(1),
                quadrant = 1,
                isFocused = focusedQuadrant == 1,
                onQuadrantClick = { quadrant ->
                    focusedQuadrant = quadrant
                    onQuadrantFocused(quadrant)
                },
                onTaskChange = {
                    //判断是编辑任务内容还是复选框修改任务状态
                    fourQuadrantTaskViewModel.update(it)
                },
                modifier = Modifier.weight(1f)
            )
            Quadrant(
                quadrantTitle = "重要不紧急",
                tasks = fourQuadrantTaskViewModel.getTasksByQuadrant(2),
                quadrant = 2,
                isFocused = focusedQuadrant == 2,
                onQuadrantClick = { quadrant ->
                    focusedQuadrant = quadrant
                    onQuadrantFocused(quadrant)
                },
                onTaskChange = { fourQuadrantTaskViewModel.update(it) },
                modifier = Modifier.weight(1f)
            )
        }
        Row(modifier = Modifier.weight(1f)) {
            Quadrant(
                quadrantTitle = "不重要但紧急",
                tasks = fourQuadrantTaskViewModel.getTasksByQuadrant(3),
                quadrant = 3,
                isFocused = focusedQuadrant == 3,
                onQuadrantClick = { quadrant ->
                    focusedQuadrant = quadrant
                    onQuadrantFocused(quadrant)
                },
                onTaskChange = { fourQuadrantTaskViewModel.update(it) },
                modifier = Modifier.weight(1f)
            )
            Quadrant(
                quadrantTitle = "不重要不紧急",
                tasks = fourQuadrantTaskViewModel.getTasksByQuadrant(4),
                quadrant = 4,
                isFocused = focusedQuadrant == 4,
                onQuadrantClick = { quadrant ->
                    focusedQuadrant = quadrant
                    onQuadrantFocused(quadrant)
                },
                onTaskChange = { fourQuadrantTaskViewModel.update(it) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}