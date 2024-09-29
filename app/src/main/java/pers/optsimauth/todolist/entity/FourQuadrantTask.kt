package pers.optsimauth.todolist.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "four_quadrant_tasks",
    indices = [Index(value = ["quadrant"], unique = false)]
)
data class FourQuadrantTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val status: Boolean,
    val quadrant: Int,
    val content: String,
)