package com.example.ui

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GatekeeperRepository
import com.example.data.model.GatedAppEntity
import com.example.ui.components.GlassCard
import com.example.ui.components.PulsingBeacon
import com.example.ui.gate.GateActivity
import com.example.ui.gate.GateOverlayZenContent
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.GateOverlayBreathingContent
import com.example.ui.screens.GateOverlayHoldingContent
import com.example.ui.screens.GateOverlayMathContent
import com.example.ui.screens.GateOverlayPromptContent
import com.example.ui.screens.GateOverlayStepContent
import com.example.ui.screens.MathChallengeSettingsScreen
import com.example.ui.screens.OnboardingAccessibilityScreen
import com.example.ui.screens.OnboardingAppPickerScreen
import com.example.ui.screens.OnboardingFramingScreen
import com.example.ui.screens.OnboardingMovementPermissionScreen
import com.example.ui.screens.OnboardingOverlayScreen
import com.example.ui.screens.SecurityGateModalScreen
import com.example.ui.screens.StatsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.BorderDefault
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CalmMoss
import com.example.ui.theme.CanvasBase
import com.example.ui.theme.ClayLock
import com.example.ui.theme.GlassBase
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

sealed class ScreenRoute(val title: String) {
  object OnboardingFraming : ScreenRoute("1. Onboarding: Framing")
  object OnboardingAccessibility : ScreenRoute("Onboarding: Accessibility")
  object OnboardingOverlay : ScreenRoute("4. Onboarding: Overlay Permission")
  object OnboardingMovement : ScreenRoute("Onboarding: Movement (Steps)")
  object OnboardingAppPicker : ScreenRoute("Onboarding: App Picker")
  object Dashboard : ScreenRoute("6. Dashboard (Dark Glass)")
  object MathChallengeSettings : ScreenRoute("2. Math Challenge Settings")
  object Stats : ScreenRoute("3. Stats (Dark Glass)")
  object Settings : ScreenRoute("7. Settings")
  object GateOverlayMath : ScreenRoute("5. Gate Overlay: Math")
  object GateOverlayBreathing : ScreenRoute("Gate Overlay: Breathing (1-Min)")
  object GateOverlayStep : ScreenRoute("Gate Overlay: Step Counter")
  object GateOverlayHolding : ScreenRoute("Gate Overlay: Holding Pause")
  object GateOverlayPrompt : ScreenRoute("Gate Overlay: Reflective Prompt")
  object GateOverlayZen : ScreenRoute("Gate Overlay: Zen Mode Sanctuary")
  object SecurityGateModal : ScreenRoute("Security Gate Modal")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation(
  repository: GatekeeperRepository
) {
  val apps by repository.allApps.collectAsState(initial = emptyList())
  val weeklyStats by repository.weeklyStats.collectAsState(initial = emptyList())
  val currentStreak by repository.currentStreak.collectAsState()
  val milestoneMessage by repository.milestoneMessage.collectAsState()
  val stepTarget by repository.stepTarget.collectAsState()
  val mathConfig by repository.mathConfig.collectAsState()
  val isOnboardingDone by repository.isOnboardingCompleted.collectAsState()
  val tactileFriction by repository.tactileFriction.collectAsState()
  val emergencyOverride by repository.emergencyOverrideEnabled.collectAsState()
  val isDarkGlassTheme by repository.isDarkGlassTheme.collectAsState()
  val isReopenGraceEnabled by repository.isReopenGraceEnabled.collectAsState()
  val reopenGraceSeconds by repository.reopenGraceSeconds.collectAsState()
  val isZenModeActive by repository.isZenModeActiveFlow.collectAsState()
  val zenModeDurationMinutes by repository.zenModeDurationMinutes.collectAsState()

  val context = androidx.compose.ui.platform.LocalContext.current
  val appPickerViewModel = remember(repository) {
    com.example.ui.viewmodel.AppPickerViewModel(repository, context)
  }

  // Current active screen route
  var currentRoute by remember {
    mutableStateOf<ScreenRoute>(
      if (isOnboardingDone) ScreenRoute.Dashboard else ScreenRoute.OnboardingFraming
    )
  }

  var selectedAppForModal by remember {
    mutableStateOf<GatedAppEntity?>(null)
  }

  var activeTestApp by remember {
    mutableStateOf<GatedAppEntity?>(null)
  }

  fun launchTargetApp(packageName: String, appName: String) {
    val pm = context.packageManager
    val launchIntent = pm.getLaunchIntentForPackage(packageName)
    if (launchIntent != null) {
      launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
      context.startActivity(launchIntent)
    } else {
      Toast.makeText(
        context,
        "Gate cleared! Opening $appName ($packageName)",
        Toast.LENGTH_LONG
      ).show()
    }
  }

  var showPrototypeSwitcher by remember { mutableStateOf(false) }
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  val showBottomBar = currentRoute is ScreenRoute.Dashboard ||
    currentRoute is ScreenRoute.Stats ||
    currentRoute is ScreenRoute.Settings

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(CanvasBase)
  ) {
    Scaffold(
      modifier = Modifier.fillMaxSize(),
      containerColor = CanvasBase,
      bottomBar = {
        if (showBottomBar) {
          BottomGlassNav(
            currentRoute = currentRoute,
            onSelectRoute = { currentRoute = it }
          )
        }
      }
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(
            top = innerPadding.calculateTopPadding(),
            bottom = if (showBottomBar) 64.dp else innerPadding.calculateBottomPadding()
          )
      ) {
        AnimatedContent(
          targetState = currentRoute,
          transitionSpec = { fadeIn() togetherWith fadeOut() },
          label = "screen_transition"
        ) { target ->
          when (target) {
            is ScreenRoute.OnboardingFraming -> {
              OnboardingFramingScreen(
                onContinue = { currentRoute = ScreenRoute.OnboardingAccessibility }
              )
            }
            is ScreenRoute.OnboardingAccessibility -> {
              OnboardingAccessibilityScreen(
                onBack = { currentRoute = ScreenRoute.OnboardingFraming },
                onContinue = { currentRoute = ScreenRoute.OnboardingOverlay }
              )
            }
            is ScreenRoute.OnboardingOverlay -> {
              OnboardingOverlayScreen(
                onBack = { currentRoute = ScreenRoute.OnboardingAccessibility },
                onGranted = { currentRoute = ScreenRoute.OnboardingMovement },
                onSkip = { currentRoute = ScreenRoute.OnboardingMovement }
              )
            }
            is ScreenRoute.OnboardingMovement -> {
              OnboardingMovementPermissionScreen(
                onBack = { currentRoute = ScreenRoute.OnboardingOverlay },
                onContinue = { currentRoute = ScreenRoute.OnboardingAppPicker },
                onSkip = { currentRoute = ScreenRoute.OnboardingAppPicker }
              )
            }
            is ScreenRoute.OnboardingAppPicker -> {
              OnboardingAppPickerScreen(
                apps = apps,
                onToggleApp = { pkg, gated -> repository.toggleAppGated(pkg, gated) },
                onToggleCategory = { cat, gated -> repository.toggleCategoryGated(cat, gated) },
                onBack = { currentRoute = ScreenRoute.OnboardingMovement },
                onFinish = {
                  repository.setOnboardingCompleted(true)
                  currentRoute = ScreenRoute.Dashboard
                },
                appPickerViewModel = appPickerViewModel
              )
            }
            is ScreenRoute.Dashboard -> {
              val isAdaptiveGatesEnabled by repository.isAdaptiveGatesEnabled.collectAsStateWithLifecycle()
              var adaptiveStatus by remember { mutableStateOf("") }
              LaunchedEffect(Unit) {
                adaptiveStatus = repository.getAdaptiveIntelligenceStatus()
              }
              
              DashboardScreen(
                apps = apps,
                weeklyStats = weeklyStats,
                streakDays = currentStreak,
                milestoneMessage = milestoneMessage,
                onToggleAppGated = { pkg, gated -> repository.toggleAppGated(pkg, gated) },
                onToggleCategoryGated = { cat, gated -> repository.toggleCategoryGated(cat, gated) },
                isZenModeActive = isZenModeActive,
                zenModeRemainingMinutes = repository.getZenRemainingMinutes(),
                isAdaptiveGatesEnabled = isAdaptiveGatesEnabled,
                adaptiveStatus = adaptiveStatus,
                onOpenAppDetails = { app ->
                  selectedAppForModal = app
                  currentRoute = ScreenRoute.SecurityGateModal
                },
                onNavigateToMathCalibration = { currentRoute = ScreenRoute.MathChallengeSettings },
                onNavigateToStats = { currentRoute = ScreenRoute.Stats },
                onNavigateToSettings = { currentRoute = ScreenRoute.Settings },
                onSimulateMathGate = { pkg ->
                  val target = apps.find { it.packageName == pkg } ?: GatedAppEntity(packageName = pkg, appName = "Instagram", challengeType = "MATH")
                  activeTestApp = target
                  currentRoute = ScreenRoute.GateOverlayMath
                },
                onSimulateBreathingGate = { pkg ->
                  val target = apps.find { it.packageName == pkg } ?: GatedAppEntity(packageName = pkg, appName = "TikTok", challengeType = "BREATHING")
                  activeTestApp = target
                  currentRoute = ScreenRoute.GateOverlayBreathing
                },
                onSimulateStepGate = { pkg ->
                  val target = apps.find { it.packageName == pkg } ?: GatedAppEntity(packageName = pkg, appName = "Twitter", challengeType = "STEPS")
                  activeTestApp = target
                  currentRoute = ScreenRoute.GateOverlayStep
                },
                onSimulateHoldingGate = { pkg ->
                  val target = apps.find { it.packageName == pkg } ?: GatedAppEntity(packageName = pkg, appName = "Reddit", challengeType = "HOLDING")
                  activeTestApp = target
                  currentRoute = ScreenRoute.GateOverlayHolding
                },
                onSimulatePromptGate = { pkg ->
                  val target = apps.find { it.packageName == pkg } ?: GatedAppEntity(packageName = pkg, appName = "YouTube", challengeType = "PROMPT")
                  activeTestApp = target
                  currentRoute = ScreenRoute.GateOverlayPrompt
                },
                onAddNewApp = { newApp ->
                  repository.saveApp(newApp)
                },
                onOpenPrototypeSwitcher = { showPrototypeSwitcher = true }
              )
            }
            is ScreenRoute.MathChallengeSettings -> {
              MathChallengeSettingsScreen(
                currentConfig = mathConfig,
                onSaveConfig = { updated -> repository.updateMathConfig(updated) },
                onBack = { currentRoute = ScreenRoute.Dashboard }
              )
            }
            is ScreenRoute.Stats -> {
              StatsScreen(
                weeklyStats = weeklyStats,
                streakDays = currentStreak,
                milestoneMessage = milestoneMessage,
                onOpenAppDetails = { pkg ->
                  val app = apps.find { it.packageName == pkg } ?: apps.firstOrNull()
                  if (app != null) {
                    selectedAppForModal = app
                    currentRoute = ScreenRoute.SecurityGateModal
                  }
                }
              )
            }
            is ScreenRoute.Settings -> {
              val isAdaptiveGatesEnabled by repository.isAdaptiveGatesEnabled.collectAsStateWithLifecycle()
              var adaptiveStatus by remember { mutableStateOf("Learning patterns...") }
              LaunchedEffect(Unit) {
                adaptiveStatus = repository.getAdaptiveIntelligenceStatus()
              }
              
              SettingsScreen(
                tactileFriction = tactileFriction,
                emergencyOverride = emergencyOverride,
                isDarkGlassTheme = isDarkGlassTheme,
                isReopenGraceEnabled = isReopenGraceEnabled,
                reopenGraceSeconds = reopenGraceSeconds,
                isZenModeActive = isZenModeActive,
                zenModeDurationMinutes = zenModeDurationMinutes,
                zenModeRemainingMinutes = repository.getZenRemainingMinutes(),
                isAdaptiveGatesEnabled = isAdaptiveGatesEnabled,
                adaptiveIntelligenceStatus = adaptiveStatus,
                socialCategoryLimitMinutes = repository.getCategoryDailyLimitMinutes("Social"),
                entertainmentCategoryLimitMinutes = repository.getCategoryDailyLimitMinutes("Entertainment"),
                onToggleTactileFriction = { repository.setTactileFriction(it) },
                onToggleEmergencyOverride = { repository.setEmergencyOverride(it) },
                onToggleTheme = { repository.setDarkGlassTheme(it) },
                onToggleReopenGrace = { repository.setReopenGraceEnabled(it) },
                onSetReopenGraceSeconds = { repository.setReopenGraceSeconds(it) },
                onToggleZenMode = { active, mins ->
                  if (active) repository.startZenMode(mins) else repository.stopZenMode()
                },
                onSetZenModeDuration = { repository.setZenModeDuration(it) },
                onToggleAdaptiveGates = { repository.setAdaptiveGatesEnabled(it) },
                onSetCategoryDailyLimit = { cat, mins -> repository.setCategoryDailyLimit(cat, mins) },
                onNavigateToMathCalibration = { currentRoute = ScreenRoute.MathChallengeSettings }
              )
            }
            is ScreenRoute.GateOverlayZen -> {
              val currentApp = activeTestApp ?: selectedAppForModal ?: GatedAppEntity(
                packageName = "com.instagram.android",
                appName = "Instagram",
                challengeType = "ZEN"
              )
              GateOverlayZenContent(
                appName = currentApp.appName,
                remainingMinutes = repository.getZenRemainingMinutes(),
                onReturnHome = {
                  currentRoute = ScreenRoute.Dashboard
                }
              )
            }
            is ScreenRoute.GateOverlayMath -> {
              val currentApp = activeTestApp ?: selectedAppForModal ?: GatedAppEntity(
                packageName = "com.instagram.android",
                appName = "Instagram",
                challengeType = "MATH"
              )
              GateOverlayMathContent(
                appName = currentApp.appName,
                onUnlock = {
                  repository.recordGateCleared(currentApp.packageName, challengeType = "MATH")
                  launchTargetApp(currentApp.packageName, currentApp.appName)
                  currentRoute = ScreenRoute.Dashboard
                },
                onDismiss = {
                  repository.recordGateSkipped(currentApp.packageName, challengeType = "MATH")
                  currentRoute = ScreenRoute.Dashboard
                },
                onSwitchToBreathing = { currentRoute = ScreenRoute.GateOverlayBreathing },
                onSwitchToSteps = { currentRoute = ScreenRoute.GateOverlayStep }
              )
            }
            is ScreenRoute.GateOverlayBreathing -> {
              val currentApp = activeTestApp ?: selectedAppForModal ?: GatedAppEntity(
                packageName = "com.zhiliaoapp.musically",
                appName = "TikTok",
                challengeType = "BREATHING"
              )
              GateOverlayBreathingContent(
                appName = currentApp.appName,
                totalSeconds = 60,
                onUnlock = {
                  repository.recordGateCleared(currentApp.packageName, challengeType = "BREATHING", durationSeconds = 60)
                  launchTargetApp(currentApp.packageName, currentApp.appName)
                  currentRoute = ScreenRoute.Dashboard
                },
                onDismiss = {
                  repository.recordGateSkipped(currentApp.packageName, challengeType = "BREATHING")
                  currentRoute = ScreenRoute.Dashboard
                },
                onSwitchToSteps = { currentRoute = ScreenRoute.GateOverlayStep },
                onSwitchToMath = { currentRoute = ScreenRoute.GateOverlayMath }
              )
            }
            is ScreenRoute.GateOverlayStep -> {
              val currentApp = activeTestApp ?: selectedAppForModal ?: GatedAppEntity(
                packageName = "com.twitter.android",
                appName = "Twitter",
                challengeType = "STEPS"
              )
              GateOverlayStepContent(
                appName = currentApp.appName,
                targetSteps = stepTarget,
                onUnlock = {
                  repository.recordGateCleared(currentApp.packageName, challengeType = "STEPS", stepsTaken = stepTarget)
                  launchTargetApp(currentApp.packageName, currentApp.appName)
                  currentRoute = ScreenRoute.Dashboard
                },
                onDismiss = {
                  repository.recordGateSkipped(currentApp.packageName, challengeType = "STEPS")
                  currentRoute = ScreenRoute.Dashboard
                },
                onSwitchToBreathing = { currentRoute = ScreenRoute.GateOverlayBreathing },
                onSwitchToMath = { currentRoute = ScreenRoute.GateOverlayMath }
              )
            }
            is ScreenRoute.GateOverlayHolding -> {
              val currentApp = activeTestApp ?: selectedAppForModal ?: GatedAppEntity(
                packageName = "com.reddit.frontpage",
                appName = "Reddit",
                challengeType = "HOLDING"
              )
              GateOverlayHoldingContent(
                appName = currentApp.appName,
                targetHoldSeconds = 5.0f,
                onUnlock = {
                  repository.recordGateCleared(currentApp.packageName, challengeType = "HOLDING", durationSeconds = 5)
                  launchTargetApp(currentApp.packageName, currentApp.appName)
                  currentRoute = ScreenRoute.Dashboard
                },
                onDismiss = {
                  repository.recordGateSkipped(currentApp.packageName, challengeType = "HOLDING")
                  currentRoute = ScreenRoute.Dashboard
                },
                onSwitchToBreathing = { currentRoute = ScreenRoute.GateOverlayBreathing },
                onSwitchToMath = { currentRoute = ScreenRoute.GateOverlayMath }
              )
            }
            is ScreenRoute.GateOverlayPrompt -> {
              val currentApp = activeTestApp ?: selectedAppForModal ?: GatedAppEntity(
                packageName = "com.google.android.youtube",
                appName = "YouTube",
                challengeType = "PROMPT"
              )
              GateOverlayPromptContent(
                appName = currentApp.appName,
                onUnlock = {
                  repository.recordGateCleared(currentApp.packageName, challengeType = "PROMPT", durationSeconds = 12)
                  launchTargetApp(currentApp.packageName, currentApp.appName)
                  currentRoute = ScreenRoute.Dashboard
                },
                onDismiss = {
                  repository.recordGateSkipped(currentApp.packageName, challengeType = "PROMPT")
                  currentRoute = ScreenRoute.Dashboard
                },
                onSwitchToBreathing = { currentRoute = ScreenRoute.GateOverlayBreathing },
                onSwitchToMath = { currentRoute = ScreenRoute.GateOverlayMath }
              )
            }
            is ScreenRoute.SecurityGateModal -> {
              val appToDisplay = selectedAppForModal ?: apps.firstOrNull() ?: GatedAppEntity(
                packageName = "com.instagram.android",
                appName = "Instagram"
              )
              SecurityGateModalScreen(
                app = appToDisplay,
                onSave = { updated -> repository.updateApp(updated) },
                onDelete = { pkg -> repository.removeApp(pkg) },
                onTestGate = { testedApp ->
                  activeTestApp = testedApp
                  val intent = Intent(context, GateActivity::class.java).apply {
                    putExtra(GateActivity.EXTRA_PACKAGE_NAME, testedApp.packageName)
                    putExtra(GateActivity.EXTRA_APP_NAME, testedApp.appName)
                    putExtra(GateActivity.EXTRA_CHALLENGE_TYPE, testedApp.challengeType)
                  }
                  try {
                    context.startActivity(intent)
                  } catch (_: Throwable) {}

                  when (testedApp.challengeType.uppercase()) {
                    "BREATHING" -> currentRoute = ScreenRoute.GateOverlayBreathing
                    "STEPS" -> currentRoute = ScreenRoute.GateOverlayStep
                    "HOLDING" -> currentRoute = ScreenRoute.GateOverlayHolding
                    "PROMPT" -> currentRoute = ScreenRoute.GateOverlayPrompt
                    else -> currentRoute = ScreenRoute.GateOverlayMath
                  }
                },
                onClose = { currentRoute = ScreenRoute.Dashboard }
              )
            }
          }
        }
      }
    }

