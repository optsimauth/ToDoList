package pers.optsimauth.todolist.backup

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import pers.optsimauth.todolist.database.AppDatabase
import pers.optsimauth.todolist.entity.NoteEntity
import pers.optsimauth.todolist.entity.Task
import pers.optsimauth.todolist.viewmodel.ContextHolder
import java.io.IOException


// 定义备份数据类
data class DatabaseBackup(
    val calendarTasks: List<Task.CalendarTaskEntity> = emptyList(),
    val fourQuadrantTasks: List<Task.FourQuadrantTaskEntity> = emptyList(),
    val notes: List<NoteEntity> = emptyList()
)

suspend fun getBackUpData(): DatabaseBackup {
    val database = AppDatabase.getInstance(ContextHolder.appContext)
    return DatabaseBackup(
        calendarTasks = database.calendarTaskDao().getAllItems().first(),
        fourQuadrantTasks = database.fourQuadrantTaskDao().getAllItems().first(),
        notes = database.noteDao().getAllItems().first()
    )
}

fun importBackUp(backup: DatabaseBackup) {
    val database = AppDatabase.getInstance(ContextHolder.appContext)
    
    backup.calendarTasks.forEach {
        database.calendarTaskDao().insert(it)
    }
    backup.fourQuadrantTasks.forEach {
        database.fourQuadrantTaskDao().insert(it)
    }
    backup.notes.forEach {
        database.noteDao().insert(it)
    }
}


// 定义导入导出管理器
class BackupManager(
    private val context: Context,
    private val database: AppDatabase,
    private val gson: Gson
) {
    // 导出数据
    suspend fun exportToJson(): Result<String> = withContext(Dispatchers.IO) {
        try {
            // 收集所有数据
            val backup = getBackUpData()

            val jsonString = gson.toJson(backup)
            val fileName = "${context.packageName}_backup.json"

            // 准备文件信息
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/json")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
                }
            }

            // 保存文件
            saveJsonToFile(contentValues, fileName, jsonString)
            Result.success("文件已保存到 Documents 目录: $fileName")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 导入数据
    suspend fun importFromJson(uri: Uri): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // 读取文件
            val jsonString = context.contentResolver.openInputStream(uri)?.use {
                it.bufferedReader().readText()
            } ?: throw IOException("无法读取文件")

            // 解析数据
            val backup = gson.fromJson(jsonString, DatabaseBackup::class.java)

            // 导入所有数据
            database.runInTransaction {
                importBackUp(backup)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 保存文件的辅助方法
    private fun saveJsonToFile(
        contentValues: ContentValues,
        fileName: String,
        jsonString: String
    ) {
        val collection = MediaStore.Files.getContentUri("external")
        val selection = "${MediaStore.MediaColumns.DISPLAY_NAME} = ?"
        val selectionArgs = arrayOf(fileName)

        // 检查并删除已存在的文件
        context.contentResolver.query(
            collection,
            arrayOf(MediaStore.MediaColumns._ID),
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                val existingUri = ContentUris.withAppendedId(collection, id)
                context.contentResolver.delete(existingUri, null, null)
            }
        }

        // 创建新文件
        val uri = context.contentResolver.insert(collection, contentValues)
            ?: throw IOException("无法创建文件")

        // 写入内容
        context.contentResolver.openOutputStream(uri)?.use {
            it.write(jsonString.toByteArray())
        } ?: throw IOException("无法写入文件")
    }
}





