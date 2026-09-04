package com.example.ui.screens

import android.content.Intent
import android.provider.Settings
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassSwitch
import com.example.ui.components.PrimaryGlassButton
import com.example.ui.components.PulsingBeacon
import com.example.ui.theme.BorderDefault
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CalmMoss
import com.example.ui.theme.CanvasBase
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.theme.PrimaryIndigoGlow
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun OnboardingAccessibilityScreen(
  onBack: () -> Unit,
  onContinue: () -> Unit
) {
  val context = LocalContext.current
  val scrollState = rememberScrollState()
  var isServiceGranted by remember { mutableStateOf(false) }
  var showWhyDialog by remember { mutableStateOf(false) }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(CanvasBase)
  ) {
    // Ambient light
    Box(
      modifier = Modifier
        .size(320.dp)
        .align(Alignment.TopCenter)
        .background(
          Brush.radialGradient(
            colors = listOf(PrimaryIndigoGlow.copy(alpha = 0.14f), Color.Transparent)
          )
        )
    )

    Column(
      modifier = Modifier
        .fillMaxSize()
        .widthIn(max = 600.dp)
        .align(Alignment.TopCenter)
        .verticalScroll(scrollState)
        .padding(horizontal = 20.dp, vertical = 16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Top Navigation & Step Indicator
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
          PulsingBeacon(color = PrimaryIndigo, size = 6.dp)
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "STEP 2 OF 3",
            color = PrimaryIndigo,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.08.sp
          )
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
          Box(
            modifier = Modifier
              .width(16.dp)
              .height(4.dp)
              .clip(RoundedCornerShape(2.dp))
              .background(PrimaryIndigo)
          )
          Box(
            modifier = Modifier
              .width(28.dp)
              .height(4.dp)
              .clip(RoundedCornerShape(2.dp))
              .background(PrimaryIndigo)
          )
          Box(
            modifier = Modifier
              .width(8.dp)
              .height(4.dp)
              .clip(RoundedCornerShape(2.dp))
              .background(SurfaceContainerHighest)
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Beacon Circle
      Box(
        modifier = Modifier
          .size(76.dp)
          .clip(CircleShape)
          .background(SurfaceContainerHigh)
          .border(1.dp, BorderHighlight, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.AccessibilityNew,
          contentDescription = "Accessibility",
          tint = if (isServiceGranted) CalmMoss else PrimaryIndigo,
          modifier = Modifier.size(36.dp)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "Enable Accessibility",
        color = TextPrimary,
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Gatekeeper needs window-state detection to recognize when a gated app launches, so it can hold the door for your attention.",
        color = TextSecondary,
        fontSize = 13.sp,
        textAlign = TextAlign.Center,
        lineHeight = 20.sp,
        modifier = Modifier.padding(horizontal = 8.dp)
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Privacy Safeguard Manifest Card
      GlassCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = SurfaceContainerLow.copy(alpha = 0.85f),
        cornerRadius = 16.dp
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Lock,
              contentDescription = null,
              tint = CalmMoss,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Privacy Safeguard Manifest",
              color = TextPrimary,
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          Row(verticalAlignment = Alignment.Top) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = CalmMoss,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Active Shielding Only",
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
              )
              Text(
                text = "Only checks the foreground package name of apps you choose to pause.",
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 16.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Row(verticalAlignment = Alignment.Top) {
            Icon(
              imageVector = Icons.Default.VisibilityOff,
              contentDescription = null,
              tint = PrimaryIndigo,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Absolute Zero Surveillance",
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
              )
              Text(
                text = "Cannot and will never read messages, keystrokes, passwords, or on-screen content.",
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 16.sp
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Android Navigation Breadcrumbs & Simulated OS Switch Card
      GlassCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = SurfaceContainerLow.copy(alpha = 0.85f),
        cornerRadius = 16.dp
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "SYSTEM SETTINGS PATH",
            color = TextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.08.sp
          )
          Spacer(modifier = Modifier.height(6.dp))
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Text(text = "Settings", color = TextSecondary, fontSize = 11.sp)
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(12.dp))
            Text(text = "Accessibility", color = TextSecondary, fontSize = 11.sp)
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(12.dp))
            Text(text = "Downloaded Apps", color = TextSecondary, fontSize = 11.sp)
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(12.dp))
            Text(text = "Gatekeeper", color = PrimaryIndigo, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Simulated Switch Card
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(SurfaceContainerHigh)
              .clickable { isServiceGranted = !isServiceGranted }
              .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(
                text = "Gatekeeper Service",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
              )
              Text(
                text = if (isServiceGranted) "Permission Active" else "Tap to toggle / confirm grant",
                color = if (isServiceGranted) CalmMoss else TextMuted,
                fontSize = 11.sp
              )
            }
            GlassSwitch(
              checked = isServiceGranted,
              onCheckedChange = { isServiceGranted = it }
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Open System Settings button
      GlassCard(
        modifier = Modifier
          .fillMaxWidth()
          .clickable {
            try {
              val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
              context.startActivity(intent)
            } catch (e: Exception) {
              // Fallback if settings cannot open
              isServiceGranted = true
            }
          },
        backgroundColor = SurfaceContainerHigh,
        cornerRadius = 14.dp
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.OpenInNew,
              contentDescription = null,
              tint = PrimaryIndigo,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = "Open Accessibility Settings",
              color = TextPrimary,
              fontSize = 13.sp,
              fontWeight = FontWeight.Medium
            )
          }
          Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(18.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      TextButton(
        onClick = { showWhyDialog = true }
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.HelpOutline, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Why is this permission strictly needed?",
            color = TextMuted,
            fontSize = 12.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Continue Button
      PrimaryGlassButton(
        text = if (isServiceGranted) "Next: Overlay Permission" else "Confirm & Continue",
        icon = Icons.AutoMirrored.Filled.ArrowForward,
        onClick = {
          isServiceGranted = true
          onContinue()
        },
        modifier = Modifier.testTag("accessibility_continue_btn")
      )

      Spacer(modifier = Modifier.height(16.dp))
    }
  }

  if (showWhyDialog) {
    AlertDialog(
      onDismissRequest = { showWhyDialog = false },
      title = {
        Text("Why Accessibility?", color = TextPrimary, fontWeight = FontWeight.SemiBold)
      },
      text = {
        Text(
          "Android's security architecture does not allow regular apps to see what you are doing in the background. The Accessibility Service is the only official Android API that can alert an app when a foreground package changes.\n\nGatekeeper requests only `TYPE_WINDOW_STATE_CHANGED` and explicitly disables window content inspection.",
          color = TextSecondary,
          fontSize = 13.sp,
          lineHeight = 18.sp
        )
      },
      confirmButton = {
        TextButton(onClick = { showWhyDialog = false }) {
          Text("Understood", color = PrimaryIndigo)
        }
      },
      containerColor = SurfaceContainerHigh
    )
  }
}
