package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CategoryTags
import com.example.data.model.DailyStatEntity
import com.example.data.model.GatedAppEntity
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassSwitch
import com.example.ui.components.PulsingBeacon
import com.example.ui.theme.BorderDefault
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CalmMoss
import com.example.ui.theme.CanvasBase
import com.example.ui.theme.ClayLock
import com.example.ui.theme.GlassBase
import com.example.ui.theme.GlassElevated
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.theme.PrimaryIndigoGlow
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

import com.example.ui.components.InstalledAppPickerView
import com.example.ui.viewmodel.AppPickerViewModel

@Composable
fun DashboardScreen(
  apps: List<GatedAppEntity>,
  weeklyStats: List<DailyStatEntity>,
  streakDays: Int = 4,
  milestoneMessage: String = "You've cleared gates for 4 days in a row! Mindful reflex engaged.",
  onToggleAppGated: (String, Boolean) -> Unit,
  onToggleCategoryGated: (String, Boolean) -> Unit = { _, _ -> },
  onOpenAppDetails: (GatedAppEntity) -> Unit,
  onNavigateToMathCalibration: () -> Unit,
  onNavigateToStats: () -> Unit,
  onNavigateToSettings: () -> Unit,
  onSimulateMathGate: (String) -> Unit,
  onSimulateBreathingGate: (String) -> Unit,
  onSimulateStepGate: (String) -> Unit = {},
  onSimulateHoldingGate: (String) -> Unit = {},
  onSimulatePromptGate: (String) -> Unit = {},
  onAddNewApp: (GatedAppEntity) -> Unit = {},
  onOpenPrototypeSwitcher: () -> Unit,
  appPickerViewModel: AppPickerViewModel? = null
) {
  var showMenu by remember { mutableStateOf(false) }
  var showAddAppDialog by remember { mutableStateOf(false) }

  val gatedApps = apps.filter { it.isGated }
  val totalCleared = gatedApps.sumOf { it.gatesClearedCount }
  val totalSkipped = gatedApps.sumOf { it.skipsCount }
  val totalReclaimedMinutes = 42

  var selectedCategoryFilter by remember { mutableStateOf<String?>(null) }

  val categories = remember(apps) {
    val found = apps.map { CategoryTags.normalize(it.category) }.distinct()
    val standardPriority = listOf(
      CategoryTags.SOCIAL,
      CategoryTags.ENTERTAINMENT,
      CategoryTags.PRODUCTIVITY,
      CategoryTags.NEWS,
      CategoryTags.GAMING,
      CategoryTags.UTILITIES
    )
    val ordered = standardPriority.filter { cat -> apps.any { CategoryTags.normalize(it.category) == cat } }
    val others = found.filter { it !in ordered }
    if (ordered.isEmpty() && others.isEmpty()) standardPriority else (ordered + others)
  }

  val displayApps = remember(apps, selectedCategoryFilter) {
    if (selectedCategoryFilter == null) apps
    else apps.filter { CategoryTags.normalize(it.category).equals(selectedCategoryFilter, ignoreCase = true) }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(CanvasBase)
  ) {
    // Subtle ambient gradient top glow
    Box(
      modifier = Modifier
        .size(360.dp)
        .align(Alignment.TopCenter)
        .background(
          Brush.radialGradient(
            colors = listOf(PrimaryIndigoGlow.copy(alpha = 0.12f), Color.Transparent)
          )
        )
    )

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Top Navigation Bar
      item {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(SurfaceContainerHigh)
                .border(1.dp, BorderHighlight, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = "Gatekeeper",
                tint = PrimaryIndigo,
                modifier = Modifier.size(18.dp)
              )
            }
            Text(
              text = "Gatekeeper",
              color = TextPrimary,
              fontSize = 18.sp,
              fontWeight = FontWeight.SemiBold
            )
          }

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            // Prototype Switcher Pill
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(9999.dp))
                .background(SurfaceContainerHigh)
                .border(1.dp, BorderSubtle, RoundedCornerShape(9999.dp))
                .clickable { onOpenPrototypeSwitcher() }
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "Flows",
                  color = PrimaryIndigo,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                  imageVector = Icons.Default.ChevronRight,
                  contentDescription = null,
                  tint = PrimaryIndigo,
                  modifier = Modifier.size(14.dp)
                )
              }
            }

            // User Avatar
            Box(
              modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(SurfaceContainerHighest)
                .border(1.dp, BorderSubtle, CircleShape)
                .clickable { onNavigateToSettings() },
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "AR",
                color = TextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }
      }

      // Greeting Card
      item {
        GlassCard(
          modifier = Modifier.fillMaxWidth(),
          backgroundColor = SurfaceContainerLow.copy(alpha = 0.85f),
          cornerRadius = 20.dp
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Peaceful morning, Alex.",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
              )

              Row(
                modifier = Modifier
                  .clip(RoundedCornerShape(9999.dp))
                  .background(Color(0xFF163224))
                  .border(1.dp, CalmMoss.copy(alpha = 0.3f), RoundedCornerShape(9999.dp))
                  .padding(horizontal = 8.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                PulsingBeacon(color = CalmMoss, size = 5.dp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "Shield Active",
                  color = CalmMoss,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Medium
                )
              }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = "\"A pause is not a wall, it is a moment to decide.\"",
              color = TextSecondary,
              fontSize = 13.sp,
              fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
          }
        }
      }

      // 2 Bento Stat Cards
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // Card 1: Gates Cleared
          GlassCard(
            modifier = Modifier
              .weight(1f)
              .clickable { onNavigateToStats() },
            backgroundColor = SurfaceContainerLow.copy(alpha = 0.75f),
            cornerRadius = 16.dp
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                text = "GATES CLEARED",
                color = TextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.08.sp
              )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "14 times",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "3 skipped intentional",
                color = CalmMoss,
                fontSize = 11.sp
              )

              Spacer(modifier = Modifier.height(10.dp))
              // Mini sparkline bars
              Row(
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.height(18.dp)
              ) {
                listOf(8, 12, 10, 16, 14, 20, 14).forEachIndexed { i, h ->
                  Box(
                    modifier = Modifier
                      .weight(1f)
                      .height(h.dp)
                      .clip(RoundedCornerShape(2.dp))
                      .background(if (i == 6) PrimaryIndigo else SurfaceContainerHighest)
                  )
                }
              }
            }
          }

          // Card 2: Reclaimed
          GlassCard(
            modifier = Modifier
              .weight(1f)
              .clickable { onNavigateToStats() },
            backgroundColor = SurfaceContainerLow.copy(alpha = 0.75f),
            cornerRadius = 16.dp
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                text = "RECLAIMED",
                color = TextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.08.sp
              )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "42m saved",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
              )
              Spacer(modifier = Modifier.height(2.dp))
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.TrendingUp,
                  contentDescription = null,
                  tint = CalmMoss,
                  modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                  text = "+18% vs 7-day avg",
                  color = CalmMoss,
                  fontSize = 11.sp
                )
              }

              Spacer(modifier = Modifier.height(10.dp))
              // Progress curve representation
              Row(
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.height(18.dp)
              ) {
                listOf(6, 10, 14, 12, 18, 16, 22).forEachIndexed { i, h ->
                  Box(
                    modifier = Modifier
                      .weight(1f)
                      .height(h.dp)
                      .clip(RoundedCornerShape(2.dp))
                      .background(if (i == 6) CalmMoss else SurfaceContainerHighest)
                  )
                }
              }
            }
          }
        }
      }

      // Streak & Encouraging Milestone Banner
      item {
        GlassCard(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigateToStats() }
            .testTag("dashboard_streak_card"),
          backgroundColor = SurfaceContainerLow.copy(alpha = 0.85f),
          borderColor = CalmMoss.copy(alpha = 0.35f),
          cornerRadius = 16.dp
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(CalmMoss.copy(alpha = 0.15f))
                .border(1.dp, CalmMoss.copy(alpha = 0.3f), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Whatshot,
                contentDescription = null,
                tint = CalmMoss,
                modifier = Modifier.size(20.dp)
              )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "$streakDays-Day Mindful Streak",
                  color = TextPrimary,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF163224))
                    .padding(horizontal = 5.dp, vertical = 1.dp)
                ) {
                  Text(
                    text = "Active",
                    color = CalmMoss,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold
                  )
                }
              }
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = milestoneMessage,
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp
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
      }

      // Quick Test Intercept Pills
      item {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            text = "QUICK GATE PREVIEWS",
            color = TextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.08.sp
          )
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            // Test Math Gate
            GlassCard(
              modifier = Modifier
                .weight(1f)
                .clickable { onSimulateMathGate("com.instagram.android") }
                .testTag("simulate_math_gate_btn"),
              backgroundColor = SurfaceContainerHigh.copy(alpha = 0.9f),
              borderColor = PrimaryIndigo.copy(alpha = 0.3f),
              cornerRadius = 10.dp
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(Icons.Default.Calculate, contentDescription = null, tint = PrimaryIndigo, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(3.dp))
                Text("Math", color = TextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
              }
            }

            // Test Breathing Gate
            GlassCard(
              modifier = Modifier
                .weight(1f)
                .clickable { onSimulateBreathingGate("com.zhiliaoapp.musically") }
                .testTag("simulate_breathing_gate_btn"),
              backgroundColor = SurfaceContainerHigh.copy(alpha = 0.9f),
              borderColor = CalmMoss.copy(alpha = 0.3f),
              cornerRadius = 10.dp
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(Icons.Default.Air, contentDescription = null, tint = CalmMoss, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(3.dp))
                Text("Breathe", color = TextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
              }
            }

            // Test Walking Step Gate
            GlassCard(
              modifier = Modifier
                .weight(1f)
                .clickable { onSimulateStepGate("com.twitter.android") }
                .testTag("simulate_step_gate_btn"),
              backgroundColor = SurfaceContainerHigh.copy(alpha = 0.9f),
              borderColor = PrimaryIndigoGlow.copy(alpha = 0.3f),
              cornerRadius = 10.dp
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(Icons.Default.DirectionsWalk, contentDescription = null, tint = PrimaryIndigo, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(3.dp))
                Text("Steps", color = TextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
              }
            }
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            // Test Holding Gate (5s)
            GlassCard(
              modifier = Modifier
                .weight(1f)
                .clickable { onSimulateHoldingGate("com.reddit.frontpage") }
                .testTag("simulate_holding_gate_btn"),
              backgroundColor = SurfaceContainerHigh.copy(alpha = 0.9f),
              borderColor = ClayLock.copy(alpha = 0.35f),
              cornerRadius = 10.dp
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(Icons.Default.TouchApp, contentDescription = null, tint = ClayLock, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Holding (5s)", color = TextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
              }
            }

            // Test Intentional Prompt Gate
            GlassCard(
              modifier = Modifier
                .weight(1f)
                .clickable { onSimulatePromptGate("com.google.android.youtube") }
                .testTag("simulate_prompt_gate_btn"),
              backgroundColor = SurfaceContainerHigh.copy(alpha = 0.9f),
              borderColor = PrimaryIndigoGlow.copy(alpha = 0.35f),
              cornerRadius = 10.dp
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(Icons.Default.EditNote, contentDescription = null, tint = PrimaryIndigoGlow, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Reflective Prompt", color = TextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
              }
            }
          }
        }
      }

      // Category Restrictions Quick-Toggle Section
      item {
        GlassCard(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("category_restrictions_card"),
          backgroundColor = SurfaceContainerLow.copy(alpha = 0.85f),
          borderColor = BorderDefault,
          cornerRadius = 18.dp
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(PrimaryIndigo.copy(alpha = 0.18f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Layers,
                    contentDescription = null,
                    tint = PrimaryIndigoGlow,
                    modifier = Modifier.size(15.dp)
                  )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text(
                    text = "Category Restrictions",
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                  )
                  Text(
                    text = "Toggle entire categories as Restricted at once",
                    color = TextMuted,
                    fontSize = 11.sp
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Category rows
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
              categories.forEach { catName ->
                val catApps = apps.filter { CategoryTags.normalize(it.category) == catName }
                val totalCount = catApps.size
                val restrictedCount = catApps.count { it.isGated }
                val isAllRestricted = totalCount > 0 && restrictedCount == totalCount
                val accentColor = Color(CategoryTags.getCategoryAccentHex(catName))

                GlassCard(
                  modifier = Modifier
                    .fillMaxWidth()
                    .testTag("category_row_${catName.lowercase()}"),
                  backgroundColor = if (isAllRestricted) SurfaceContainerHigh.copy(alpha = 0.75f) else SurfaceContainerLowest.copy(alpha = 0.45f),
                  borderColor = if (isAllRestricted) accentColor.copy(alpha = 0.45f) else BorderSubtle,
                  cornerRadius = 12.dp
                ) {
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(horizontal = 12.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      modifier = Modifier.weight(1f)
                    ) {
                      Box(
                        modifier = Modifier
                          .size(30.dp)
                          .clip(RoundedCornerShape(8.dp))
                          .background(accentColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                      ) {
                        val icon = when (catName) {
                          CategoryTags.SOCIAL -> Icons.Default.Forum
                          CategoryTags.ENTERTAINMENT -> Icons.Default.SmartDisplay
                          CategoryTags.PRODUCTIVITY -> Icons.Default.Language
                          CategoryTags.NEWS -> Icons.Default.AlternateEmail
                          else -> Icons.Default.Shield
                        }
                        Icon(
                          imageVector = icon,
                          contentDescription = null,
                          tint = accentColor,
                          modifier = Modifier.size(16.dp)
                        )
                      }

                      Spacer(modifier = Modifier.width(10.dp))

                      Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                          Text(
                            text = catName,
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                          )
                          Spacer(modifier = Modifier.width(6.dp))
                          Box(
                            modifier = Modifier
                              .clip(RoundedCornerShape(4.dp))
                              .background(if (isAllRestricted) CalmMoss.copy(alpha = 0.2f) else SurfaceContainerHighest)
                              .padding(horizontal = 5.dp, vertical = 1.dp)
                          ) {
                            Text(
                              text = if (isAllRestricted) "Restricted" else "$restrictedCount / $totalCount Restricted",
                              color = if (isAllRestricted) CalmMoss else TextSecondary,
                              fontSize = 10.sp,
                              fontWeight = FontWeight.Medium
                            )
                          }
                        }

                        if (catApps.isNotEmpty()) {
                          Text(
                            text = catApps.joinToString(", ") { it.appName },
                            color = TextMuted,
                            fontSize = 10.sp,
                            maxLines = 1
                          )
                        }
                      }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                      GlassSwitch(
                        checked = isAllRestricted,
                        onCheckedChange = { shouldGateAll ->
                          onToggleCategoryGated(catName, shouldGateAll)
                        }
                      )
                    }
                  }
                }
              }
            }
          }
        }
      }

      // Filter Chips by Category Tag
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // All Chip
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(9999.dp))
              .background(if (selectedCategoryFilter == null) PrimaryIndigo else SurfaceContainer)
              .border(
                1.dp,
                if (selectedCategoryFilter == null) PrimaryIndigoGlow else BorderSubtle,
                RoundedCornerShape(9999.dp)
              )
              .clickable { selectedCategoryFilter = null }
              .padding(horizontal = 10.dp, vertical = 5.dp)
          ) {
            Text(
              text = "All (${apps.size})",
              color = if (selectedCategoryFilter == null) Color.White else TextSecondary,
              fontSize = 11.sp,
              fontWeight = if (selectedCategoryFilter == null) FontWeight.SemiBold else FontWeight.Normal
            )
          }

          categories.forEach { cat ->
            val count = apps.count { CategoryTags.normalize(it.category) == cat }
            val isSelected = selectedCategoryFilter == cat
            val catColor = Color(CategoryTags.getCategoryAccentHex(cat))

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(9999.dp))
                .background(if (isSelected) catColor.copy(alpha = 0.25f) else SurfaceContainer)
                .border(
                  1.dp,
                  if (isSelected) catColor else BorderSubtle,
                  RoundedCornerShape(9999.dp)
                )
                .clickable {
                  selectedCategoryFilter = if (selectedCategoryFilter == cat) null else cat
                }
                .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
              Text(
                text = "$cat ($count)",
                color = if (isSelected) catColor else TextSecondary,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
              )
            }
          }
        }
      }

      // Active Gated Apps Section Header
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = if (selectedCategoryFilter != null) "$selectedCategoryFilter Apps" else "Active Gated Apps",
              color = TextPrimary,
              fontSize = 16.sp,
              fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(9999.dp))
                .background(SurfaceContainerHighest)
                .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
              Text(
                text = "${displayApps.count { it.isGated }} of ${displayApps.size} Protected",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
              )
            }
          }

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(PrimaryIndigo)
                .clickable { showAddAppDialog = true }
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .testTag("add_app_btn"),
              contentAlignment = Alignment.Center
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Add, contentDescription = "Add App", tint = Color.White, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add App", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
              }
            }

            if (selectedCategoryFilter != null) {
              val catAllRestricted = displayApps.isNotEmpty() && displayApps.all { it.isGated }
              Text(
                text = if (catAllRestricted) "Unrestrict All" else "Restrict All",
                color = PrimaryIndigo,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable {
                  onToggleCategoryGated(selectedCategoryFilter!!, !catAllRestricted)
                }
              )
            } else {
              Text(
                text = "Math",
                color = PrimaryIndigo,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onNavigateToMathCalibration() }
              )
            }
          }
        }
      }

      // App List
      items(displayApps, key = { it.packageName }) { app ->
        DashboardAppCard(
          app = app,
          onToggle = { onToggleAppGated(app.packageName, it) },
          onClick = { onOpenAppDetails(app) }
        )
      }

      // System Status Banner
      item {
        GlassCard(
          modifier = Modifier.fillMaxWidth(),
          backgroundColor = Color(0x221E382A),
          borderColor = CalmMoss.copy(alpha = 0.3f),
          cornerRadius = 14.dp
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = CalmMoss,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Accessibility service running smoothly",
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
              )
              Text(
                text = "Battery optimization whitelisted • Zero background drain",
                color = TextSecondary,
                fontSize = 11.sp
              )
            }
          }
        }
        Spacer(modifier = Modifier.height(70.dp)) // Extra padding for bottom navigation bar
      }
    }
  }

  if (showAddAppDialog) {
    AddAppPickerDialog(
      existingApps = apps,
      appPickerViewModel = appPickerViewModel,
      onDismiss = { showAddAppDialog = false },
      onAddApp = { newApp ->
        onAddNewApp(newApp)
        showAddAppDialog = false
      }
    )
  }
}

