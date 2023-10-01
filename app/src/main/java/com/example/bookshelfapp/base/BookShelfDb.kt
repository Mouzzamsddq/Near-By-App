package com.example.bookshelfapp.base

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bookshelfapp.constants.StringConstant
import com.example.bookshelfapp.data.features.auth.repository.local.dao.UsersDao
import com.example.bookshelfapp.data.features.auth.repository.local.entity.Users
import com.example.bookshelfapp.data.features.favourites.repository.local.FavBook
import com.example.bookshelfapp.data.features.favourites.repository.local.dao.FavBooksDao

@Database(
    version = 2,
    entities = [Users::class, FavBook::class],
    exportSchema = false,
)
@TypeConverters(Converter::class)
abstract class BookShelfDb : RoomDatabase() {

    abstract fun usersDao(): UsersDao
    abstract fun favBookDao(): FavBooksDao

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
