package pers.optsimauth.todolist.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pers.optsimauth.todolist.entity.Task

@Dao
interface CalendarTaskDao {
    @Query("SELECT * FROM calendar_tasks")
    fun getAllItems(): Flow<List<Task.CalendarTaskEntity>>

    @Query("SELECT * FROM calendar_tasks WHERE day = :day ORDER BY startTime")
    fun getTasksByDay(day: String): Flow<List<Task.CalendarTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task.CalendarTaskEntity)

    @Update
    fun update(task: Task.CalendarTaskEntity)

    @Delete
    fun delete(task: Task.CalendarTaskEntity)

    @Query("DELETE FROM calendar_tasks WHERE status = True")
    fun deleteAllCheckedTasks()

}