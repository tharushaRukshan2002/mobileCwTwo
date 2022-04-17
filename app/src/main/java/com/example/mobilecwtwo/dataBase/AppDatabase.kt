package com.example.mobilecwtwo.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Movie::class], version = 1)

abstract class AppDatabase:RoomDatabase() {
    abstract fun movieDao(): MovieDao

    //making the class single ton
    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(context,AppDatabase::class.java, "Movie DB")
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}