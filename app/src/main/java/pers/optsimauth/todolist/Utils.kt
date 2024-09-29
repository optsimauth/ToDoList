package pers.optsimauth.todolist

import pers.optsimauth.todolist.entity.CalendarTask
import pers.optsimauth.todolist.entity.FourQuadrantTask
import pers.optsimauth.todolist.ui.colorscheme.blue
import pers.optsimauth.todolist.ui.colorscheme.green
import pers.optsimauth.todolist.ui.colorscheme.red
import pers.optsimauth.todolist.ui.colorscheme.yellow

object Utils {

    fun getColorListOfCalendarTaskList(calendarTaskList: List<CalendarTask>) =
        calendarTaskList.map { getColorOfCalendarTask(it) }


    fun getColorOfCalendarTask(calendarTask: CalendarTask) =
        when {
            calendarTask.startTime == "00:00" && calendarTask.endTime == "00:00" -> green
            calendarTask.startTime == "00:00" -> red
            calendarTask.endTime == "00:00" -> blue
            else -> yellow
        }

    fun getColorOfFourQuadrantTask(fourQuadrantTask: FourQuadrantTask) =
        when (fourQuadrantTask.quadrant) {
            1 -> red
            2 -> yellow
            3 -> blue
            4 -> green
            else -> throw IllegalArgumentException("Invalid quadrant number")
        }

}