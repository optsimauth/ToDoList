package pers.optsimauth.todolist.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pers.optsimauth.todolist.Utils
import pers.optsimauth.todolist.entity.NoteEntity
import java.time.LocalDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteContentEdit(
    noteEntity: NoteEntity?,
    onDelete: () -> Unit,
    onBack: (NoteEntity) -> Unit
) {
    var title by remember(noteEntity) {
        mutableStateOf(TextFieldValue(noteEntity?.title.orEmpty()))
    }

    var content by remember(noteEntity) {
        mutableStateOf(TextFieldValue(noteEntity?.content.orEmpty()))
    }

    val scrollState = rememberScrollState()

    BackHandler {
        saveAndNavigateBack(noteEntity, title.text, content.text, onBack)
    }

    NoteEditorScaffold(
        noteEntity = noteEntity,
        onBack = { saveAndNavigateBack(noteEntity, title.text, content.text, onBack) },
        onDelete = onDelete
    ) { paddingValues ->
        NoteEditorContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding(),
            title = title,
            onTitleChange = { title = it },
            content = content,
            onContentChange = { content = it },
            isNewNote = noteEntity == null
        )
    }
}

@Composable
private fun NoteEditorScaffold(
    noteEntity: NoteEntity?,
    onBack: () -> Unit,
    onDelete: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .shadow(elevation = 4.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            NoteEditorTopBar(
                showDeleteButton = noteEntity != null,
                onBackClick = onBack,
                onDeleteClick = onDelete
            )
        },
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoteEditorTopBar(
    showDeleteButton: Boolean,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit
) {

    CenterAlignedTopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.primary
        ),
        title = { Text(text = "编辑") },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "返回",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        actions = {
            AnimatedVisibility(
                visible = showDeleteButton,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "删除",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    )
}

@Composable
private fun NoteEditorContent(
    modifier: Modifier = Modifier,
    title: TextFieldValue,
    onTitleChange: (TextFieldValue) -> Unit,
    content: TextFieldValue,
    onContentChange: (TextFieldValue) -> Unit,
    isNewNote: Boolean
) {
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
    ) {
        NoteTextField(
            value = title,
            onValueChange = onTitleChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            textStyle = MaterialTheme.typography.titleLarge,
            placeholder = "标题",
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        NoteTextField(
            value = content,
            onValueChange = onContentChange,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .focusRequester(focusRequester),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                lineHeight = 24.sp
            ),
            placeholder = "在这里写下你的想法...",
            singleLine = false
        )
    }

    LaunchedEffect(isNewNote) {
        if (isNewNote) {
            focusRequester.requestFocus()
        }
    }
}

@Composable
private fun NoteTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle,
    placeholder: String,
    singleLine: Boolean
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor by animateColorAsState(
        targetValue = if (isFocused) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        },
        label = "borderColor"
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = textStyle.copy(color = MaterialTheme.colorScheme.onSurface),
        placeholder = {
            Text(
                text = placeholder,
                style = textStyle,
                color = MaterialTheme.colorScheme.outline
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = borderColor,
            unfocusedBorderColor = borderColor,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        interactionSource = interactionSource,
        singleLine = singleLine
    )
}

private fun saveAndNavigateBack(
    noteEntity: NoteEntity?,
    title: String,
    content: String,
    onBack: (NoteEntity) -> Unit
) {
    onBack(
        NoteEntity(
            id = noteEntity?.id ?: 0,
            title = title,
            content = content,
            updateDateTime = LocalDateTime.now().format(Utils.getMyDatetimeFormatter())
        )
    )
}



