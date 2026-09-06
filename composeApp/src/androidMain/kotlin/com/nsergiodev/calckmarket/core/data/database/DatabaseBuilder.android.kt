package com.nsergiodev.calckmarket.core.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<CalkMarketDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath(DB_FILE_NAME)
    return Room.databaseBuilder<CalkMarketDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
