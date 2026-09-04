package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_stats")
data class DailyStatEntity(
  @PrimaryKey val dayOfWeek: String, // "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
  val date: String,
  val gatesCleared: Int,
  val skipsCount: Int,
  val minutesSaved: Int,
  val isPeak: Boolean = false,
  val sortOrder: Int = 0
)

data class MathChallengeConfig(
  val difficultyTier: String = "STANDARD", // "GENTLE", "STANDARD", "ADVANCED", "PROBLEM_SOLVING"
  val randomizeOnFailedEntry: Boolean = true,
  val inputTimeoutCountdown: Boolean = false,
  val progressiveEscalation: Boolean = true,
  val permittedAdditionSubtraction: Boolean = true,
  val permittedMultiplication: Boolean = false,
  val permittedSequencePatterns: Boolean = false,
  val emergencyOverrideCode: String = "1234",
  val predefinedDifficultyLevel: Int = 2, // 1 = Gentle, 2 = Standard, 3 = Advanced, 4 = Problem Solving
  val problemSolvingType: String = "ANY" // "ANY", "SEQUENCES", "BALANCE", "LOGIC_RIDDLES"
)