    // Prototype Flows Switcher BottomSheet
    if (showPrototypeSwitcher) {
      ModalBottomSheet(
        onDismissRequest = { showPrototypeSwitcher = false },
        sheetState = sheetState,
        containerColor = SurfaceContainerHigh,
        contentColor = TextPrimary
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .navigationBarsPadding()
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Prototype Screen Navigator",
                color = TextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
              )
              Text(
                text = "Jump directly to any spec flow or screen",
                color = TextSecondary,
                fontSize = 12.sp
              )
            }
            IconButton(onClick = { showPrototypeSwitcher = false }) {
              Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          val flowItems = listOf(
            Triple(ScreenRoute.OnboardingFraming, "1. Onboarding: Framing (Dark Glass)", Icons.Default.Shield),
            Triple(ScreenRoute.MathChallengeSettings, "2. Math Challenge Settings", Icons.Default.Calculate),
            Triple(ScreenRoute.Stats, "3. Stats (Dark Glass)", Icons.Default.BarChart),
            Triple(ScreenRoute.OnboardingOverlay, "4. Onboarding: Overlay Permission (Dark Glass)", Icons.Default.Layers),
            Triple(ScreenRoute.GateOverlayMath, "5. Gate Overlay: Math (Dark Glass)", Icons.Default.Lock),
            Triple(ScreenRoute.Dashboard, "6. Dashboard (Dark Glass)", Icons.Default.Dashboard),
            Triple(ScreenRoute.Settings, "7. Settings", Icons.Default.Settings),
            Triple(ScreenRoute.OnboardingAccessibility, "• Onboarding: Accessibility Permission", Icons.Default.Security),
            Triple(ScreenRoute.OnboardingMovement, "• Onboarding: Movement Permission", Icons.Default.DirectionsWalk),
            Triple(ScreenRoute.OnboardingAppPicker, "• Onboarding: App Picker", Icons.Default.TouchApp),
            Triple(ScreenRoute.GateOverlayBreathing, "• Gate Overlay: Breathing Pacer", Icons.Default.Air),
            Triple(ScreenRoute.GateOverlayStep, "• Gate Overlay: Physical Steps", Icons.Default.DirectionsWalk),
            Triple(ScreenRoute.GateOverlayHolding, "• Gate Overlay: Holding Pause (5s)", Icons.Default.TouchApp),
            Triple(ScreenRoute.GateOverlayPrompt, "• Gate Overlay: Reflective Prompt", Icons.Default.EditNote),
            Triple(ScreenRoute.GateOverlayZen, "• Gate Overlay: Zen Mode Sanctuary", Icons.Default.SelfImprovement),
            Triple(ScreenRoute.SecurityGateModal, "• Security Gate Modal (App Details)", Icons.Default.Shield)
          )

          LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            items(flowItems) { (route, label, icon) ->
              val isSelected = currentRoute == route
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(12.dp))
                  .background(if (isSelected) PrimaryIndigo.copy(alpha = 0.18f) else SurfaceContainerLow)
                  .border(1.dp, if (isSelected) PrimaryIndigo else BorderSubtle, RoundedCornerShape(12.dp))
                  .clickable {
                    currentRoute = route
                    showPrototypeSwitcher = false
                  }
                  .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = icon,
                  contentDescription = null,
                  tint = if (isSelected) PrimaryIndigo else TextSecondary,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                  text = label,
                  color = if (isSelected) PrimaryIndigo else TextPrimary,
                  fontSize = 13.sp,
                  fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                )
              }
            }
          }
          Spacer(modifier = Modifier.height(16.dp))
        }
      }
    }
  }
}

