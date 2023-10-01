package com.smrize.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bookshelfapp.constants.StringConstant
import com.example.bookshelfapp.data.features.auth.repository.local.dao.UsersDao
import com.example.bookshelfapp.data.features.auth.repository.local.entity.Users

@Database(
    version = 1,
    entities = [Users::class],
    exportSchema = false,
)
abstract class BookShelfDb : RoomDatabase() {

    abstract fun usersDao(): UsersDao

    companion object {
        @Volatile
        private var INSTANCE: BookShelfDb? = null
        fun getDatabase(context: Context): BookShelfDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookShelfDb::class.java,
                    StringConstant.DB_NAME,
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
