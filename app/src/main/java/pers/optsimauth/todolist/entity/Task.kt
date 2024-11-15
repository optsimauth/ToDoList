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
    data class CalendarTaskEntity(
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
    data class FourQuadrantTaskEntity(
        @PrimaryKey(autoGenerate = true) override val id: Int = 0,
        override val content: String,
        override val status: Boolean,
        val quadrant: Int,
    ) : Task()
}


@Entity(
    tableName = "notes"
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val content: String,
    val title: String,
    val updateDateTime: String,
)