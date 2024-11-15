package pers.optsimauth.todolist.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pers.optsimauth.todolist.entity.NoteEntity

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(noteEntity: NoteEntity)

    @Update
    fun update(noteEntity: NoteEntity)

    @Delete
    fun delete(noteEntity: NoteEntity)

    @Query("SELECT * FROM notes")
    fun getAllItems(): Flow<List<NoteEntity>>

}