@Composable
private fun AddAppPickerDialog(
  existingApps: List<GatedAppEntity>,
  appPickerViewModel: AppPickerViewModel? = null,
  onDismiss: () -> Unit,
  onAddApp: (GatedAppEntity) -> Unit
) {
  val popularApps = remember {
    listOf(
      Triple("Instagram", "com.instagram.android", CategoryTags.SOCIAL),
      Triple("TikTok", "com.zhiliaoapp.musically", CategoryTags.SOCIAL),
      Triple("YouTube", "com.google.android.youtube", CategoryTags.ENTERTAINMENT),
      Triple("Twitter / X", "com.twitter.android", CategoryTags.SOCIAL),
      Triple("Reddit", "com.reddit.frontpage", CategoryTags.SOCIAL),
      Triple("Snapchat", "com.snapchat.android", CategoryTags.SOCIAL),
      Triple("Facebook", "com.facebook.katana", CategoryTags.SOCIAL),
      Triple("Netflix", "com.netflix.mediaclient", CategoryTags.ENTERTAINMENT),
      Triple("Twitch", "tv.twitch.android.app", CategoryTags.ENTERTAINMENT),
      Triple("Discord", "com.discord", CategoryTags.SOCIAL),
      Triple("Pinterest", "com.pinterest", CategoryTags.SOCIAL),
      Triple("Spotify", "com.spotify.music", CategoryTags.ENTERTAINMENT),
      Triple("WhatsApp", "com.whatsapp", CategoryTags.SOCIAL),
      Triple("Telegram", "org.telegram.messenger", CategoryTags.SOCIAL),
      Triple("Roblox", "com.roblox.client", "Games"),
      Triple("Subway Surfers", "com.kiloo.subwaysurf", "Games")
    )
  }

  var selectedTab by remember { mutableStateOf(0) } // 0: Popular, 1: Search Installed, 2: Custom
  var selectedSuggested by remember { mutableStateOf(popularApps.firstOrNull { p -> existingApps.none { it.packageName == p.second } } ?: popularApps[0]) }
  var customAppName by remember { mutableStateOf("") }
  var customPkgName by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf(selectedSuggested.third) }
  var selectedChallenge by remember { mutableStateOf("MATH") }
  var dailyLimitMins by remember { mutableStateOf(30) }
  var sessionLimitMins by remember { mutableStateOf(10) }
  var isDistracting by remember { mutableStateOf(true) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text("Protect New App", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
        IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
          Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
        }
      }
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Tab selector
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceContainer)
            .padding(2.dp)
        ) {
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(6.dp))
              .background(if (selectedTab == 0) PrimaryIndigo else Color.Transparent)
              .clickable { selectedTab = 0 }
              .padding(vertical = 6.dp),
            contentAlignment = Alignment.Center
          ) {
            Text("Popular", color = if (selectedTab == 0) Color.White else TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
          }

          if (appPickerViewModel != null) {
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(6.dp))
                .background(if (selectedTab == 1) PrimaryIndigo else Color.Transparent)
                .clickable { selectedTab = 1 }
                .padding(vertical = 6.dp),
              contentAlignment = Alignment.Center
            ) {
              Text("Search", color = if (selectedTab == 1) Color.White else TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
          }

          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(6.dp))
              .background(if (selectedTab == 2) PrimaryIndigo else Color.Transparent)
              .clickable { selectedTab = 2 }
              .padding(vertical = 6.dp),
            contentAlignment = Alignment.Center
          ) {
            Text("Manual", color = if (selectedTab == 2) Color.White else TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
          }
        }

        if (selectedTab == 0) {
          Text("Select app to gate:", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
          Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            popularApps.forEach { (name, pkg, cat) ->
              val isExisting = existingApps.any { it.packageName == pkg }
              val isSelected = selectedSuggested.second == pkg

              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (isSelected) PrimaryIndigo.copy(alpha = 0.25f) else SurfaceContainerLow)
                  .border(1.dp, if (isSelected) PrimaryIndigo else BorderSubtle, RoundedCornerShape(8.dp))
                  .clickable(enabled = !isExisting) {
                    selectedSuggested = Triple(name, pkg, cat)
                    selectedCategory = cat
                  }
                  .padding(horizontal = 10.dp, vertical = 7.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text(name, color = if (isExisting) TextMuted else TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                  Text(pkg, color = TextMuted, fontSize = 10.sp)
                }
                if (isExisting) {
                  Text("Added", color = CalmMoss, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                } else if (isSelected) {
                  Icon(Icons.Default.Check, contentDescription = null, tint = PrimaryIndigo, modifier = Modifier.size(16.dp))
                }
              }
            }
          }
        } else {
          OutlinedTextField(
            value = customAppName,
            onValueChange = { customAppName = it },
            label = { Text("App Name (e.g. Chess)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = PrimaryIndigo,
              unfocusedBorderColor = BorderSubtle,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary
            )
          )

          OutlinedTextField(
            value = customPkgName,
            onValueChange = { customPkgName = it },
            label = { Text("Package Name (e.g. com.chess)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = PrimaryIndigo,
              unfocusedBorderColor = BorderSubtle,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary
            )
          )
        }

        // Category Selection
        Text("Category:", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          listOf(CategoryTags.SOCIAL, CategoryTags.ENTERTAINMENT, CategoryTags.PRODUCTIVITY, "Games").forEach { cat ->
            val isSel = selectedCategory == cat
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(6.dp))
                .background(if (isSel) PrimaryIndigo else SurfaceContainerLow)
                .clickable { selectedCategory = cat }
                .padding(vertical = 6.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(cat, color = if (isSel) Color.White else TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            }
          }
        }

        // Challenge Selection
        Text("Friction Challenge:", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          listOf("MATH" to "Math", "PROBLEM_SOLVING" to "Logic", "BREATHING" to "Breathe", "STEPS" to "Steps").forEach { (type, label) ->
            val isSel = selectedChallenge == type
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(6.dp))
                .background(if (isSel) PrimaryIndigo else SurfaceContainerLow)
                .clickable { selectedChallenge = type }
                .padding(vertical = 6.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(label, color = if (isSel) Color.White else TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            }
          }
        }

        // Daily Limit Selection
        Text("Daily Limit Duration:", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          listOf(0 to "Off", 15 to "15m", 30 to "30m", 60 to "60m").forEach { (mins, label) ->
            val isSel = dailyLimitMins == mins
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(6.dp))
                .background(if (isSel) PrimaryIndigo else SurfaceContainerLow)
                .clickable { dailyLimitMins = mins }
                .padding(vertical = 5.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(label, color = if (isSel) Color.White else TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            }
          }
        }
      }
    },
    confirmButton = {
      val targetName = if (selectedTab == 0) selectedSuggested.first else customAppName.trim()
      val targetPkg = if (selectedTab == 0) selectedSuggested.second else customPkgName.trim()
      val canAdd = targetName.isNotEmpty() && targetPkg.isNotEmpty()

      TextButton(
        onClick = {
          if (canAdd) {
            val newApp = GatedAppEntity(
              packageName = targetPkg,
              appName = targetName,
              category = selectedCategory,
              isGated = true,
              challengeType = selectedChallenge,
              graceWindowMinutes = 15,
              dailyLimitMinutes = dailyLimitMins,
              sessionLimitMinutes = sessionLimitMins,
              isDistracting = isDistracting
            )
            onAddApp(newApp)
          }
        },
        enabled = canAdd
      ) {
        Text("Add to Gated Apps", color = if (canAdd) PrimaryIndigo else TextMuted, fontWeight = FontWeight.SemiBold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel", color = TextSecondary)
      }
    },
    containerColor = SurfaceContainerHigh
  )
}

