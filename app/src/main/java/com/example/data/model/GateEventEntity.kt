package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
  tableName = "gate_events",
  indices = [
    androidx.room.Index(value = ["packageName", "timestamp"]),
    androidx.room.Index(value = ["timestamp"])
  ]
)
data class GateEventEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val packageName: String,
  val appName: String,
  val timestamp: Long = System.currentTimeMillis(),
  val challengeType: String = "MATH", // "MATH", "BREATHING", "STEPS", "HOLDING", "PROMPT"
  val outcome: String = "CLEARED", // "CLEARED", "SKIPPED", "DEFLECTED"
  val durationSeconds: Int = 0,
  val stepsTaken: Int = 0
)
