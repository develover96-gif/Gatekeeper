package com.example.data

import com.example.data.dao.GateEventDao
import com.example.data.model.GateEventEntity
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.pow

/**
 * AdaptiveChallengeEngine: A lightweight contextual pattern-matching engine
 * that learns "Vulnerability Windows" from user behavior.
 * 
 * Instead of static friction, it computes a score based on historic skip rates
 * for specific time buckets and apps, adapting challenge difficulty dynamically.
 */
class AdaptiveChallengeEngine(private val gateEventDao: GateEventDao) {

    /**
     * Computes a vulnerability score (0.0 to 1.0) for a specific app context.
     * High score = User is likely to skip/fail the gate in this context (high impulse).
     */
    suspend fun computeVulnerabilityScore(packageName: String, now: Instant = Instant.now()): Float {
        val currentBucket = timeBucket(now)
        
        // Fetch recent history for this specific app
        val events = gateEventDao.getRecentEventsForPackageSync(packageName, limit = 50)
        
        // Filter events that happened in the same context bucket (e.g. Morning Weekday)
        val contextualEvents = events.filter { timeBucket(Instant.ofEpochMilli(it.timestamp)) == currentBucket }
        
        if (contextualEvents.size < MIN_SAMPLES) {
            return DEFAULT_SCORE // Cold start default
        }

        var weightedSkips = 0f
        var weightedTotal = 0f
        
        // Compute skip rate with exponential decay (recent events matter more)
        contextualEvents.forEachIndexed { i, event ->
            val recencyWeight = DECAY.pow(i.toFloat())
            weightedTotal += recencyWeight
            
            // "DEFLECTED" outcome indicates a skip (mindful abandonment)
            // If user cleared the gate, they "needed" the app or were focused enough to solve.
            // If they skipped, the gate worked as a deflecter.
            // We want to detect when they usually NEED the gate (high skip potential).
            if (event.outcome == "DEFLECTED") {
                weightedSkips += recencyWeight
            }
        }

        return (weightedSkips / weightedTotal).coerceIn(0f, 1f)
    }

    /**
     * Determines the optimal challenge type and difficulty based on vulnerability score.
     */
    fun selectChallengeForScore(score: Float, baseType: String): Pair<String, Int> {
        return when {
            // High Vulnerability (> 60% skip rate): Insert maximum cognitive friction
            score > 0.6f -> "MATH" to 4 
            
            // Moderate Vulnerability: Use physical friction to break auto-pilot
            score > 0.35f -> "STEPS" to 20 // 20 steps target
            
            // Low Vulnerability: Use the user's preferred challenge at base difficulty
            else -> baseType to 2
        }
    }

    /**
     * Buckets time into 4-hour chunks across Weekdays/Weekends.
     * Total buckets: 6 (hours) * 2 (day type) = 12 context buckets.
     */
    private fun timeBucket(t: Instant): Pair<Int, Boolean> {
        val ldt = LocalDateTime.ofInstant(t, ZoneId.systemDefault())
        val hourBucket = ldt.hour / 4
        val isWeekend = ldt.dayOfWeek.value >= 6
        return hourBucket to isWeekend
    }

    companion object {
        const val MIN_SAMPLES = 8 // Need at least 8 context matches to trust adaptive logic
        const val DEFAULT_SCORE = 0.4f
        const val DECAY = 0.9f // Recency decay factor
    }
}
