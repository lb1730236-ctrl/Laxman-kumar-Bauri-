package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.SoulSampleData
import com.example.data.firestore.FirestoreChatMessage
import com.example.data.firestore.FirestoreUserProfile
import com.example.model.CallType
import com.example.model.CallState
import com.example.viewmodel.SoulViewModel
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
    assertEquals("SoulSync", appName)
  }

  @Test
  fun `verify soulmate candidates and calling session initialization`() {
    val viewModel = SoulViewModel()
    val users = viewModel.discoveryUsers.value
    assertTrue("Should have sample soulmates", users.isNotEmpty())

    val firstSoulmate = users.first()
    viewModel.startCall(firstSoulmate, CallType.VIDEO)

    val activeCall = viewModel.activeCall.value
    assertNotNull("Active call should be initialized", activeCall)
    assertEquals(CallState.OUTGOING_RINGING, activeCall?.state)
    assertEquals(CallType.VIDEO, activeCall?.callType)
    assertEquals(firstSoulmate.id, activeCall?.peerUser?.id)

    // Verify mute toggle
    viewModel.toggleMute()
    assertTrue("Call should be muted", viewModel.activeCall.value?.isMuted == true)

    // Verify end call
    viewModel.endCall()
  }

  @Test
  fun `verify firestore models default properties`() {
    val profile = FirestoreUserProfile(
      userId = "user_123",
      name = "Seraphina",
      soulArchetype = "Cosmic Mystic",
      compatibilityScore = 98
    )
    assertEquals("user_123", profile.userId)
    assertEquals("Seraphina", profile.name)

    val message = FirestoreChatMessage(
      userId = "user_123",
      matchId = "user_seraphina",
      senderId = "user_123",
      text = "Hello cosmic soul!"
    )
    assertEquals("user_seraphina", message.matchId)
    assertEquals("Hello cosmic soul!", message.text)
  }
}
