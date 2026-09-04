package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.DailyStatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyStatDao {
  @Query("SELECT * FROM daily_stats ORDER BY sortOrder ASC")
  fun getWeeklyStats(): Flow<List<DailyStatEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(stats: List<DailyStatEntity>)

  @Query("UPDATE daily_stats SET gatesCleared = gatesCleared + 1, minutesSaved = minutesSaved + 3 WHERE dayOfWeek = :dayOfWeek")
  suspend fun recordGateClearedForDay(dayOfWeek: String)

  @Query("UPDATE daily_stats SET skipsCount = skipsCount + 1, minutesSaved = minutesSaved + 5 WHERE dayOfWeek = :dayOfWeek")
  suspend fun recordSkipForDay(dayOfWeek: String)
}
