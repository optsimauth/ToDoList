// CalendarTaskDatabase.kt
package pers.optsimauth.todolist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pers.optsimauth.todolist.dao.CalendarTaskDao
import pers.optsimauth.todolist.dao.FourQuadrantTaskDao
import pers.optsimauth.todolist.dao.NoteDao
import pers.optsimauth.todolist.entity.NoteEntity
import pers.optsimauth.todolist.entity.Task

@Database(
    entities = [Task.CalendarTaskEntity::class, Task.FourQuadrantTaskEntity::class, NoteEntity::class],
    version = 4
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun calendarTaskDao(): CalendarTaskDao

    abstract fun fourQuadrantTaskDao(): FourQuadrantTaskDao

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "todolist_database"
                )
//                    .fallbackToDestructiveMigration() // 可选：在没有提供迁移策略时使用
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}