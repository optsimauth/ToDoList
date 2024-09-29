package pers.optsimauth.todolist.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "calendar_tasks",
    indices = [Index(value = ["startTime"], unique = false)]
)
data class CalendarTask(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val startTime: String,
    val endTime: String,
    val content: String,
    val day: String,
    val status: Boolean,
)

