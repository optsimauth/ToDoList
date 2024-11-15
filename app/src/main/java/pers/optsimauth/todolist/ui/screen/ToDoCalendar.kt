package pers.optsimauth.todolist.ui.screen

import MultiColorCircle
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarViewWeek
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.daysOfWeek
import pers.optsimauth.todolist.Utils
import pers.optsimauth.todolist.config.colorscheme.onSurface
import pers.optsimauth.todolist.config.colorscheme.outlineVariant
import pers.optsimauth.todolist.config.colorscheme.primary
import pers.optsimauth.todolist.config.colorscheme.primaryContainer
import pers.optsimauth.todolist.config.colorscheme.scrim
import pers.optsimauth.todolist.config.colorscheme.secondary
import pers.optsimauth.todolist.config.colorscheme.surface
import pers.optsimauth.todolist.entity.Task
import pers.optsimauth.todolist.ui.component.CalendarTasksList
import pers.optsimauth.todolist.viewmodel.CalendarTaskViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale


@Composable
fun ToDoCalendar(
    onDateFocused: (LocalDate) -> Unit,
    calendarTaskViewModel: CalendarTaskViewModel,
) {
    var isMonthView by remember { mutableStateOf(true) }
    var focusedDate by remember { mutableStateOf(LocalDate.now()) }

    Column(modifier = Modifier.fillMaxSize()) {
        CalendarHeader(
            isMonthView = isMonthView,
            onViewTypeChange = { isMonthView = it }
        )

        AnimatedCalendarContent(
            isMonthView = isMonthView,
            focusedDate = focusedDate,
            calendarTaskViewModel = calendarTaskViewModel,
            onDateFocused = { date ->
                onDateFocused(date)
                focusedDate = date
            }
        )
    }
}

@Composable
private fun CalendarHeader(
    isMonthView: Boolean,
    onViewTypeChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (isMonthView) "月视图" else "周视图",
            style = MaterialTheme.typography.titleMedium
        )

        Row {
            CalendarViewButton(
                isSelected = !isMonthView,
                icon = Icons.Default.CalendarViewWeek,
                onClick = { onViewTypeChange(false) }
            )
            CalendarViewButton(
                isSelected = isMonthView,
                icon = Icons.Default.CalendarMonth,
                onClick = { onViewTypeChange(true) }
            )
        }
    }
}

@Composable
private fun CalendarViewButton(
    isSelected: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick) {
        Icon(
            icon,
            contentDescription = null,
            tint = if (isSelected) primary else outlineVariant
        )
    }
}

@Composable
private fun AnimatedCalendarContent(
    isMonthView: Boolean,
    focusedDate: LocalDate,
    calendarTaskViewModel: CalendarTaskViewModel,
    onDateFocused: (LocalDate) -> Unit,
) {
    AnimatedContent(
        targetState = isMonthView,
        transitionSpec = {
            val slideDirection = if (targetState) 1 else -1
            slideInVertically { height -> height * slideDirection } + fadeIn() togetherWith
                    slideOutVertically { height -> -height * slideDirection } + fadeOut()
        }
    ) { targetIsMonthView ->
        if (targetIsMonthView) {
            MonthCalendarView(
                focusedDate = focusedDate,
                calendarTaskViewModel = calendarTaskViewModel,
                onDateFocused = onDateFocused
            )
        } else {
            WeekCalendarView(
                focusedDate = focusedDate,
                calendarTaskViewModel = calendarTaskViewModel,
                onDateFocused = onDateFocused
            )
        }
    }
}

@Composable
private fun MonthCalendarView(
    focusedDate: LocalDate,
    calendarTaskViewModel: CalendarTaskViewModel,
    onDateFocused: (LocalDate) -> Unit,
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(50) }
    val daysOfWeek = remember { daysOfWeek() }
    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first(),
        outDateStyle = OutDateStyle.EndOfGrid
    )

    CalendarContainer {
        HorizontalCalendar(
            state = state,
            dayContent = { day ->
                DayContent(
                    date = day.date,
                    position = day.position,
                    calendarTaskViewModel = calendarTaskViewModel,
                    focusedDate = focusedDate,
                    onDateFocused = onDateFocused
                )
            },
            monthHeader = { month ->
                MonthHeader(month = month, daysOfWeek = daysOfWeek)
            }
        )

        CalendarTasksList(
            focusedDate = focusedDate,
            calendarTaskViewModel = calendarTaskViewModel
        )
    }
}

