package com.example.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GatekeeperRepository
import com.example.data.model.CategoryTags
import com.example.data.model.GatedAppEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class InstalledAppItem(
  val packageName: String,
  val appName: String,
  val category: String,
  val isGated: Boolean,
  val icon: Drawable? = null,
  val isSystemApp: Boolean = false,
  val estimatedOpensPerDay: Int = 20,
  val defaultChallengeType: String = "MATH"
)

class AppPickerViewModel(
  private val repository: GatekeeperRepository,
  private val context: Context
) : ViewModel() {

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _filterGatedOnly = MutableStateFlow(false)
  val filterGatedOnly: StateFlow<Boolean> = _filterGatedOnly.asStateFlow()

  private val _selectedCategoryFilter = MutableStateFlow<String?>(null)
  val selectedCategoryFilter: StateFlow<String?> = _selectedCategoryFilter.asStateFlow()

  private val _rawInstalledApps = MutableStateFlow<List<InstalledAppItem>>(emptyList())
  val isLoading = MutableStateFlow(true)

  val displayApps: StateFlow<List<InstalledAppItem>> = combine(
    _rawInstalledApps,
    repository.allApps,
    _searchQuery,
    _filterGatedOnly,
    _selectedCategoryFilter
  ) { installed, dbApps, query, gatedOnly, categoryFilter ->
    val dbMap = dbApps.associateBy { it.packageName }
    installed.map { item ->
      val dbEntity = dbMap[item.packageName]
      item.copy(
        category = dbEntity?.category ?: item.category,
        isGated = dbEntity?.isGated ?: false,
        defaultChallengeType = dbEntity?.challengeType ?: item.defaultChallengeType
      )
    }.filter { item ->
      val matchesQuery = query.isBlank() ||
        item.appName.contains(query, ignoreCase = true) ||
        item.packageName.contains(query, ignoreCase = true) ||
        item.category.contains(query, ignoreCase = true)
      val matchesFilter = !gatedOnly || item.isGated
      val matchesCategory = categoryFilter == null ||
        CategoryTags.normalize(item.category).equals(categoryFilter, ignoreCase = true)
      matchesQuery && matchesFilter && matchesCategory
    }.sortedWith(
      compareByDescending<InstalledAppItem> { it.isGated }
        .thenBy { it.appName.lowercase() }
    )
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  init {
    loadInstalledApps()
  }

  fun onSearchQueryChanged(newQuery: String) {
    _searchQuery.value = newQuery
  }

  fun toggleFilterGatedOnly() {
    _filterGatedOnly.value = !_filterGatedOnly.value
  }

  fun setCategoryFilter(category: String?) {
    _selectedCategoryFilter.value = if (_selectedCategoryFilter.value == category) null else category
  }

  fun toggleCategoryGated(category: String, shouldGate: Boolean) {
    viewModelScope.launch {
      repository.toggleCategoryGated(category, shouldGate)
    }
  }

  fun loadInstalledApps() {
    viewModelScope.launch {
      isLoading.value = true
      val list = withContext(Dispatchers.IO) {
        val pm = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
          addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val resolveInfos = pm.queryIntentActivities(mainIntent, 0)
        val ownPackage = context.packageName

        val apps = mutableListOf<InstalledAppItem>()
        for (info in resolveInfos) {
          val pkg = info.activityInfo.packageName
          if (pkg == ownPackage) continue

          val appName = info.loadLabel(pm).toString()
          val icon = try {
            info.loadIcon(pm)
          } catch (e: Exception) {
            null
          }
          val appInfo = info.activityInfo.applicationInfo
          val isSystem = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0

          val category = categorizeApp(pkg, appName)
          apps.add(
            InstalledAppItem(
              packageName = pkg,
              appName = appName,
              category = category,
              isGated = false,
              icon = icon,
              isSystemApp = isSystem
            )
          )
        }

        // If running in an emulator or environment with few apps, add common habit apps
        if (apps.size < 5) {
          val defaultHabits = listOf(
            InstalledAppItem("com.instagram.android", "Instagram", CategoryTags.SOCIAL, true),
            InstalledAppItem("com.zhiliaoapp.musically", "TikTok", CategoryTags.ENTERTAINMENT, true, defaultChallengeType = "BREATHING"),
            InstalledAppItem("com.twitter.android", "X / Twitter", CategoryTags.NEWS, true),
            InstalledAppItem("com.reddit.frontpage", "Reddit", CategoryTags.SOCIAL, true),
            InstalledAppItem("com.google.android.youtube", "YouTube", CategoryTags.ENTERTAINMENT, true),
            InstalledAppItem("com.android.chrome", "Chrome", CategoryTags.PRODUCTIVITY, false)
          )
          for (defaultApp in defaultHabits) {
            if (apps.none { it.packageName == defaultApp.packageName }) {
              apps.add(defaultApp)
            }
          }
        }

        apps
      }
      _rawInstalledApps.value = list
      isLoading.value = false
    }
  }

  fun toggleAppGated(pkg: String, shouldGate: Boolean, appName: String, category: String) {
    viewModelScope.launch {
      repository.toggleAppGated(pkg, shouldGate, appName, category)
    }
  }

  private fun categorizeApp(pkg: String, name: String): String {
    return CategoryTags.normalize(
      when {
        pkg.contains("instagram", ignoreCase = true) || pkg.contains("facebook", ignoreCase = true) ||
          pkg.contains("snapchat", ignoreCase = true) || pkg.contains("whatsapp", ignoreCase = true) -> CategoryTags.SOCIAL
        pkg.contains("tiktok", ignoreCase = true) || pkg.contains("youtube", ignoreCase = true) ||
          pkg.contains("netflix", ignoreCase = true) || pkg.contains("spotify", ignoreCase = true) -> CategoryTags.ENTERTAINMENT
        pkg.contains("chrome", ignoreCase = true) || pkg.contains("browser", ignoreCase = true) ||
          pkg.contains("mail", ignoreCase = true) || pkg.contains("slack", ignoreCase = true) -> CategoryTags.PRODUCTIVITY
        pkg.contains("twitter", ignoreCase = true) || pkg.contains("news", ignoreCase = true) -> CategoryTags.NEWS
        else -> CategoryTags.normalize(name)
      }
    )
  }
}
