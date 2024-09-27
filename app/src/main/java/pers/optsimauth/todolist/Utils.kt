package pers.optsimauth.todolist

import pers.optsimauth.todolist.entity.CalendarTask
import pers.optsimauth.todolist.ui.colorscheme.allDayColor
import pers.optsimauth.todolist.ui.colorscheme.deadlineColor
import pers.optsimauth.todolist.ui.colorscheme.durationColor
import pers.optsimauth.todolist.ui.colorscheme.startColor

object Utils {

    fun getColorListOfCalendarTaskList(calendarTaskList: List<CalendarTask>) =
        calendarTaskList.map { getColorOfCalendarTask(it) }


    fun getColorOfCalendarTask(calendarTask: CalendarTask) =
        when {
            calendarTask.startTime == "00:00" && calendarTask.endTime == "00:00" -> allDayColor
            calendarTask.startTime == "00:00" -> deadlineColor
            calendarTask.endTime == "00:00" -> startColor
            else -> durationColor
        }


}