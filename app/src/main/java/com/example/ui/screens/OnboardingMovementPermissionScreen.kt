package com.example.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.ui.components.GhostGlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassSwitch
import com.example.ui.components.PrimaryGlassButton
import com.example.ui.components.PulsingBeacon
import com.example.ui.theme.BorderDefault
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CalmMoss
import com.example.ui.theme.CanvasBase
import com.example.ui.theme.ClayLock
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.theme.PrimaryIndigoGlow
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun OnboardingMovementPermissionScreen(
  onBack: () -> Unit,
  onContinue: () -> Unit,
  onSkip: () -> Unit
) {
  val context = LocalContext.current
  val scrollState = rememberScrollState()

  fun checkPermissionStatus(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACTIVITY_RECOGNITION
      ) == PackageManager.PERMISSION_GRANTED
    } else {
      true
    }
  }

  var hasPermission by remember { mutableStateOf(checkPermissionStatus()) }
  var isRejected by remember { mutableStateOf(false) }

  // Auto re-check on app resume when returning from System Settings
  val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
  DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event ->
      if (event == Lifecycle.Event.ON_RESUME) {
        val granted = checkPermissionStatus()
        hasPermission = granted
        if (granted) isRejected = false
      }
    }
    lifecycleOwner.lifecycle.addObserver(observer)
    onDispose {
      lifecycleOwner.lifecycle.removeObserver(observer)
    }
  }

  val permissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { granted ->
    hasPermission = granted
    if (granted) {
      isRejected = false
      onContinue()
    } else {
      isRejected = true
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(CanvasBase)
  ) {
    // Ambient aura
    Box(
      modifier = Modifier
        .size(360.dp)
        .align(Alignment.TopCenter)
        .background(
          Brush.radialGradient(
            colors = listOf(PrimaryIndigoGlow.copy(alpha = 0.15f), Color.Transparent)
          )
        )
    )

    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(horizontal = 20.dp, vertical = 16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Stepper Header
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onBack,
          modifier = Modifier.size(36.dp)
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = TextSecondary
          )
        }

        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(9999.dp))
            .background(SurfaceContainerHigh.copy(alpha = 0.7f))
            .border(1.dp, BorderSubtle, RoundedCornerShape(9999.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          PulsingBeacon(color = if (hasPermission) CalmMoss else if (isRejected) ClayLock else PrimaryIndigo, size = 6.dp)
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (isRejected) "STEP 2 OF 2 · SETTINGS LINK" else "OPTIONAL · MOVEMENT",
            color = if (hasPermission) CalmMoss else if (isRejected) ClayLock else PrimaryIndigo,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.08.sp
          )
        }

        Spacer(modifier = Modifier.size(36.dp))
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Hero icon
      Box(
        modifier = Modifier
          .size(64.dp)
          .clip(CircleShape)
          .background(if (hasPermission) CalmMoss.copy(alpha = 0.15f) else if (isRejected) ClayLock.copy(alpha = 0.15f) else PrimaryIndigo.copy(alpha = 0.15f))
          .border(1.dp, if (hasPermission) CalmMoss.copy(alpha = 0.4f) else if (isRejected) ClayLock.copy(alpha = 0.4f) else PrimaryIndigo.copy(alpha = 0.4f), CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = if (hasPermission) Icons.Default.CheckCircle else if (isRejected) Icons.Default.Warning else Icons.Default.DirectionsWalk,
          contentDescription = null,
          tint = if (hasPermission) CalmMoss else if (isRejected) ClayLock else PrimaryIndigo,
          modifier = Modifier.size(32.dp)
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = if (hasPermission) "Movement Permission Active" else if (isRejected) "Permission Requires Settings Approval" else "Physical Step Gate",
        color = TextPrimary,
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = if (isRejected) {
          "Android rejected the initial in-app prompt. To count steps, please enable Activity Recognition in App Settings below."
        } else {
          "Taking 20 steps before opening a habitual app disrupts muscle memory with real physical movement."
        },
        color = TextSecondary,
        fontSize = 13.sp,
        textAlign = TextAlign.Center,
        lineHeight = 18.sp,
        modifier = Modifier.padding(horizontal = 12.dp)
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Informative feature cards
      GlassCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = SurfaceContainerLow,
        cornerRadius = 18.dp
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          MovementFeatureRow(
            icon = Icons.Default.Speed,
            title = "Hardware Step Counter",
            description = "Uses Android's low-power pedometer sensor (TYPE_STEP_COUNTER). Minimal battery impact."
          )

          Spacer(modifier = Modifier.height(14.dp))
          Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BorderSubtle))
          Spacer(modifier = Modifier.height(14.dp))

          MovementFeatureRow(
            icon = Icons.Default.Lock,
            title = "Zero Location or GPS",
            description = "We only count step pulses. Never tracks GPS, coordinates, velocity, or outdoor routes."
          )

          Spacer(modifier = Modifier.height(14.dp))
          Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BorderSubtle))
          Spacer(modifier = Modifier.height(14.dp))

          MovementFeatureRow(
            icon = Icons.Default.AccessibilityNew,
            title = "Accessibility Fallback Guaranteed",
            description = "Wheelchair users or indoor stationary contexts can always switch instantly to Math or Breathing."
          )
        }
      }

      // Step 2: If rejected, show System Settings Breadcrumbs & Deep-Link Card
      if (isRejected && !hasPermission) {
        Spacer(modifier = Modifier.height(18.dp))

        GlassCard(
          modifier = Modifier.fillMaxWidth(),
          backgroundColor = SurfaceContainerLow.copy(alpha = 0.95f),
          cornerRadius = 16.dp
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Warning, contentDescription = null, tint = ClayLock, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "MANUAL APPROVAL PATH",
                color = ClayLock,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.08.sp
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "Android won't show the permission popup twice. Navigate directly:",
              color = TextSecondary,
              fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Text(text = "Settings", color = TextSecondary, fontSize = 11.sp)
              Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(12.dp))
              Text(text = "Apps", color = TextSecondary, fontSize = 11.sp)
              Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(12.dp))
              Text(text = "Gatekeeper", color = TextSecondary, fontSize = 11.sp)
              Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(12.dp))
              Text(text = "Permissions", color = TextSecondary, fontSize = 11.sp)
              Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(12.dp))
              Text(text = "Physical activity", color = PrimaryIndigo, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Direct Settings Deep-Link button
            GlassCard(
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                      data = Uri.fromParts("package", context.packageName, null)
                      flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                  } catch (e: Exception) {
                    val intent = Intent(Settings.ACTION_SETTINGS).apply {
                      flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                  }
                },
              backgroundColor = SurfaceContainerHigh,
              cornerRadius = 12.dp
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.OpenInNew, contentDescription = null, tint = PrimaryIndigo, modifier = Modifier.size(18.dp))
                  Spacer(modifier = Modifier.width(10.dp))
                  Text(
                    text = "Open App Settings",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                  )
                }
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Simulated Confirmation / Re-check row
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceContainerHigh)
                .clickable {
                  val status = checkPermissionStatus()
                  hasPermission = status
                  if (status) isRejected = false
                }
                .padding(horizontal = 12.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Refresh, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Re-check Permission Status",
                  color = TextSecondary,
                  fontSize = 11.sp
                )
              }
              Text(
                text = if (hasPermission) "Granted ✓" else "Not granted yet",
                color = if (hasPermission) CalmMoss else ClayLock,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      if (hasPermission) {
        PrimaryGlassButton(
          text = "Continue to App Selection",
          icon = Icons.Default.Check,
          onClick = onContinue,
          modifier = Modifier.testTag("continue_movement_permission_btn")
        )
      } else if (isRejected) {
        PrimaryGlassButton(
          text = "Open App Settings",
          icon = Icons.Default.OpenInNew,
          onClick = {
            try {
              val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
              }
              context.startActivity(intent)
            } catch (e: Exception) {
              val intent = Intent(Settings.ACTION_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
              }
              context.startActivity(intent)
            }
          },
          modifier = Modifier.testTag("open_movement_settings_btn")
        )
      } else {
        PrimaryGlassButton(
          text = "Enable Step Detection",
          icon = Icons.Default.DirectionsWalk,
          onClick = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !hasPermission) {
              permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
            } else {
              onContinue()
            }
          },
          modifier = Modifier.testTag("enable_movement_permission_btn")
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      GhostGlassButton(
        text = "Skip for now (Use Math & Breathing only)",
        onClick = onSkip,
        modifier = Modifier.testTag("skip_movement_permission_btn")
      )

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
private fun MovementFeatureRow(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  description: String
) {
  Row(verticalAlignment = Alignment.Top) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = CalmMoss,
      modifier = Modifier.size(18.dp)
    )
    Spacer(modifier = Modifier.width(10.dp))
    Column {
      Text(title, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
      Spacer(modifier = Modifier.height(2.dp))
      Text(description, color = TextSecondary, fontSize = 11.sp, lineHeight = 15.sp)
    }
  }
}
