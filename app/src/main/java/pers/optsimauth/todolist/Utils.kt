package pers.optsimauth.todolist

import android.content.Context
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pers.optsimauth.todolist.backup.BackupManager
import pers.optsimauth.todolist.config.colorscheme.blue
import pers.optsimauth.todolist.config.colorscheme.green
import pers.optsimauth.todolist.config.colorscheme.red
import pers.optsimauth.todolist.config.colorscheme.yellow
import pers.optsimauth.todolist.database.AppDatabase
import pers.optsimauth.todolist.entity.Task
import java.time.format.DateTimeFormatter

object Utils {

    fun getColorListOfCalendarTaskList(calendarTaskList: List<Task.CalendarTaskEntity>) =
        calendarTaskList.map { getColorOfCalendarTask(it) }


    fun getColorOfCalendarTask(calendarTask: Task.CalendarTaskEntity) =
        when {
            calendarTask.startTime == "00:00" && calendarTask.endTime == "00:00" -> green
            calendarTask.startTime == "00:00" -> red
            calendarTask.endTime == "00:00" -> blue
            else -> yellow
        }

    fun getColorOfQuadrant(quadrant: Int) =
        when (quadrant) {
            1 -> red
            2 -> yellow
            3 -> blue
            4 -> green
            else -> throw IllegalArgumentException("Invalid quadrant number")
        }

    fun invertColor(color: Color): Color {
        return Color(0xFFFFFF - color.toArgb())
    }

    fun getMyDatetimeFormatter(): DateTimeFormatter {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    }

    fun getMyDateFormatter(): DateTimeFormatter {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }

}


object DatabaseUtils {
    private val gson = GsonBuilder()
        .setPrettyPrinting() // 美化输出的 JSON
        .create()

    suspend fun exportDatabaseJson(context: Context, database: AppDatabase): Result<String> {
        return withContext(Dispatchers.IO) {
            val backupManager = BackupManager(context, database, gson)

// 导出
            backupManager.exportToJson().onSuccess { message ->
                // 处理成功
            }.onFailure { error ->
                // 处理错误
            }
        }
    }

    suspend fun importDatabaseJson(
        context: Context,
        uri: Uri,
        database: AppDatabase
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            val backupManager = BackupManager(context, database, gson)
            backupManager.importFromJson(uri).onSuccess {
                // 处理成功
            }.onFailure { error ->
                // 处理错误
            }
        }
    }


//    fun importDatabase(context: Context): Result<String> {
//        return try {
//            // Get the database folder path
//            val databasePath = context.getDatabasePath("todolist_database").parentFile
//
//            // Get the destination folder path in Documents
//            val folderPath =
//                "${Environment.getExternalStorageDirectory()}/${Environment.DIRECTORY_DOCUMENTS}/${context.packageName}"
//            val folder = File(folderPath)
//
//            // Ensure the destination folder exists, create it if necessary
//            if (!folder.isDirectory) folder.mkdirs()
//
//            // Delete the existing database folder if it exists
//            if (folder.exists()) {
//                folder.deleteRecursively()
//            }
//
//            // Copy the entire database folder to the destination
//            databasePath.copyRecursively(folder, overwrite = true)
//
//            Result.success("Database files imported from folder: $folderPath")
//        } catch (e: IOException) {
//            Result.failure(e)
//        }
//    }
//
//    fun exportDatabase(context: Context): Result<String> {
//        return try {
//            // Get the database folder path
//            val databasePath = context.getDatabasePath("todolist_database").parentFile
//
//            // Get the destination folder path in Documents
//            val folderPath =
//                "${Environment.getExternalStorageDirectory()}/${Environment.DIRECTORY_DOCUMENTS}/${context.packageName}"
//            val folder = File(folderPath)
//
//            // Ensure the destination folder exists, create it if necessary
//            if (!folder.exists()) folder.mkdirs()
//
//            // Copy the entire database folder to the destination
//            databasePath?.copyRecursively(folder, overwrite = true)
//
//            Result.success("Database files exported to folder: $folderPath")
//        } catch (e: IOException) {
//            Result.failure(e)
//        }
//    }
//
//    fun restartApp(context: Context) {
//        // 获取应用的启动 Activity
//        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
//        val componentName = intent?.component
//        val mainIntent = Intent.makeRestartActivityTask(componentName)
//
//        // 启动应用的主 Activity，并结束当前的 Activity
//        context.startActivity(mainIntent)
//        Runtime.getRuntime().exit(0)  // 结束当前进程，达到重启效果
//    }

}
