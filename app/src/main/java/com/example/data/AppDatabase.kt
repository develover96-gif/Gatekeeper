package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.DailyStatDao
import com.example.data.dao.GateEventDao
import com.example.data.dao.GatedAppDao
import com.example.data.model.DailyStatEntity
import com.example.data.model.GateEventEntity
import com.example.data.model.GatedAppEntity

@Database(
  entities = [GatedAppEntity::class, DailyStatEntity::class, GateEventEntity::class],
  version = 3,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun gatedAppDao(): GatedAppDao
  abstract fun dailyStatDao(): DailyStatDao
  abstract fun gateEventDao(): GateEventDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "gatekeeper_database"
        )
          .fallbackToDestructiveMigration()
          .build()
        INSTANCE = instance
        instance
      }
    }
  }
}
