package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CategoryTags
import com.example.data.model.GatedAppEntity
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassSwitch
import com.example.ui.components.InstalledAppPickerView
import com.example.ui.components.PrimaryGlassButton
import com.example.ui.components.PulsingBeacon
import com.example.ui.viewmodel.AppPickerViewModel
import com.example.ui.theme.BorderDefault
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CalmMoss
import com.example.ui.theme.CanvasBase
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun OnboardingAppPickerScreen(
  apps: List<GatedAppEntity>,
  onToggleApp: (String, Boolean) -> Unit,
  onToggleCategory: ((String, Boolean) -> Unit)? = null,
  onBack: () -> Unit,
  onFinish: () -> Unit,
  appPickerViewModel: AppPickerViewModel? = null
) {
  var activeTab by remember { mutableStateOf(0) } // 0: Quick Habit Apps, 1: All Installed Apps
  var searchQuery by remember { mutableStateOf("") }
  var selectedCategoryFilter by remember { mutableStateOf<String?>(null) }

  val filteredApps = apps.filter {
    (selectedCategoryFilter == null || CategoryTags.normalize(it.category).equals(selectedCategoryFilter, ignoreCase = true)) &&
      (it.appName.contains(searchQuery, ignoreCase = true) ||
        it.category.contains(searchQuery, ignoreCase = true))
  }

  val activeCount = apps.count { it.isGated }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(CanvasBase),
    contentAlignment = Alignment.TopCenter
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .widthIn(max = 600.dp)
        .padding(horizontal = 20.dp, vertical = 16.dp)
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
        PulsingBeacon(color = PrimaryIndigo, size = 6.dp)
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "SETUP · APPS",
          color = PrimaryIndigo,
          fontSize = 10.sp,
          fontWeight = FontWeight.SemiBold,
          letterSpacing = 0.08.sp
        )
      }

      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(9999.dp))
          .background(CalmMoss.copy(alpha = 0.15f))
          .padding(horizontal = 10.dp, vertical = 4.dp)
      ) {
        Text(
          text = "$activeCount Selected",
          color = CalmMoss,
          fontSize = 11.sp,
          fontWeight = FontWeight.SemiBold
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Choose apps to pause",
      color = TextPrimary,
      fontSize = 24.sp,
      fontWeight = FontWeight.SemiBold
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
      text = "Select the habitual apps where automatic muscle memory takes over. You can reconfigure this anytime.",
      color = TextSecondary,
      fontSize = 13.sp,
      lineHeight = 18.sp
    )

    Spacer(modifier = Modifier.height(14.dp))

    if (appPickerViewModel != null) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(SurfaceContainerLow)
          .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(9.dp))
            .background(if (activeTab == 0) PrimaryIndigo.copy(alpha = 0.25f) else Color.Transparent)
            .border(1.dp, if (activeTab == 0) PrimaryIndigo else Color.Transparent, RoundedCornerShape(9.dp))
            .clickable { activeTab = 0 }
            .padding(vertical = 8.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "Curated Habits",
            color = if (activeTab == 0) TextPrimary else TextMuted,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
          )
        }

        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(9.dp))
            .background(if (activeTab == 1) PrimaryIndigo.copy(alpha = 0.25f) else Color.Transparent)
            .border(1.dp, if (activeTab == 1) PrimaryIndigo else Color.Transparent, RoundedCornerShape(9.dp))
            .clickable { activeTab = 1 }
            .padding(vertical = 8.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "All Device Apps",
            color = if (activeTab == 1) TextPrimary else TextMuted,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
      Spacer(modifier = Modifier.height(10.dp))
    }

    if (activeTab == 1 && appPickerViewModel != null) {
      Box(modifier = Modifier.weight(1f)) {
        InstalledAppPickerView(
          viewModel = appPickerViewModel,
          modifier = Modifier.fillMaxSize()
        )
      }
    } else {
      // Search bar
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp),
        placeholder = { Text("Search installed habit apps...", color = TextMuted, fontSize = 13.sp) },
        leadingIcon = {
          Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
        },
        shape = RoundedCornerShape(14.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = SurfaceContainerHigh,
          unfocusedContainerColor = SurfaceContainerLow,
          focusedBorderColor = PrimaryIndigo,
          unfocusedBorderColor = BorderSubtle,
          focusedTextColor = TextPrimary,
          unfocusedTextColor = TextPrimary
        )
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Category Tag Chips
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(9999.dp))
            .background(if (selectedCategoryFilter == null) PrimaryIndigo else SurfaceContainerLow)
            .border(1.dp, if (selectedCategoryFilter == null) PrimaryIndigo else BorderSubtle, RoundedCornerShape(9999.dp))
            .clickable { selectedCategoryFilter = null }
            .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
          Text(
            text = "All",
            color = if (selectedCategoryFilter == null) Color.White else TextSecondary,
            fontSize = 11.sp,
            fontWeight = if (selectedCategoryFilter == null) FontWeight.SemiBold else FontWeight.Normal
          )
        }

        listOf(CategoryTags.SOCIAL, CategoryTags.ENTERTAINMENT, CategoryTags.PRODUCTIVITY, CategoryTags.NEWS).forEach { cat ->
          val isSelected = selectedCategoryFilter == cat
          val catColor = Color(CategoryTags.getCategoryAccentHex(cat))
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(9999.dp))
              .background(if (isSelected) catColor.copy(alpha = 0.22f) else SurfaceContainerLow)
              .border(1.dp, if (isSelected) catColor else BorderSubtle, RoundedCornerShape(9999.dp))
              .clickable {
                selectedCategoryFilter = if (selectedCategoryFilter == cat) null else cat
              }
              .padding(horizontal = 10.dp, vertical = 5.dp)
          ) {
            Text(
              text = cat,
              color = if (isSelected) catColor else TextSecondary,
              fontSize = 11.sp,
              fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
            )
          }
        }
      }

      if (selectedCategoryFilter != null && onToggleCategory != null) {
        val catName = selectedCategoryFilter!!
        val catApps = apps.filter { CategoryTags.normalize(it.category) == catName }
        val isAllRestricted = catApps.isNotEmpty() && catApps.all { it.isGated }
        val catColor = Color(CategoryTags.getCategoryAccentHex(catName))

        Spacer(modifier = Modifier.height(6.dp))
        GlassCard(
          modifier = Modifier.fillMaxWidth(),
          backgroundColor = SurfaceContainerLow.copy(alpha = 0.75f),
          borderColor = catColor.copy(alpha = 0.35f),
          cornerRadius = 10.dp
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "$catName (${catApps.count { it.isGated }}/${catApps.size} restricted)",
              color = TextPrimary,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = if (isAllRestricted) "Unrestrict All" else "Restrict All",
                color = catColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                  onToggleCategory(catName, !isAllRestricted)
                }
              )
              Spacer(modifier = Modifier.width(8.dp))
              GlassSwitch(
                checked = isAllRestricted,
                onCheckedChange = { shouldGateAll ->
                  onToggleCategory(catName, shouldGateAll)
                }
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // App List
      LazyColumn(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(filteredApps, key = { it.packageName }) { app ->
          AppPickerItem(
            app = app,
            onToggle = { onToggleApp(app.packageName, it) }
          )
        }

        item {
          Spacer(modifier = Modifier.height(8.dp))
          // Grace window info
          GlassCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = SurfaceContainerLow.copy(alpha = 0.6f),
            cornerRadius = 14.dp
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = null,
                tint = PrimaryIndigo,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "Default 15-Minute Grace Window",
                  color = TextPrimary,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.SemiBold
                )
                Text(
                  text = "Once you complete a pause, you have free access for 15 minutes without re-triggering.",
                  color = TextSecondary,
                  fontSize = 11.sp
                )
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Primary CTA
    PrimaryGlassButton(
      text = "Complete Setup & Activate Shield",
      icon = Icons.Default.Shield,
      onClick = onFinish,
      modifier = Modifier.testTag("app_picker_finish_btn")
    )

    Spacer(modifier = Modifier.height(8.dp))
    }
  }
}

@Composable
private fun AppPickerItem(
  app: GatedAppEntity,
  onToggle: (Boolean) -> Unit
) {
  GlassCard(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onToggle(!app.isGated) },
    backgroundColor = if (app.isGated) SurfaceContainerHigh else SurfaceContainerLow.copy(alpha = 0.6f),
    borderColor = if (app.isGated) PrimaryIndigo.copy(alpha = 0.35f) else BorderSubtle,
    cornerRadius = 14.dp
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
            .size(38.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (app.isGated) PrimaryIndigo.copy(alpha = 0.2f) else SurfaceContainer)
            .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp)),
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
            modifier = Modifier.size(20.dp)
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
          }
          Text(
            text = "${CategoryTags.normalize(app.category)} • ~${app.dailyOpensCount} opens/day",
            color = TextSecondary,
            fontSize = 11.sp
          )
        }
      }

      GlassSwitch(
        checked = app.isGated,
        onCheckedChange = onToggle
      )
    }
  }
}
