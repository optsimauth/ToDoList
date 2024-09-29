// CalendarTaskDatabase.kt
package pers.optsimauth.todolist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pers.optsimauth.todolist.dao.CalendarTaskDao
import pers.optsimauth.todolist.dao.FourQuadrantTaskDao
import pers.optsimauth.todolist.entity.CalendarTask
import pers.optsimauth.todolist.entity.FourQuadrantTask

@Database(entities = [CalendarTask::class, FourQuadrantTask::class], version = 2)
abstract class CalendarTaskDatabase : RoomDatabase() {
    abstract fun calendarTaskDao(): CalendarTaskDao

    abstract fun fourQuadrantTaskDao(): FourQuadrantTaskDao

    companion object {
        @Volatile
        private var INSTANCE: CalendarTaskDatabase? = null

        fun getDatabase(context: Context): CalendarTaskDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CalendarTaskDatabase::class.java,
                    "calendar_task_database"
                ).fallbackToDestructiveMigration() // 可选：在没有提供迁移策略时使用
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}