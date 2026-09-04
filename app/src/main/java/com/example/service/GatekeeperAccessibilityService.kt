package com.example.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.example.data.AppDatabase
import com.example.data.GatekeeperRepository
import com.example.ui.gate.GateActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GatekeeperAccessibilityService : AccessibilityService() {
  private val scope = CoroutineScope(Dispatchers.IO)
  private lateinit var repository: GatekeeperRepository
  private var currentForegroundPackage: String? = null

  override fun onCreate() {
    super.onCreate()
    val db = AppDatabase.getDatabase(this)
    repository = GatekeeperRepository(db, this)
  }

  override fun onAccessibilityEvent(event: AccessibilityEvent?) {
    if (event == null || event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return
    val packageName = event.packageName?.toString() ?: return

    // Track app closing/switching transitions for Re-Open Grace Period
    val previous = currentForegroundPackage
    if (previous != null && previous != packageName && previous != this.packageName) {
      repository.recordAppClosed(previous)
    }
    if (packageName != this.packageName) {
      currentForegroundPackage = packageName
    }

    // Don't intercept own package
    if (packageName == this.packageName) return

    scope.launch {
      if (repository.isPackageGatedAndLocked(packageName)) {
        val app = AppDatabase.getDatabase(applicationContext).gatedAppDao().getApp(packageName)
        val isZenActive = repository.isZenModeActive()
        val isDistracting = app?.isDistracting == true || repository.isCategoryDistracting(app?.category ?: "")
        
        val baseChallengeType = app?.challengeType ?: "MATH"
        val (challengeType, difficulty) = repository.getAdaptiveChallenge(packageName, baseChallengeType)
        
        val finalChallengeType = if (isZenActive && isDistracting) "ZEN" else challengeType

        val intent = Intent(applicationContext, GateActivity::class.java).apply {
          addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
          putExtra(GateActivity.EXTRA_PACKAGE_NAME, packageName)
          putExtra(GateActivity.EXTRA_APP_NAME, app?.appName ?: packageName)
          putExtra(GateActivity.EXTRA_CHALLENGE_TYPE, finalChallengeType)
          
          if (finalChallengeType == "MATH" || finalChallengeType == "PROBLEM_SOLVING") {
            putExtra(GateActivity.EXTRA_ADAPTIVE_DIFFICULTY, difficulty)
          } else if (finalChallengeType == "STEPS") {
            putExtra(GateActivity.EXTRA_ADAPTIVE_STEPS, difficulty) // steps uses difficulty field for count in selectChallengeForScore
          }
        }
        startActivity(intent)
      }
    }
  }

  override fun onInterrupt() {
    // Service interrupted
  }
}
