package com.example.priyomkaipt2019.db

import android.content.Context
import androidx.room.*

@Database(entities = [RecordDetails::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao

    companion object {
        private lateinit var db: AppDatabase

        fun getInstance(context: Context? = null): AppDatabase? {
            if (!this::db.isInitialized) {
                if (context != null)
                    db = Room.databaseBuilder(context, AppDatabase::class.java, "priyomka-2019").build()
                else
                    return null
            }
            return db
        }
    }
}
