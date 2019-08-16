package com.mohsenmb.googlenewsapisample.repository.persistence

import android.content.Context
import androidx.room.*
import java.util.*


object DateTypeConverter {

    @JvmStatic
    @TypeConverter
    fun fromMillis(millis: Long?): Date {
        return Date(millis ?: 0)
    }

    @JvmStatic
    @TypeConverter
    fun toMillis(date: Date?): Long {
        return date?.time ?: 0
    }
}

/**
 * To implement this part used the following article
 *      https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/index.html?index=..%2F..index#6
 */
@TypeConverters(DateTypeConverter::class)
@Database(entities = [PersistedArticle::class], version = 1)
abstract class ArticlesDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: ArticlesDatabase? = null

        fun getDatabase(context: Context): ArticlesDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ArticlesDatabase::class.java,
                    "articles_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

    abstract fun articlesDao(): ArticlesDao
}