@Composable
private fun BottomGlassNav(
  currentRoute: ScreenRoute,
  onSelectRoute: (ScreenRoute) -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color(0xE6121317))
      .border(1.dp, BorderSubtle, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
      .padding(horizontal = 24.dp, vertical = 8.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      BottomNavItem(
        icon = Icons.Default.Dashboard,
        label = "Dashboard",
        isSelected = currentRoute is ScreenRoute.Dashboard,
        onClick = { onSelectRoute(ScreenRoute.Dashboard) }
      )

      BottomNavItem(
        icon = Icons.Default.BarChart,
        label = "Stats",
        isSelected = currentRoute is ScreenRoute.Stats,
        onClick = { onSelectRoute(ScreenRoute.Stats) }
      )

      BottomNavItem(
        icon = Icons.Default.Settings,
        label = "Settings",
        isSelected = currentRoute is ScreenRoute.Settings,
        onClick = { onSelectRoute(ScreenRoute.Settings) }
      )
    }
  }
}

@Composable
private fun BottomNavItem(
  icon: ImageVector,
  label: String,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .clip(RoundedCornerShape(12.dp))
      .clickable { onClick() }
      .padding(horizontal = 16.dp, vertical = 4.dp)
  ) {
    Icon(
      imageVector = icon,
      contentDescription = label,
      tint = if (isSelected) PrimaryIndigo else TextMuted,
      modifier = Modifier.size(22.dp)
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = label,
      color = if (isSelected) PrimaryIndigo else TextMuted,
      fontSize = 11.sp,
      fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
    )
    if (isSelected) {
      Spacer(modifier = Modifier.height(2.dp))
      Box(
        modifier = Modifier
          .size(4.dp)
          .background(PrimaryIndigo, CircleShape)
      )
    }
  }
}
