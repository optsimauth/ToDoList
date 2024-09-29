import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties


@Composable
fun BottomSheetInput(
) {
    var isBottomSheetInputVisible by remember { mutableStateOf(false) }

    Button(onClick = { isBottomSheetInputVisible = true }) {
        Text("显示输入框")

    }

    BottomSheetInputDialog(
        isVisible = isBottomSheetInputVisible,
        onDismiss = { isBottomSheetInputVisible = false },
        onTextSubmit = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetInputDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onTextSubmit: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    if (isVisible) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false  // 更改为 false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding(),  // 添加 imePadding
                contentAlignment = Alignment.BottomCenter
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                                .onFocusChanged { focusState ->
                                    if (focusState.isFocused) {
                                        // 这里可以添加额外的处理，例如记录输入框获取焦点时的状态
                                    }
                                },
                            label = { Text("输入文本") }
                        )

                        Button(
                            onClick = {
                                onTextSubmit(text)
                                onDismiss()
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("提交")
                        }
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}