@Composable
private fun WeekCalendarView(
    focusedDate: LocalDate,
    calendarTaskViewModel: CalendarTaskViewModel,
    onDateFocused: (LocalDate) -> Unit,
) {
    val currentDate = LocalDate.now()
    val state = rememberWeekCalendarState(
        startDate = currentDate.minusDays(400),
        endDate = currentDate.plusDays(200),
        firstVisibleWeekDate = currentDate
    )
    val daysOfWeek = remember { daysOfWeek() }

    CalendarContainer {
        Text(
            text = focusedDate.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")),
            modifier = Modifier.padding(8.dp),
            textAlign = TextAlign.Center,
            color = onSurface
        )

        Box(
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, color = secondary, shape = RoundedCornerShape(8.dp))
        ) {
            WeekCalendar(
                state = state,
                dayContent = { day ->
                    DayContent(
                        date = day.date,
                        position = DayPosition.MonthDate,
                        calendarTaskViewModel = calendarTaskViewModel,
                        focusedDate = focusedDate,
                        onDateFocused = onDateFocused
                    )
                },
                weekHeader = { week ->
                    WeekHeader(week = week, daysOfWeek = daysOfWeek)
                }
            )
        }

        CalendarTasksList(
            focusedDate = focusedDate,
            calendarTaskViewModel = calendarTaskViewModel
        )
    }
}

@Composable
private fun CalendarContainer(content: @Composable () -> Unit) {
    Column {
        content()
    }
}

@Composable
private fun MonthHeader(month: CalendarMonth, daysOfWeek: List<DayOfWeek>) {
    Column {
        Text(
            text = "${month.yearMonth.year}年${month.yearMonth.month.value}月",
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = onSurface
        )
        DaysOfWeekTitle(daysOfWeek = daysOfWeek)
    }
}

@Composable
private fun WeekHeader(week: Week, daysOfWeek: List<DayOfWeek>) {
    Column {
        val firstDayOfWeek = week.days.first().date
        val weekNumber = firstDayOfWeek.get(WeekFields.of(Locale.getDefault()).weekOfYear())
        Text(
            text = "第${weekNumber}周",
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = onSurface
        )
        DaysOfWeekTitle(daysOfWeek = daysOfWeek)
    }
}

@Composable
private fun DayContent(
    date: LocalDate,
    position: DayPosition,
    calendarTaskViewModel: CalendarTaskViewModel,
    focusedDate: LocalDate,
    onDateFocused: (LocalDate) -> Unit,
) {
    val calendarTaskList by calendarTaskViewModel.getTasksByDay(date.toString())
        .collectAsState(initial = emptyList())

    val isToday = remember(date) { date == LocalDate.now() }
    val isFocused = date == focusedDate
    val textColor = when {
        isToday -> MaterialTheme.colorScheme.onPrimaryContainer
        position == DayPosition.MonthDate -> MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
    }

    DayContentLayout(
        dayText = date.dayOfMonth.toString(),
        isToday = isToday,
        isFocused = isFocused,
        textColor = textColor,
        calendarTaskList = calendarTaskList,
        onDayClick = { onDateFocused(date) }
    )
}

@Composable
private fun DayContentLayout(
    dayText: String,
    isToday: Boolean,
    isFocused: Boolean,
    textColor: Color,
    calendarTaskList: List<Task.CalendarTaskEntity>,
    onDayClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onDayClick
            )
    ) {
        if (isToday) {
            val color = primaryContainer
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = color,
                    radius = size.minDimension / 2.35f,
                )
            }
        }

        if (calendarTaskList.isNotEmpty()) {
            val colorList = Utils.getColorListOfCalendarTaskList(calendarTaskList)
            MultiColorCircle(
                segments = calendarTaskList.size,
                colors = colorList,
                modifier = Modifier.fillMaxSize()
            )
        }

        if (isFocused) {
            val color = primary

            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = color,
                    radius = size.minDimension / 2,
                    style = Stroke(width = size.minDimension * 0.14f)
                )
            }
        }

        Text(
            text = dayText,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(surface)
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
            )
        }
    }
    HorizontalDivider(color = scrim)
}

@Composable
private fun CalendarTasksList(
    focusedDate: LocalDate,
    calendarTaskViewModel: CalendarTaskViewModel,
) {
    val tasks by calendarTaskViewModel.getTasksByDay(focusedDate.toString())
        .collectAsState(initial = emptyList())

    CalendarTasksList(
        tasks = tasks,
        onTaskChange = { calendarTaskViewModel.update(it) }
    )

}


