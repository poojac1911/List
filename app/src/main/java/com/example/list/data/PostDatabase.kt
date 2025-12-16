package com.example.list.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class PostDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var Instance: PostDatabase? = null

        fun getDatabase(context: Context): PostDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, PostDatabase::class.java, "posts_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}