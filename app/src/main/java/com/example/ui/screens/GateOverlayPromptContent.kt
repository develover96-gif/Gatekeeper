package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GhostGlassButton
import com.example.ui.components.GlassCard
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
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GateOverlayPromptContent(
  appName: String = "Instagram",
  onUnlock: () -> Unit,
  onDismiss: () -> Unit,
  onSwitchToBreathing: (() -> Unit)? = null,
  onSwitchToMath: (() -> Unit)? = null
) {
  var selectedIntention by remember { mutableStateOf("") }
  var customIntentionText by remember { mutableStateOf("") }
  var selectedTimeMinutes by remember { mutableIntStateOf(5) }
  val focusManager = LocalFocusManager.current
  val scrollState = rememberScrollState()

  val promptPresets = listOf(
    "Check urgent message",
    "Reply to a contact",
    "Post planned update",
    "Quick 3-min lookup",
    "Research study topic"
  )

  val hasValidIntention = selectedIntention.isNotBlank() || customIntentionText.trim().length >= 3

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(CanvasBase)
  ) {
    // Ambient radial glow
    Box(
      modifier = Modifier
        .size(420.dp)
        .align(Alignment.Center)
        .background(
          Brush.radialGradient(
            colors = listOf(
              PrimaryIndigoGlow.copy(alpha = 0.16f),
              Color.Transparent
            )
          )
        )
    )

    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(horizontal = 24.dp, vertical = 20.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      // Top Context Pill & Instructions
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(9999.dp))
            .background(SurfaceContainerHigh.copy(alpha = 0.85f))
            .border(1.dp, BorderSubtle, RoundedCornerShape(9999.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            tint = ClayLock,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "$appName · Intentional Reflection Gate",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "What is your intention?",
          color = TextPrimary,
          fontSize = 24.sp,
          fontWeight = FontWeight.SemiBold,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = "Name your specific purpose before opening $appName to prevent unconscious scrolling.",
          color = TextSecondary,
          fontSize = 12.sp,
          textAlign = TextAlign.Center,
          lineHeight = 17.sp,
          modifier = Modifier.padding(horizontal = 12.dp)
        )
      }

      // Prompt Content Card
      GlassCard(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 16.dp),
        backgroundColor = SurfaceContainerLow.copy(alpha = 0.9f),
        cornerRadius = 20.dp
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "SELECT AN INTENTION",
            color = TextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.08.sp
          )

          Spacer(modifier = Modifier.height(10.dp))

          // Preset Chips
          FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            promptPresets.forEach { preset ->
              val isSelected = selectedIntention == preset
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(20.dp))
                  .background(if (isSelected) PrimaryIndigo.copy(alpha = 0.22f) else SurfaceContainerHigh)
                  .border(
                    1.dp,
                    if (isSelected) PrimaryIndigo else BorderSubtle,
                    RoundedCornerShape(20.dp)
                  )
                  .clickable {
                    selectedIntention = if (isSelected) "" else preset
                    if (!isSelected) {
                      customIntentionText = preset
                    }
                  }
                  .padding(horizontal = 12.dp, vertical = 7.dp)
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  if (isSelected) {
                    Icon(
                      imageVector = Icons.Default.Check,
                      contentDescription = null,
                      tint = PrimaryIndigo,
                      modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                  }
                  Text(
                    text = preset,
                    color = if (isSelected) PrimaryIndigo else TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = "OR TYPE YOUR SPECIFIC GOAL",
            color = TextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.08.sp
          )

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = customIntentionText,
            onValueChange = {
              customIntentionText = it
              if (selectedIntention != it) {
                selectedIntention = ""
              }
            },
            placeholder = {
              Text(
                "e.g. Look up dinner recipe, then leave",
                color = TextMuted,
                fontSize = 13.sp
              )
            },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("prompt_intention_input"),
            shape = RoundedCornerShape(12.dp),
            maxLines = 2,
            singleLine = false,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = SurfaceContainerHigh,
              unfocusedContainerColor = SurfaceContainer,
              focusedBorderColor = PrimaryIndigo,
              unfocusedBorderColor = BorderSubtle,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary
            )
          )

          Spacer(modifier = Modifier.height(16.dp))

          // Session Time Budget
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = null,
                tint = CalmMoss,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Session Budget:",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
              )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              listOf(3, 5, 10, 15).forEach { mins ->
                val isSelected = selectedTimeMinutes == mins
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) CalmMoss.copy(alpha = 0.25f) else SurfaceContainerHigh)
                    .border(1.dp, if (isSelected) CalmMoss else BorderSubtle, RoundedCornerShape(8.dp))
                    .clickable { selectedTimeMinutes = mins }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  Text(
                    text = "${mins}m",
                    color = if (isSelected) CalmMoss else TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                  )
                }
              }
            }
          }
        }
      }

      // Bottom Actions & Alternative Challenges
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        PrimaryGlassButton(
          text = if (hasValidIntention) "Enter $appName with Intention" else "Specify Intention to Unlock",
          enabled = hasValidIntention,
          onClick = onUnlock,
          modifier = Modifier.testTag("prompt_unlock_btn")
        )

        Spacer(modifier = Modifier.height(8.dp))

        GhostGlassButton(
          text = "Not now · Put phone down",
          onClick = onDismiss,
          modifier = Modifier.testTag("prompt_dismiss_btn")
        )

        // Quick emergency skip button for testing/urgent access
        if (!hasValidIntention) {
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Emergency skip (open without intention)",
            color = TextMuted,
            fontSize = 11.sp,
            modifier = Modifier
              .clickable { onUnlock() }
              .padding(4.dp)
          )
        }

        // Alternative challenge switchers
        if (onSwitchToBreathing != null || onSwitchToMath != null) {
          Spacer(modifier = Modifier.height(10.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            if (onSwitchToBreathing != null) {
              Row(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(8.dp))
                  .background(SurfaceContainerHigh)
                  .clickable { onSwitchToBreathing() }
                  .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(Icons.Default.Air, contentDescription = null, tint = CalmMoss, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Switch to Breathing", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
              }
            }
            if (onSwitchToMath != null) {
              Row(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(8.dp))
                  .background(SurfaceContainerHigh)
                  .clickable { onSwitchToMath() }
                  .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(Icons.Default.Calculate, contentDescription = null, tint = PrimaryIndigo, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Switch to Math", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "Conscious intent transforms reflexive habits into purposeful tools.",
          color = TextMuted,
          fontSize = 11.sp
        )
      }
    }
  }
}