@Composable
private fun DashboardAppCard(
  app: GatedAppEntity,
  onToggle: (Boolean) -> Unit,
  onClick: () -> Unit
) {
  GlassCard(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() },
    backgroundColor = if (app.isGated) SurfaceContainerLow else SurfaceContainerLowest.copy(alpha = 0.5f),
    borderColor = if (app.isGated) BorderDefault else BorderSubtle,
    cornerRadius = 16.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (app.isGated) SurfaceContainerHigh else SurfaceContainer)
            .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp)),
          contentAlignment = Alignment.Center
        ) {
          val icon = when (app.iconType) {
            "camera" -> Icons.Default.CameraAlt
            "play" -> Icons.Default.PlayArrow
            "at" -> Icons.Default.AlternateEmail
            "forum" -> Icons.Default.Forum
            "video" -> Icons.Default.SmartDisplay
            else -> Icons.Default.Language
          }
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (app.isGated) PrimaryIndigo else TextMuted,
            modifier = Modifier.size(22.dp)
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = app.appName,
              color = TextPrimary,
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(6.dp))
            val catAccent = Color(CategoryTags.getCategoryAccentHex(app.category))
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(catAccent.copy(alpha = 0.15f))
                .padding(horizontal = 5.dp, vertical = 1.dp)
            ) {
              Text(
                text = CategoryTags.normalize(app.category),
                color = catAccent,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(
                  when (app.challengeType.uppercase()) {
                    "BREATHING" -> CalmMoss.copy(alpha = 0.15f)
                    "STEPS" -> PrimaryIndigoGlow.copy(alpha = 0.15f)
                    "HOLDING" -> ClayLock.copy(alpha = 0.15f)
                    "PROMPT" -> PrimaryIndigo.copy(alpha = 0.15f)
                    else -> PrimaryIndigo.copy(alpha = 0.15f)
                  }
                )
                .padding(horizontal = 5.dp, vertical = 1.dp)
            ) {
              Text(
                text = when (app.challengeType.uppercase()) {
                  "BREATHING" -> "Breathing"
                  "STEPS" -> "Walking"
                  "HOLDING" -> "Holding"
                  "PROMPT" -> "Prompt"
                  else -> "Math"
                },
                color = when (app.challengeType.uppercase()) {
                  "BREATHING" -> CalmMoss
                  "STEPS" -> PrimaryIndigoGlow
                  "HOLDING" -> ClayLock
                  "PROMPT" -> PrimaryIndigo
                  else -> PrimaryIndigo
                },
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
          }

          Spacer(modifier = Modifier.height(2.dp))

          Text(
            text = if (app.isGated) "${app.gatesClearedCount} gates cleared • avg 18s pause" else "Protection paused",
            color = if (app.isGated) TextSecondary else TextMuted,
            fontSize = 11.sp
          )
        }
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        GlassSwitch(
          checked = app.isGated,
          onCheckedChange = onToggle
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
          imageVector = Icons.Default.ChevronRight,
          contentDescription = "Details",
          tint = TextMuted,
          modifier = Modifier.size(16.dp)
        )
      }
    }
  }
}
