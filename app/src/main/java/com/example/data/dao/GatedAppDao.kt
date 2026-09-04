package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.GatedAppEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GatedAppDao {
  @Query("SELECT * FROM gated_apps ORDER BY isGated DESC, gatesClearedCount DESC")
  fun getAllApps(): Flow<List<GatedAppEntity>>

  @Query("SELECT * FROM gated_apps WHERE isGated = 1")
  fun getActiveGatedApps(): Flow<List<GatedAppEntity>>

  @Query("SELECT * FROM gated_apps WHERE packageName = :pkg LIMIT 1")
  suspend fun getApp(pkg: String): GatedAppEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdate(app: GatedAppEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(apps: List<GatedAppEntity>)

  @Update
  suspend fun update(app: GatedAppEntity)

  @Query("UPDATE gated_apps SET isGated = :isGated WHERE packageName = :pkg")
  suspend fun setGated(pkg: String, isGated: Boolean)

  @Query("UPDATE gated_apps SET isGated = :isGated WHERE packageName IN (:packageNames)")
  suspend fun setGatedForPackages(packageNames: List<String>, isGated: Boolean)

  @Query("UPDATE gated_apps SET isGated = :isGated WHERE category = :category")
  suspend fun setGatedByCategory(category: String, isGated: Boolean)

  @Query("UPDATE gated_apps SET category = :newCategory WHERE packageName = :pkg")
  suspend fun updateAppCategory(pkg: String, newCategory: String)

  @Query("DELETE FROM gated_apps WHERE packageName = :pkg")
  suspend fun delete(pkg: String)

  @Query("UPDATE gated_apps SET gatesClearedCount = gatesClearedCount + 1, lastUnlockedTimestamp = :timestamp WHERE packageName = :pkg")
  suspend fun recordGateCleared(pkg: String, timestamp: Long)

  @Query("UPDATE gated_apps SET skipsCount = skipsCount + 1 WHERE packageName = :pkg")
  suspend fun recordGateSkipped(pkg: String)

  @Query("UPDATE gated_apps SET todayUsedMinutes = todayUsedMinutes + :minutes WHERE packageName = :pkg")
  suspend fun addUsageMinutes(pkg: String, minutes: Int)

  @Query("UPDATE gated_apps SET dailyLimitMinutes = :limitMinutes WHERE packageName = :pkg")
  suspend fun updateDailyLimit(pkg: String, limitMinutes: Int)

  @Query("UPDATE gated_apps SET sessionLimitMinutes = :sessionMinutes WHERE packageName = :pkg")
  suspend fun updateSessionLimit(pkg: String, sessionMinutes: Int)

  @Query("UPDATE gated_apps SET isDistracting = :isDistracting WHERE packageName = :pkg")
  suspend fun updateDistracting(pkg: String, isDistracting: Boolean)
}
