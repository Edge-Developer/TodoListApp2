package com.edgedevstudio.todolistapp_2.Database

import android.content.Context
import androidx.room.*

/**
 * Created by Olorunleke Opeyemi on 21/01/2019.
 **/

@Database(entities = arrayOf(TaskEntry::class), version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao() : TaskDAO

    companion object {
        private val DATABASE_NAME = "todolist"
        private var sInstance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (sInstance == null) {
                sInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                 // removed querying on main thread, let's run the app and let's see what happens//   .allowMainThreadQueries()
                    .build()
            }

            return sInstance!!
        }
    }
}