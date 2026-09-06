package com.nsergiodev.calckmarket.core.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
fun getDatabaseBuilder(): RoomDatabase.Builder<CalkMarketDatabase> {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )
    val dirPath = requireNotNull(documentDirectory?.path) { "No se pudo obtener el directorio de documentos en iOS" }
    val dbFilePath = "$dirPath/$DB_FILE_NAME"
    return Room.databaseBuilder<CalkMarketDatabase>(
        name = dbFilePath
    )
}
