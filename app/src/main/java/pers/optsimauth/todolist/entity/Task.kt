package pers.optsimauth.todolist.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


sealed class Task {
    abstract val id: Int
    abstract val content: String
    abstract val status: Boolean

    @Entity(
        tableName = "calendar_tasks",
        indices = [Index(value = ["startTime"], unique = false)]
    )
    data class CalendarTask(
        @PrimaryKey(autoGenerate = true) override val id: Int = 0,
        override val content: String,
        override val status: Boolean,
        val startTime: String,
        val endTime: String,
        val day: String,
    ) : Task()

    @Entity(
        tableName = "four_quadrant_tasks",
        indices = [Index(value = ["quadrant"], unique = false)]
    )
    data class FourQuadrantTask(
        @PrimaryKey(autoGenerate = true) override val id: Int = 0,
        override val content: String,
        override val status: Boolean,
        val quadrant: Int,
    ) : Task()
}