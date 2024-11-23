package pers.optsimauth.todolist.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pers.optsimauth.todolist.Utils
import pers.optsimauth.todolist.entity.NoteEntity
import java.time.LocalDateTime


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NoteContentEdit(
    noteEntity: NoteEntity?,
    onDelete: () -> Unit,
    onBack: (NoteEntity) -> Unit
) {
    val noteState by remember {
        mutableStateOf(
            NoteState.Edit(
                titleState = TextFieldState(initialText = noteEntity?.title ?: ""),
                contentState = TextFieldState(initialText = noteEntity?.content ?: "")
            )
        )
    }
    val lazyListState = rememberLazyListState()

    BackHandler {
        onBack(
            NoteEntity(
                id = noteEntity?.id ?: 0,
                title = noteState.titleState.text.toString(),
                content = noteState.contentState.text.toString(),
                updateDateTime = LocalDateTime.now().format(Utils.getMyDatetimeFormatter())
            )
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .shadow(4.dp, clip = false),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 2.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.primary
                ),
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBack(
                                NoteEntity(
                                    id = noteEntity?.id ?: 0,
                                    title = noteState.titleState.text.toString(),
                                    content = noteState.contentState.text.toString(),
                                    updateDateTime = LocalDateTime.now()
                                        .format(Utils.getMyDatetimeFormatter())
                                )
                            )
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    if (noteEntity != null) {
                        IconButton(onClick = onDelete) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "删除",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        MainBody(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .imePadding(),
            state = noteState,
            blockColumnState = lazyListState,
            noteEntity = noteEntity
        )
    }
}

@Composable
fun StyledTextField(
    textFieldState: TextFieldState,
    modifier: Modifier = Modifier,
    decorator: TextFieldDecorator,
    textStyle: TextStyle,
    minHeight: Dp = 56.dp,
    focusRequester: FocusRequester = remember { FocusRequester() },
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val isFocused by interactionSource.collectIsFocusedAsState()
    val borderColor by animateColorAsState(
        if (isFocused) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
        label = "borderColor"
    )

    Box(
        modifier = modifier
            .heightIn(min = minHeight)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        BasicTextField(
            state = textFieldState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .focusRequester(focusRequester),
            textStyle = textStyle,
            interactionSource = interactionSource,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorator = decorator
        )
    }
}

@Composable
fun MainBody(
    modifier: Modifier = Modifier,
    state: NoteState.Edit,
    blockColumnState: LazyListState = rememberLazyListState(),
    noteEntity: NoteEntity?
) {
    val focusRequester = remember { FocusRequester() }
    val titleInteractionSource = remember { MutableInteractionSource() }
    val contentInteractionSource = remember { MutableInteractionSource() }

    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            state = blockColumnState,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                StyledTextField(
                    textFieldState = state.titleState,
                    decorator = {
                        if (state.titleState.text.isEmpty()) {
                            Text(
                                text = "Title",
                                color = MaterialTheme.colorScheme.outline,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        it()
                    },
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    interactionSource = titleInteractionSource,
                )
            }

            item {
                StyledTextField(
                    textFieldState = state.contentState,

                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 24.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    interactionSource = contentInteractionSource,
                    focusRequester = focusRequester,
                    decorator = {
                        if (state.contentState.text.isEmpty()) {
                            Text(
                                text = "Write down your opinions here",
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                        it()
                    }
                )
            }
        }

        LaunchedEffect(Unit) {
            if (noteEntity == null) {
                focusRequester.requestFocus()
            }
        }
    }
}

sealed class NoteState {
    data class Edit(
        val titleState: TextFieldState,
        val contentState: TextFieldState
    ) : NoteState()
}






