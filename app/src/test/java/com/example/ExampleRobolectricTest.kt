package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.GatekeeperRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Gatekeeper", appName)
  }

  @Test
  fun `repository defaults and streak tracking`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repo = GatekeeperRepository(context)

    val streak = repo.currentStreak.value
    assertTrue("Streak should be >= 0", streak >= 0)

    val milestone = repo.milestoneMessage.value
    assertTrue("Milestone message should not be blank", milestone.isNotBlank())

    val apps = repo.allApps.first()
    assertNotNull("Apps list should be initialized", apps)
  }
}
