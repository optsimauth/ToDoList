package pers.optsimauth.todolist

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import pers.optsimauth.todolist.config.colorscheme.blue
import pers.optsimauth.todolist.config.colorscheme.green
import pers.optsimauth.todolist.config.colorscheme.red
import pers.optsimauth.todolist.config.colorscheme.yellow
import pers.optsimauth.todolist.entity.Task

object Utils {

    fun getColorListOfCalendarTaskList(calendarTaskList: List<Task.CalendarTask>) =
        calendarTaskList.map { getColorOfCalendarTask(it) }


    fun getColorOfCalendarTask(calendarTask: Task.CalendarTask) =
        when {
            calendarTask.startTime == "00:00" && calendarTask.endTime == "00:00" -> green
            calendarTask.startTime == "00:00" -> red
            calendarTask.endTime == "00:00" -> blue
            else -> yellow
        }

    fun getColorOfQuadrant(quadrant: Int) =
        when (quadrant) {
            1 -> red
            2 -> yellow
            3 -> blue
            4 -> green
            else -> throw IllegalArgumentException("Invalid quadrant number")
        }

    fun invertColor(color: Color): Color {
        return Color(0xFFFFFF - color.toArgb())
    }

}