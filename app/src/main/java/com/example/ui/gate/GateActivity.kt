package com.example.ui.gate

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.data.AppDatabase
import com.example.data.GatekeeperRepository
import com.example.ui.screens.GateOverlayBreathingContent
import com.example.ui.screens.GateOverlayHoldingContent
import com.example.ui.screens.GateOverlayMathContent
import com.example.ui.screens.GateOverlayPromptContent
import com.example.ui.screens.GateOverlayStepContent
import com.example.ui.theme.MyApplicationTheme

class GateActivity : ComponentActivity() {

  companion object {
    const val EXTRA_PACKAGE_NAME = "extra_package_name"
    const val EXTRA_APP_NAME = "extra_app_name"
    const val EXTRA_CHALLENGE_TYPE = "extra_challenge_type"
    const val EXTRA_ADAPTIVE_DIFFICULTY = "extra_adaptive_difficulty"
    const val EXTRA_ADAPTIVE_STEPS = "extra_adaptive_steps"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME) ?: "com.instagram.android"
    val appName = intent.getStringExtra(EXTRA_APP_NAME) ?: "Instagram"
    val initialChallengeType = intent.getStringExtra(EXTRA_CHALLENGE_TYPE) ?: "MATH"
    val adaptiveDifficulty = intent.getIntExtra(EXTRA_ADAPTIVE_DIFFICULTY, -1)
    val adaptiveSteps = intent.getIntExtra(EXTRA_ADAPTIVE_STEPS, -1)

    val repository = GatekeeperRepository(AppDatabase.getDatabase(this), this)

    setContent {
      MyApplicationTheme(darkTheme = true) {
        var activeChallenge by remember { mutableStateOf(initialChallengeType.uppercase()) }

        val isZenActive = repository.isZenModeActive()
        val isDistracting = initialChallengeType == "ZEN"

        Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
          if (isZenActive && (isDistracting || activeChallenge == "ZEN")) {
            GateOverlayZenContent(
              appName = appName,
              remainingMinutes = repository.getZenRemainingMinutes(),
              onReturnHome = {
                repository.recordGateSkipped(packageName, challengeType = "ZEN")
                dismissToHome()
              }
            )
          } else {
            when (activeChallenge) {
              "BREATHING" -> {
              GateOverlayBreathingContent(
                appName = appName,
                totalSeconds = 60,
                onUnlock = {
                  repository.recordGateCleared(
                    packageName,
                    challengeType = "BREATHING",
                    durationSeconds = 60
                  )
                  openGatedAppAndFinish(packageName, appName)
                },
                onDismiss = {
                  repository.recordGateSkipped(packageName, challengeType = "BREATHING")
                  dismissToHome()
                },
                onSwitchToSteps = { activeChallenge = "STEPS" },
                onSwitchToMath = { activeChallenge = "MATH" }
              )
            }
            "STEPS" -> {
              GateOverlayStepContent(
                appName = appName,
                targetSteps = if (adaptiveSteps > 0) adaptiveSteps else repository.stepTarget.value,
                onUnlock = {
                  repository.recordGateCleared(
                    packageName,
                    challengeType = "STEPS",
                    stepsTaken = if (adaptiveSteps > 0) adaptiveSteps else repository.stepTarget.value
                  )
                  openGatedAppAndFinish(packageName, appName)
                },
                onDismiss = {
                  repository.recordGateSkipped(packageName, challengeType = "STEPS")
                  dismissToHome()
                },
                onSwitchToBreathing = { activeChallenge = "BREATHING" },
                onSwitchToMath = { activeChallenge = "MATH" }
              )
            }
            "HOLDING" -> {
              GateOverlayHoldingContent(
                appName = appName,
                targetHoldSeconds = 5.0f,
                onUnlock = {
                  repository.recordGateCleared(
                    packageName,
                    challengeType = "HOLDING",
                    durationSeconds = 5
                  )
                  openGatedAppAndFinish(packageName, appName)
                },
                onDismiss = {
                  repository.recordGateSkipped(packageName, challengeType = "HOLDING")
                  dismissToHome()
                },
                onSwitchToBreathing = { activeChallenge = "BREATHING" },
                onSwitchToMath = { activeChallenge = "MATH" }
              )
            }
            "PROMPT" -> {
              GateOverlayPromptContent(
                appName = appName,
                onUnlock = {
                  repository.recordGateCleared(
                    packageName,
                    challengeType = "PROMPT",
                    durationSeconds = 12
                  )
                  openGatedAppAndFinish(packageName, appName)
                },
                onDismiss = {
                  repository.recordGateSkipped(packageName, challengeType = "PROMPT")
                  dismissToHome()
                },
                onSwitchToBreathing = { activeChallenge = "BREATHING" },
                onSwitchToMath = { activeChallenge = "MATH" }
              )
            }
            else -> {
              GateOverlayMathContent(
                appName = appName,
                initialDifficultyLevel = if (adaptiveDifficulty > 0) adaptiveDifficulty else (if (activeChallenge == "PROBLEM_SOLVING") 4 else 2),
                onUnlock = {
                  repository.recordGateCleared(
                    packageName,
                    challengeType = if (activeChallenge == "PROBLEM_SOLVING") "PROBLEM_SOLVING" else "MATH",
                    durationSeconds = 18
                  )
                  openGatedAppAndFinish(packageName, appName)
                },
                onDismiss = {
                  repository.recordGateSkipped(packageName, challengeType = if (activeChallenge == "PROBLEM_SOLVING") "PROBLEM_SOLVING" else "MATH")
                  dismissToHome()
                },
                onSwitchToBreathing = { activeChallenge = "BREATHING" },
                onSwitchToSteps = { activeChallenge = "STEPS" }
              )
            }
          }
        }
      }
    }
  }
}

  private fun openGatedAppAndFinish(packageName: String, appName: String) {
    val pm = packageManager
    val launchIntent = pm.getLaunchIntentForPackage(packageName)
    if (launchIntent != null) {
      launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
      startActivity(launchIntent)
    } else {
      Toast.makeText(
        this,
        "Gate cleared! Opening $appName ($packageName)",
        Toast.LENGTH_LONG
      ).show()
    }
    finish()
  }

  private fun dismissToHome() {
    val homeIntent = Intent(Intent.ACTION_MAIN).apply {
      addCategory(Intent.CATEGORY_HOME)
      flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(homeIntent)
    finish()
  }
}
