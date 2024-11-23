package pers.optsimauth.todolist.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pers.optsimauth.todolist.Utils
import pers.optsimauth.todolist.entity.NoteEntity
import pers.optsimauth.todolist.viewmodel.NoteViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Note(
    noteViewModel: NoteViewModel,
    onNoteItemClick: (NoteEntity) -> Unit,
    onNoteItemDelete: (NoteEntity) -> Unit,
) {
    val notes = noteViewModel.getAllNotes().collectAsState(initial = emptyList()).value

    // 创建滚动行为
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "笔记",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalItemSpacing = 16.dp,
            // 设置内容填充
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 20.dp // 为底部导航栏预留空间
            ),
            // 设置过度滚动模式
            flingBehavior = ScrollableDefaults.flingBehavior(),
            state = rememberLazyStaggeredGridState()
        ) {
            items(notes, key = { it.id }) { note ->
                NoteCard(
                    noteEntity = note,
                    onNoteItemClick = onNoteItemClick,
                    onNoteItemDelete = onNoteItemDelete
                )
            }
        }
    }

}

@Composable
fun NoteCard(
    noteEntity: NoteEntity,
    onNoteItemClick: (NoteEntity) -> Unit,
    onNoteItemDelete: (NoteEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onNoteItemClick(noteEntity) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 标题部分
            val title = noteEntity.title.ifBlank {
                noteEntity.content.split("\n").firstOrNull() ?: ""
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )


            // 内容部分
            val content = noteEntity.content
                .substringAfter(title)
                .trimStart()
                .takeIf { it.isNotBlank() }
                ?: "无内容"

            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis
            )


            // 显示时间
            //判断updateTime是否为今天
            val updateDate =
                LocalDate.parse(
                    noteEntity.updateDateTime,
                    Utils.getMyDatetimeFormatter()
                )
            val currentDate = LocalDate.now()

            val updateTime = noteEntity.updateDateTime.substring(11, 16)

            Text(
                text = when (updateDate) {
                    currentDate -> "今天$updateTime"
                    currentDate.minusDays(1) -> "昨天$updateTime"
                    currentDate.minusDays(2) -> "前天$updateTime"
                    else -> updateDate.format(Utils.getMyDateFormatter())
                },
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 8.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }


}