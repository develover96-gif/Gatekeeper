package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gated_apps")
data class GatedAppEntity(
  @PrimaryKey val packageName: String,
  val appName: String,
  val category: String = "Social & Media",
  val isGated: Boolean = true,
  val challengeType: String = "MATH", // "MATH", "BREATHING", "STEPS", "HOLDING", "PROMPT"
  val graceWindowMinutes: Int = 15,
  val gatesClearedCount: Int = 0,
  val skipsCount: Int = 0,
  val lastUnlockedTimestamp: Long = 0L,
  val dailyOpensCount: Int = 30,
  val iconType: String = "camera", // "camera", "play", "at", "forum", "video", "browser", "walking"
  val dailyLimitMinutes: Int = 30, // 0 = unlimited, e.g. 30 = gate triggers every launch after 30 min daily usage
  val todayUsedMinutes: Int = 12,
  val sessionLimitMinutes: Int = 10, // Max session duration granted upon gate clearing before gate re-locks
  val isDistracting: Boolean = true // Designated distracting app blocked completely in Zen Mode
)

typealias AppEntity = GatedAppEntity
