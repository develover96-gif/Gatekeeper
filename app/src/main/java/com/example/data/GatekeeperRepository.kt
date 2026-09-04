package com.example.data

import android.content.Context
import com.example.data.model.CategoryTags
import com.example.data.model.DailyStatEntity
import com.example.data.model.GateEventEntity
import com.example.data.model.GatedAppEntity
import com.example.data.model.MathChallengeConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GatekeeperRepository(private val db: AppDatabase, private val context: Context) {
  constructor(context: Context) : this(AppDatabase.getDatabase(context), context)

  private val scope = CoroutineScope(Dispatchers.IO)
  private val prefs = context.getSharedPreferences("gatekeeper_prefs", Context.MODE_PRIVATE)

  val allApps: Flow<List<GatedAppEntity>> = db.gatedAppDao().getAllApps()
  val weeklyStats: Flow<List<DailyStatEntity>> = db.dailyStatDao().getWeeklyStats()
  val gateEvents: Flow<List<GateEventEntity>> = db.gateEventDao().getAllEvents()

  private val _currentStreak = MutableStateFlow(prefs.getInt("current_streak", 4))
  val currentStreak = _currentStreak.asStateFlow()

  private val _milestoneMessage = MutableStateFlow(
    prefs.getString("milestone_message", "You've cleared gates for 4 days in a row! Mindful reflex engaged.")
      ?: "You've cleared gates for 4 days in a row! Mindful reflex engaged."
  )
  val milestoneMessage = _milestoneMessage.asStateFlow()

  private val _stepTarget = MutableStateFlow(prefs.getInt("step_target", 20))
  val stepTarget = _stepTarget.asStateFlow()

  private val _mathConfig = MutableStateFlow(loadMathConfig())
  val mathConfig = _mathConfig.asStateFlow()

  private val _isOnboardingCompleted = MutableStateFlow(prefs.getBoolean("onboarding_done", false))
  val isOnboardingCompleted = _isOnboardingCompleted.asStateFlow()

  private val _tactileFriction = MutableStateFlow(prefs.getBoolean("tactile_friction", true))
  val tactileFriction = _tactileFriction.asStateFlow()

  private val _emergencyOverrideEnabled = MutableStateFlow(prefs.getBoolean("emergency_override", true))
  val emergencyOverrideEnabled = _emergencyOverrideEnabled.asStateFlow()

  private val _isDarkGlassTheme = MutableStateFlow(prefs.getBoolean("dark_glass_theme", true))
  val isDarkGlassTheme = _isDarkGlassTheme.asStateFlow()

  // Re-Open Grace Period (Prevents gate if re-opening app within e.g. 60s)
  private val _isReopenGraceEnabled = MutableStateFlow(prefs.getBoolean("reopen_grace_enabled", true))
  val isReopenGraceEnabled = _isReopenGraceEnabled.asStateFlow()

  private val _reopenGraceSeconds = MutableStateFlow(prefs.getInt("reopen_grace_seconds", 60))
  val reopenGraceSeconds = _reopenGraceSeconds.asStateFlow()

  private val lastAppClosedTimes = java.util.concurrent.ConcurrentHashMap<String, Long>()

  // Zen Mode (Temporarily disables challenges and blocks access to distracting apps)
  private val _zenModeActiveUntil = MutableStateFlow(prefs.getLong("zen_mode_until", 0L))
  val zenModeActiveUntil = _zenModeActiveUntil.asStateFlow()

  // Adaptive Gate Intelligence
  private val _isAdaptiveGatesEnabled = MutableStateFlow(prefs.getBoolean("adaptive_gates_enabled", false))
  val isAdaptiveGatesEnabled = _isAdaptiveGatesEnabled.asStateFlow()

  private val adaptiveEngine = AdaptiveChallengeEngine(db.gateEventDao())

  private val _isZenModeActiveFlow = MutableStateFlow(System.currentTimeMillis() < prefs.getLong("zen_mode_until", 0L))
  val isZenModeActiveFlow = _isZenModeActiveFlow.asStateFlow()

  private val _zenModeDurationMinutes = MutableStateFlow(prefs.getInt("zen_mode_duration_minutes", 30))
  val zenModeDurationMinutes = _zenModeDurationMinutes.asStateFlow()

  init {
    scope.launch {
      seedInitialDataIfNeeded()
    }
  }

  private suspend fun seedInitialDataIfNeeded() {
    val existingApps = db.gatedAppDao().getAllApps().first()
    if (existingApps.isEmpty()) {
      val initialApps = listOf(
        GatedAppEntity(
          packageName = "com.instagram.android",
          appName = "Instagram",
          category = CategoryTags.SOCIAL,
          isGated = true,
          challengeType = "MATH",
          graceWindowMinutes = 15,
          gatesClearedCount = 38,
          skipsCount = 7,
          dailyOpensCount = 48,
          iconType = "camera"
        ),
        GatedAppEntity(
          packageName = "com.zhiliaoapp.musically",
          appName = "TikTok",
          category = CategoryTags.ENTERTAINMENT,
          isGated = true,
          challengeType = "BREATHING",
          graceWindowMinutes = 15,
          gatesClearedCount = 27,
          skipsCount = 5,
          dailyOpensCount = 36,
          iconType = "play"
        ),
        GatedAppEntity(
          packageName = "com.twitter.android",
          appName = "X / Twitter",
          category = CategoryTags.NEWS,
          isGated = true,
          challengeType = "MATH",
          graceWindowMinutes = 15,
          gatesClearedCount = 18,
          skipsCount = 4,
          dailyOpensCount = 24,
          iconType = "at"
        ),
        GatedAppEntity(
          packageName = "com.reddit.frontpage",
          appName = "Reddit",
          category = CategoryTags.SOCIAL,
          isGated = true,
          challengeType = "MATH",
          graceWindowMinutes = 15,
          gatesClearedCount = 6,
          skipsCount = 2,
          dailyOpensCount = 19,
          iconType = "forum"
        ),
        GatedAppEntity(
          packageName = "com.google.android.youtube",
          appName = "YouTube",
          category = CategoryTags.ENTERTAINMENT,
          isGated = false,
          challengeType = "BREATHING",
          graceWindowMinutes = 15,
          gatesClearedCount = 0,
          skipsCount = 0,
          dailyOpensCount = 12,
          iconType = "video"
        ),
        GatedAppEntity(
          packageName = "com.android.chrome",
          appName = "Chrome",
          category = CategoryTags.PRODUCTIVITY,
          isGated = false,
          challengeType = "MATH",
          graceWindowMinutes = 15,
          gatesClearedCount = 0,
          skipsCount = 0,
          dailyOpensCount = 15,
          iconType = "browser"
        )
      )
      db.gatedAppDao().insertAll(initialApps)
    } else {
      // Migrate legacy category labels to standard tags if needed
      existingApps.forEach { app ->
        val normalized = CategoryTags.normalize(app.category)
        if (normalized != app.category) {
          db.gatedAppDao().updateAppCategory(app.packageName, normalized)
        }
      }
    }

    val existingStats = db.dailyStatDao().getWeeklyStats().first()
    if (existingStats.isEmpty()) {
      val initialStats = listOf(
        DailyStatEntity(dayOfWeek = "Mon", date = "Mon, Oct 18", gatesCleared = 11, skipsCount = 2, minutesSaved = 34, isPeak = false, sortOrder = 1),
        DailyStatEntity(dayOfWeek = "Tue", date = "Tue, Oct 19", gatesCleared = 9, skipsCount = 1, minutesSaved = 28, isPeak = false, sortOrder = 2),
        DailyStatEntity(dayOfWeek = "Wed", date = "Wed, Oct 20", gatesCleared = 14, skipsCount = 3, minutesSaved = 42, isPeak = false, sortOrder = 3),
        DailyStatEntity(dayOfWeek = "Thu", date = "Thu, Oct 21", gatesCleared = 22, skipsCount = 4, minutesSaved = 68, isPeak = true, sortOrder = 4),
        DailyStatEntity(dayOfWeek = "Fri", date = "Fri, Oct 22", gatesCleared = 15, skipsCount = 2, minutesSaved = 45, isPeak = false, sortOrder = 5),
        DailyStatEntity(dayOfWeek = "Sat", date = "Sat, Oct 23", gatesCleared = 10, skipsCount = 3, minutesSaved = 32, isPeak = false, sortOrder = 6),
        DailyStatEntity(dayOfWeek = "Sun", date = "Sun, Oct 24", gatesCleared = 8, skipsCount = 1, minutesSaved = 24, isPeak = false, sortOrder = 7)
      )
      db.dailyStatDao().insertAll(initialStats)
    }
  }

  fun toggleAppGated(
    packageName: String,
    isGated: Boolean,
    appName: String = "",
    category: String = "Social & Media"
  ) {
    scope.launch {
      val existing = db.gatedAppDao().getApp(packageName)
      if (existing == null && isGated) {
        db.gatedAppDao().insertOrUpdate(
          GatedAppEntity(
            packageName = packageName,
            appName = appName.ifEmpty { packageName.substringAfterLast('.') },
            category = category,
            isGated = true
          )
        )
      } else {
        db.gatedAppDao().setGated(packageName, isGated)
      }
    }
  }

  fun toggleCategoryGated(categoryTag: String, isGated: Boolean) {
    scope.launch {
      val all = db.gatedAppDao().getAllApps().first()
      val targetPackageNames = all.filter {
        CategoryTags.normalize(it.category).equals(categoryTag, ignoreCase = true) ||
          it.category.equals(categoryTag, ignoreCase = true)
      }.map { it.packageName }

      if (targetPackageNames.isNotEmpty()) {
        db.gatedAppDao().setGatedForPackages(targetPackageNames, isGated)
      }
    }
  }

  fun updateAppCategory(packageName: String, newCategory: String) {
    scope.launch {
      db.gatedAppDao().updateAppCategory(packageName, newCategory)
    }
  }

  fun updateApp(app: GatedAppEntity) {
    scope.launch {
      db.gatedAppDao().update(app)
    }
  }

  fun removeApp(packageName: String) {
    scope.launch {
      db.gatedAppDao().delete(packageName)
    }
  }

  fun addApp(app: GatedAppEntity) {
    scope.launch {
      db.gatedAppDao().insertOrUpdate(app)
    }
  }

  fun recordGateCleared(
    packageName: String,
    challengeType: String = "MATH",
    durationSeconds: Int = 18,
    stepsTaken: Int = 0
  ) {
    scope.launch {
      val now = System.currentTimeMillis()
      db.gatedAppDao().recordGateCleared(packageName, now)
      db.dailyStatDao().recordGateClearedForDay("Thu")

      val app = db.gatedAppDao().getApp(packageName)
      val appName = app?.appName ?: packageName.substringAfterLast('.')
      db.gateEventDao().insertEvent(
        com.example.data.model.GateEventEntity(
          packageName = packageName,
          appName = appName,
          timestamp = now,
          challengeType = challengeType,
          outcome = "CLEARED",
          durationSeconds = durationSeconds,
          stepsTaken = stepsTaken
        )
      )

      val newStreak = _currentStreak.value + 1
      _currentStreak.value = newStreak
      val msg = when {
        newStreak >= 7 -> "7-day streak reached! Your mindful friction pause is now an ingrained habit."
        newStreak >= 5 -> "You've cleared gates for 5 days in a row! Strong prefrontal focus."
        newStreak >= 3 -> "You've cleared gates for 3 days in a row! Keep up the deliberate pause."
        else -> "Streak started: You've cleared gates for $newStreak consecutive days!"
      }
      _milestoneMessage.value = msg
      prefs.edit()
        .putInt("current_streak", newStreak)
        .putString("milestone_message", msg)
        .apply()
    }
  }

  fun recordGateSkipped(packageName: String, challengeType: String = "MATH") {
    scope.launch {
      val now = System.currentTimeMillis()
      db.gatedAppDao().recordGateSkipped(packageName)
      db.dailyStatDao().recordSkipForDay("Thu")

      val app = db.gatedAppDao().getApp(packageName)
      val appName = app?.appName ?: packageName.substringAfterLast('.')
      db.gateEventDao().insertEvent(
        com.example.data.model.GateEventEntity(
          packageName = packageName,
          appName = appName,
          timestamp = now,
          challengeType = challengeType,
          outcome = "DEFLECTED",
          durationSeconds = 6
        )
      )
    }
  }

  fun setStepTarget(steps: Int) {
    _stepTarget.value = steps
    prefs.edit().putInt("step_target", steps).apply()
  }

  fun setOnboardingCompleted(completed: Boolean) {
    _isOnboardingCompleted.value = completed
    prefs.edit().putBoolean("onboarding_done", completed).apply()
  }

  fun setTactileFriction(enabled: Boolean) {
    _tactileFriction.value = enabled
    prefs.edit().putBoolean("tactile_friction", enabled).apply()
  }

  fun setEmergencyOverride(enabled: Boolean) {
    _emergencyOverrideEnabled.value = enabled
    prefs.edit().putBoolean("emergency_override", enabled).apply()
  }

  fun setDarkGlassTheme(isDark: Boolean) {
    _isDarkGlassTheme.value = isDark
    prefs.edit().putBoolean("dark_glass_theme", isDark).apply()
  }

  // --- RE-OPEN GRACE PERIOD ---
  fun setReopenGraceEnabled(enabled: Boolean) {
    _isReopenGraceEnabled.value = enabled
    prefs.edit().putBoolean("reopen_grace_enabled", enabled).apply()
  }

  fun setReopenGraceSeconds(seconds: Int) {
    _reopenGraceSeconds.value = seconds
    prefs.edit().putInt("reopen_grace_seconds", seconds).apply()
  }

  fun recordAppClosed(packageName: String, timestamp: Long = System.currentTimeMillis()) {
    lastAppClosedTimes[packageName] = timestamp
  }

  fun getLastAppClosedTime(packageName: String): Long {
    return lastAppClosedTimes[packageName] ?: 0L
  }

  // --- ZEN MODE ---
  fun isZenModeActive(): Boolean {
    return System.currentTimeMillis() < _zenModeActiveUntil.value
  }

  fun getZenRemainingMinutes(): Int {
    val remainingMillis = _zenModeActiveUntil.value - System.currentTimeMillis()
    if (remainingMillis <= 0L) return 0
    return ((remainingMillis / 60000L).toInt() + 1).coerceAtLeast(1)
  }

  fun saveApp(app: GatedAppEntity) = addApp(app)

  fun startZenMode(durationMinutes: Int = _zenModeDurationMinutes.value) {
    val targetUntil = System.currentTimeMillis() + (durationMinutes * 60 * 1000L)
    _zenModeActiveUntil.value = targetUntil
    _zenModeDurationMinutes.value = durationMinutes
    _isZenModeActiveFlow.value = true
    prefs.edit()
      .putLong("zen_mode_until", targetUntil)
      .putInt("zen_mode_duration_minutes", durationMinutes)
      .apply()
  }

  fun stopZenMode() {
    _zenModeActiveUntil.value = 0L
    _isZenModeActiveFlow.value = false
    prefs.edit().putLong("zen_mode_until", 0L).apply()
  }

  fun setZenModeDuration(durationMinutes: Int) {
    _zenModeDurationMinutes.value = durationMinutes
    prefs.edit().putInt("zen_mode_duration_minutes", durationMinutes).apply()
  }

  // --- ADAPTIVE INTELLIGENCE ---
  fun setAdaptiveGatesEnabled(enabled: Boolean) {
    _isAdaptiveGatesEnabled.value = enabled
    prefs.edit().putBoolean("adaptive_gates_enabled", enabled).apply()
  }

  suspend fun getAdaptiveChallenge(packageName: String, baseType: String): Pair<String, Int> {
    if (!_isAdaptiveGatesEnabled.value) return baseType to 2
    val score = adaptiveEngine.computeVulnerabilityScore(packageName)
    return adaptiveEngine.selectChallengeForScore(score, baseType)
  }

  suspend fun getAdaptiveIntelligenceStatus(): String {
    val count = db.gateEventDao().getEventCount()
    return if (count < AdaptiveChallengeEngine.MIN_SAMPLES * 2) {
      "Gatekeeper is learning your patterns — adaptive gates unlock in ${((AdaptiveChallengeEngine.MIN_SAMPLES * 2) - count).coerceAtLeast(1)} more events."
    } else {
      "Adaptive Intelligence Active: Optimizing friction based on your behavioral patterns."
    }
  }

  fun isCategoryDistracting(category: String): Boolean {
    val norm = CategoryTags.normalize(category)
    return norm == CategoryTags.SOCIAL || norm == CategoryTags.ENTERTAINMENT || norm == CategoryTags.GAMING
  }

  // --- DAILY LIMIT & SESSION LIMITS ---
  fun setAppDailyLimit(packageName: String, limitMinutes: Int) {
    scope.launch {
      db.gatedAppDao().updateDailyLimit(packageName, limitMinutes)
    }
  }

  fun setAppSessionLimit(packageName: String, sessionMinutes: Int) {
    scope.launch {
      db.gatedAppDao().updateSessionLimit(packageName, sessionMinutes)
    }
  }

  fun setAppDistracting(packageName: String, isDistracting: Boolean) {
    scope.launch {
      db.gatedAppDao().updateDistracting(packageName, isDistracting)
    }
  }

  fun setCategoryDailyLimit(category: String, limitMinutes: Int) {
    prefs.edit().putInt("cat_limit_${category}", limitMinutes).apply()
  }

  fun getCategoryDailyLimit(category: String): Int {
    val defaultLimit = when (CategoryTags.normalize(category)) {
      CategoryTags.SOCIAL -> 45
      CategoryTags.ENTERTAINMENT -> 60
      else -> 0
    }
    return prefs.getInt("cat_limit_${category}", defaultLimit)
  }

  fun getCategoryDailyLimitMinutes(category: String): Int = getCategoryDailyLimit(category)

  fun recordAppUsage(packageName: String, additionalMinutes: Int) {
    scope.launch {
      db.gatedAppDao().addUsageMinutes(packageName, additionalMinutes)
    }
  }

  fun updateMathConfig(config: MathChallengeConfig) {
    _mathConfig.value = config
    prefs.edit()
      .putString("math_tier", config.difficultyTier)
      .putBoolean("math_randomize", config.randomizeOnFailedEntry)
      .putBoolean("math_timeout", config.inputTimeoutCountdown)
      .putBoolean("math_escalate", config.progressiveEscalation)
      .putBoolean("math_add_sub", config.permittedAdditionSubtraction)
      .putBoolean("math_mult", config.permittedMultiplication)
      .putBoolean("math_seq", config.permittedSequencePatterns)
      .putString("math_override_code", config.emergencyOverrideCode)
      .putInt("math_predefined_level", config.predefinedDifficultyLevel)
      .putString("math_problem_type", config.problemSolvingType)
      .apply()
  }

  private fun loadMathConfig(): MathChallengeConfig {
    return MathChallengeConfig(
      difficultyTier = prefs.getString("math_tier", "STANDARD") ?: "STANDARD",
      randomizeOnFailedEntry = prefs.getBoolean("math_randomize", true),
      inputTimeoutCountdown = prefs.getBoolean("math_timeout", false),
      progressiveEscalation = prefs.getBoolean("math_escalate", true),
      permittedAdditionSubtraction = prefs.getBoolean("math_add_sub", true),
      permittedMultiplication = prefs.getBoolean("math_mult", false),
      permittedSequencePatterns = prefs.getBoolean("math_seq", false),
      emergencyOverrideCode = prefs.getString("math_override_code", "1234") ?: "1234",
      predefinedDifficultyLevel = prefs.getInt("math_predefined_level", 2),
      problemSolvingType = prefs.getString("math_problem_type", "ANY") ?: "ANY"
    )
  }

  suspend fun isPackageGatedAndLocked(packageName: String): Boolean {
    val app = db.gatedAppDao().getApp(packageName) ?: return false
    if (!app.isGated) return false

    val now = System.currentTimeMillis()

    // 1. Zen Mode Rule: Distracting apps are blocked entirely without challenge bypass
    if (isZenModeActive() && (app.isDistracting || isCategoryDistracting(app.category))) {
      return true
    }

    // 2. Re-Open Grace Period: if app was closed less than reopenGraceSeconds ago, allow immediate re-entry
    if (_isReopenGraceEnabled.value) {
      val lastClosed = lastAppClosedTimes[packageName] ?: 0L
      if (lastClosed > 0L && (now - lastClosed) < (_reopenGraceSeconds.value * 1000L)) {
        return false // Do NOT trigger gate during quick re-open grace
      }
    }

    // 3. Daily Limit Rule: If daily duration reached/exceeded, gate triggers on EVERY subsequent launch that day
    val dailyLimit = app.dailyLimitMinutes
    val categoryDailyLimit = getCategoryDailyLimit(app.category)
    val isAppDailyLimitExceeded = dailyLimit > 0 && app.todayUsedMinutes >= dailyLimit
    val isCategoryDailyLimitExceeded = categoryDailyLimit > 0 && app.todayUsedMinutes >= categoryDailyLimit

    if (isAppDailyLimitExceeded || isCategoryDailyLimitExceeded) {
      // Must trigger gate for every launch today
      return true
    }

    // 4. Session Limit & Grace Window: User has an active unlocked session window
    val effectiveSessionLimitMinutes = if (app.sessionLimitMinutes > 0) app.sessionLimitMinutes else app.graceWindowMinutes
    val sessionDurationMillis = effectiveSessionLimitMinutes * 60 * 1000L
    val isWithinSession = (now - app.lastUnlockedTimestamp) < sessionDurationMillis
    return !isWithinSession
  }
}
