package pers.optsimauth.todolist.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pers.optsimauth.todolist.entity.FourQuadrantTask

@Dao
interface FourQuadrantTaskDao {
    @Insert
    fun insert(task: FourQuadrantTask)

    @Update
    fun update(task: FourQuadrantTask)

    @Query("SELECT * FROM four_quadrant_tasks WHERE quadrant = :quadrant")
    fun getTasksByQuadrant(quadrant: Int): Flow<List<FourQuadrantTask>>

    @Query("DELETE FROM four_quadrant_tasks WHERE status = true")
    fun deleteAllCheckedTasks()
}