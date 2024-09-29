package pers.optsimauth.todolist.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pers.optsimauth.todolist.entity.Task

@Dao
interface FourQuadrantTaskDao {
    @Insert
    fun insert(task: Task.FourQuadrantTask)

    @Update
    fun update(task: Task.FourQuadrantTask)

    @Query("SELECT * FROM four_quadrant_tasks WHERE quadrant = :quadrant")
    fun getTasksByQuadrant(quadrant: Int): Flow<List<Task.FourQuadrantTask>>

    @Query("DELETE FROM four_quadrant_tasks WHERE status = true")
    fun deleteAllCheckedTasks()
}