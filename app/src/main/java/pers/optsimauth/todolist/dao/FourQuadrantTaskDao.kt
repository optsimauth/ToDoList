package pers.optsimauth.todolist.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pers.optsimauth.todolist.entity.Task

@Dao
interface FourQuadrantTaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task.FourQuadrantTaskEntity)

    @Update
    fun update(task: Task.FourQuadrantTaskEntity)

    @Query("SELECT * FROM four_quadrant_tasks WHERE quadrant = :quadrant")
    fun getTasksByQuadrant(quadrant: Int): Flow<List<Task.FourQuadrantTaskEntity>>

    @Query("SELECT * FROM four_quadrant_tasks")
    fun getAllItems(): Flow<List<Task.FourQuadrantTaskEntity>>

    @Query("DELETE FROM four_quadrant_tasks WHERE status = true")
    fun deleteAllCheckedTasks()
}