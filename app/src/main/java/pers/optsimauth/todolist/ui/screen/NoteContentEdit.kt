package pers.optsimauth.todolist.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import pers.optsimauth.todolist.Utils
import pers.optsimauth.todolist.config.colorscheme.onSurface
import pers.optsimauth.todolist.config.colorscheme.primary
import pers.optsimauth.todolist.entity.NoteEntity
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteContentEdit(
    noteEntity: NoteEntity?,
    onSave: (NoteEntity) -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit
) {
    // 保存编辑历史用于撤销功能
    val editHistory = remember { mutableStateListOf(TextFieldValue(noteEntity?.content ?: "")) }
    var currentHistoryIndex by remember { mutableIntStateOf(0) }

    var textState by remember { mutableStateOf(TextFieldValue(noteEntity?.content ?: "")) }
    var titleState by remember { mutableStateOf(TextFieldValue(noteEntity?.title ?: "")) }


    // 用于控制键盘显示
//    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current


    // 撤销功能
    fun undo() {
        if (currentHistoryIndex > 0) {
            currentHistoryIndex--
            textState = editHistory[currentHistoryIndex]
        }
    }

    // 重做功能
    fun redo() {
        if (currentHistoryIndex < editHistory.size - 1) {
            currentHistoryIndex++
            textState = editHistory[currentHistoryIndex]
        }
    }

    BackHandler {
        onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    // 撤销按钮
                    IconButton(
                        onClick = { undo() },
                        enabled = currentHistoryIndex > 0
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Undo,
                            contentDescription = "撤销",
                            tint = if (currentHistoryIndex > 0)
                                primary
                            else onSurface.copy(alpha = 0.38f)
                        )
                    }

                    // 重做按钮
                    IconButton(onClick = { redo() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.Redo,
                            contentDescription = "重做",
                            tint = if (currentHistoryIndex < editHistory.size - 1)
                                primary
                            else onSurface.copy(alpha = 0.38f)
                        )
                    }


                    //删除按钮
                    if (noteEntity != null) {
                        IconButton(onClick = onDelete) {
                            Icon(
                                Icons.Default.Delete, contentDescription = "删除", tint = Color.Red
                            )

                        }
                    }

                    // 保存按钮
                    IconButton(
                        onClick = {
                            focusManager.clearFocus()
                            onSave(
                                NoteEntity(
                                    id = noteEntity?.id ?: 0,
                                    title = titleState.text,
                                    content = textState.text,
                                    updateDateTime = LocalDateTime.now()
                                        .format(Utils.getMyDatetimeFormatter())
                                )
                            )
                        }
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "完成")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column {
                BasicTextField(
                    value = titleState,
                    onValueChange = { newValue ->
                        //判断是否通过redo或undo操作修改文本而不是用户输入
                        titleState = newValue
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
//                        .focusRequester(focusRequester)
                    ,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    decorationBox = { innerTextField ->
                        Box {
                            innerTextField()
                            if (titleState.text.isEmpty()) {
                                Text(
                                    text = "标题",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                )
                BasicTextField(
                    value = textState,
                    onValueChange = { newValue ->

                        //判断是否通过redo或undo操作修改文本而不是用户输入
                        textState = newValue

                        if (newValue != editHistory.getOrNull(
                                currentHistoryIndex
                            )
                        ) {
                            currentHistoryIndex++
                            editHistory.add(currentHistoryIndex, newValue)
                            // 移除当前位置之后的历史记录
                            while (editHistory.size - 1 > currentHistoryIndex) {
                                editHistory.removeAt(editHistory.size - 1)
                            }
                        }


                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
//                        .focusRequester(focusRequester)
                    ,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    decorationBox = { innerTextField ->
                        Box {
                            innerTextField()
                            if (textState.text.isEmpty()) {
                                Text(
                                    text = "输入内容",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                )

            }
        }
    }

    LaunchedEffect(Unit) {
//        if (noteEntity == null) {
//            focusRequester.requestFocus()
//        }
    }
}