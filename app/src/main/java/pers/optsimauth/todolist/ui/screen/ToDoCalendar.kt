package pers.optsimauth.todolist.ui.screen

import MultiColorCircle
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.daysOfWeek
import pers.optsimauth.todolist.Utils
import pers.optsimauth.todolist.entity.CalendarTask
import pers.optsimauth.todolist.ui.colorscheme.onSurface
import pers.optsimauth.todolist.ui.colorscheme.outlineVariant
import pers.optsimauth.todolist.ui.colorscheme.primary
import pers.optsimauth.todolist.ui.colorscheme.primaryContainer
import pers.optsimauth.todolist.ui.colorscheme.scrim
import pers.optsimauth.todolist.ui.colorscheme.secondary
import pers.optsimauth.todolist.ui.colorscheme.surface
import pers.optsimauth.todolist.ui.component.dialog.TasksList
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


    Column(
        modifier = Modifier
            .fillMaxSize()

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
                IconButton(onClick = { isMonthView = false }) {
                    Icon(
                        Icons.Default.CalendarViewWeek,
                        contentDescription = null,
                        tint = if (!isMonthView) primary else outlineVariant
                    )
                }
                IconButton(onClick = { isMonthView = true }) {
                    Icon(
                        Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = if (isMonthView) primary else outlineVariant
                    )
                }
            }
        }

        AnimatedContent(
            targetState = isMonthView,
            transitionSpec = {
                if (targetState) {
                    slideInVertically { height -> height } + fadeIn() togetherWith
                            slideOutVertically { height -> -height } + fadeOut()
                } else {
                    slideInVertically { height -> -height } + fadeIn() togetherWith
                            slideOutVertically { height -> height } + fadeOut()
                }.using(
                    SizeTransform(clip = false)
                )
            }
        ) { targetIsMonthView ->
            if (targetIsMonthView) {
                MonthCalendarView(
                    focusedDate = focusedDate,
                    calendarTaskViewModel = calendarTaskViewModel,
                    onDateFocused = {
                        onDateFocused(it) //用于判断floatingActionButton添加哪天的日期
                        focusedDate = it //为选择日期画圈
                    }
                )
            } else {
                WeekCalendarView(
                    focusedDate = focusedDate,
                    calendarTaskViewModel = calendarTaskViewModel,
                    onDateFocused = {
                        onDateFocused(it)
                        focusedDate = it
                    },
                )
            }
        }
    }
}

@Composable
fun MonthCalendarView(
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


    Column {
        HorizontalCalendar(state = state,
            dayContent = { calendarDay ->
                MonthDay(
                    day = calendarDay,
                    calendarTaskList = calendarTaskViewModel.getTasksByDay(day = calendarDay.date.toString())
                        .collectAsState(initial = emptyList()).value,
                    isFocused = calendarDay.date == focusedDate,
                    onDayClick = {
                        onDateFocused(calendarDay.date)
                    }
                )
            },
            monthHeader = { calendarMonth ->

                Text(
                    text = "${calendarMonth.yearMonth.year}年${calendarMonth.yearMonth.month.value}月",
                    modifier = Modifier
                        .padding(8.dp),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = onSurface
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(surface)
                ) {
                    DaysOfWeekTitle(daysOfWeek = daysOfWeek)
                }
                HorizontalDivider(color = scrim)
            },
            monthBody = { _, content ->
                Box(
                    modifier = Modifier.background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                surface, surface
                            )
                        )
                    )
                ) {
                    content()
                }
            },
            monthContainer = { _, container ->
                val configuration = LocalConfiguration.current
                val screenWidth = configuration.screenWidthDp.dp
                Box(
                    modifier = Modifier
                        .width(screenWidth * 1)
                        .padding(8.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .border(
                            color = secondary, width = 1.dp, shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    container()
                }
            }
        )
        // 获取指定日期的任务
        val tasks by calendarTaskViewModel.getTasksByDay(day = focusedDate.toString())
            .collectAsState(initial = emptyList())

        TasksList(tasks = tasks, onTaskChange = { calendarTaskViewModel.update(it) })

    }
}

@Composable
fun WeekCalendarView(
    focusedDate: LocalDate,
    calendarTaskViewModel: CalendarTaskViewModel,
    onDateFocused: (LocalDate) -> Unit,
) {
    val currentDate = LocalDate.now()

    // 初始化周历状态
    val state = rememberWeekCalendarState(
        startDate = currentDate.minusDays(400),
        endDate = currentDate.plusDays(200),
        firstVisibleWeekDate = currentDate
    )
    val daysOfWeek = remember { daysOfWeek() }

    Column {
        // 头部日期显示
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
                    WeekDay(
                        day = day,
                        calendarTaskList = calendarTaskViewModel.getTasksByDay(day = day.date.toString())
                            .collectAsState(initial = emptyList()).value,
                        isFocused = day.date == focusedDate,
                        onDayClick = {
                            onDateFocused(day.date)
                        }
                    )
                },
                weekHeader = { week ->
                    val firstDayOfWeek = week.days.first().date
                    val weekNumber =
                        firstDayOfWeek.get(WeekFields.of(Locale.getDefault()).weekOfYear())
                    Text(
                        text = "第${weekNumber}周",
                        modifier = Modifier
                            .padding(8.dp),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        color = onSurface
                    )


                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(surface)
                    ) {
                        DaysOfWeekTitle(daysOfWeek = daysOfWeek)
                    }
                    HorizontalDivider(color = scrim)
                }
            )
        }

        // 显示当前选中日期的任务
        val tasks by calendarTaskViewModel.getTasksByDay(day = focusedDate.toString())
            .collectAsState(initial = emptyList())

        TasksList(tasks = tasks, onTaskChange = { calendarTaskViewModel.update(it) })
    }
}


@Composable
fun MonthDay(
    day: CalendarDay,
    calendarTaskList: List<CalendarTask> = emptyList(),
    isFocused: Boolean,
    onDayClick: () -> Unit = {},
) {
    val isToday = remember(day.date) { day.date == LocalDate.now() }
    val textColor = when {
        isToday -> MaterialTheme.colorScheme.onPrimaryContainer
        day.position == DayPosition.MonthDate -> MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
    }

    DayContent(
        isToday = isToday,
        isFocused = isFocused,
        textColor = textColor,
        calendarTaskList = calendarTaskList,
        onDayClick = onDayClick,
        dayText = day.date.dayOfMonth.toString()
    )
}

@Composable
fun WeekDay(
    day: WeekDay,
    calendarTaskList: List<CalendarTask>,
    isFocused: Boolean,
    onDayClick: () -> Unit = {},
) {
    val isToday = remember(day.date) { day.date == LocalDate.now() }
    val textColor =
        if (isToday) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface

    DayContent(
        isToday = isToday,
        isFocused = isFocused,
        textColor = textColor,
        calendarTaskList = calendarTaskList,
        onDayClick = onDayClick,
        dayText = day.date.dayOfMonth.toString(),
    )
}

@Composable
fun DayContent(
    isToday: Boolean,
    isFocused: Boolean,
    textColor: Color,
    calendarTaskList: List<CalendarTask>,
    onDayClick: () -> Unit,
    dayText: String,
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

        val colorList = Utils.getColorListOfCalendarTaskList(calendarTaskList)
        if (calendarTaskList.isNotEmpty()) {
            MultiColorCircle(
                segments = calendarTaskList.size,
                colors = colorList,
                modifier = Modifier
                    .fillMaxSize()
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
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
            )
        }
    }

}


