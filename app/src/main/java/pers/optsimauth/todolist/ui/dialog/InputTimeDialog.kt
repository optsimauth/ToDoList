package pers.optsimauth.todolist.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pers.optsimauth.todolist.ui.component.WheelPicker


@Composable
fun ScrollableTimePicker(
    hour: Int,
    minute: Int,
    onTimeChanged: (Int, Int) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        WheelPicker(
            data = (0..23).toList(),
            selectIndex = hour,
            visibleCount = 3,
            modifier = Modifier
                .width(60.dp)
                .height(150.dp),
            onSelect = { index, item -> onTimeChanged(item, minute) }
        ) { item ->
            Text(
                text = String.format("%02d", item),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier,
                fontSize = 24.sp
            )
        }

        Text(
            ":",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        WheelPicker(
            data = (0..59).toList(),
            selectIndex = minute,
            visibleCount = 3,
            modifier = Modifier
                .width(60.dp)
                .height(150.dp),
            onSelect = { index, item -> onTimeChanged(hour, item) }
        ) { item ->
            Text(
                text = String.format("%02d", item),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier,
                fontSize = 24.sp
            )
        }
    }
}


@Composable
fun InputTimeDialog(
    initialStartTime: Pair<Int, Int> = Pair(0, 0),
    initialEndTime: Pair<Int, Int> = Pair(0, 0),
    onConfirm: (Pair<Int, Int>, Pair<Int, Int>) -> Unit,
    onDismiss: () -> Unit,
) {
    var startHour by remember { mutableStateOf(initialStartTime.first) }
    var startMinute by remember { mutableStateOf(initialStartTime.second) }
    var endHour by remember { mutableStateOf(initialEndTime.first) }
    var endMinute by remember { mutableStateOf(initialEndTime.second) }

    AlertDialog(
        title = { Text(text = "时间选择器") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "选择开始时间",
                    style = MaterialTheme.typography.titleMedium
                )
                ScrollableTimePicker(
                    hour = startHour,
                    minute = startMinute,
                    onTimeChanged = { h, m -> startHour = h; startMinute = m }
                )


                Text(
                    text = "选择结束时间",
                    style = MaterialTheme.typography.titleMedium
                )
                ScrollableTimePicker(
                    hour = endHour,
                    minute = endMinute,
                    onTimeChanged = { h, m -> endHour = h; endMinute = m }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(Pair(startHour, startMinute), Pair(endHour, endMinute))
            }) {
                Text("确认")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        },
        onDismissRequest = onDismiss
    )
}

