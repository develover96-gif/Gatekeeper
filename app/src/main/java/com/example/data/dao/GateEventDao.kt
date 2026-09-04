package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.GateEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GateEventDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertEvent(event: GateEventEntity): Long

  @Query("SELECT * FROM gate_events ORDER BY timestamp DESC")
  fun getAllEvents(): Flow<List<GateEventEntity>>

  @Query("SELECT * FROM gate_events WHERE packageName = :packageName ORDER BY timestamp DESC")
  fun getEventsForApp(packageName: String): Flow<List<GateEventEntity>>

  @Query("SELECT * FROM gate_events ORDER BY timestamp DESC LIMIT :limit")
  fun getRecentEvents(limit: Int = 50): Flow<List<GateEventEntity>>

  @Query("SELECT * FROM gate_events WHERE timestamp >= :sinceTimestamp ORDER BY timestamp DESC")
  fun getEventsSince(sinceTimestamp: Long): Flow<List<GateEventEntity>>

  @Query("SELECT * FROM gate_events WHERE packageName = :packageName ORDER BY timestamp DESC LIMIT :limit")
  suspend fun getRecentEventsForPackageSync(packageName: String, limit: Int): List<GateEventEntity>

  @Query("SELECT * FROM gate_events WHERE packageName = :packageName AND timestamp >= :sinceTimestamp ORDER BY timestamp DESC")
  suspend fun getEventsForPackageSinceSync(packageName: String, sinceTimestamp: Long): List<GateEventEntity>

  @Query("SELECT COUNT(*) FROM gate_events")
  suspend fun getEventCount(): Int

  @Query("DELETE FROM gate_events")
  suspend fun clearAllEvents()
}
