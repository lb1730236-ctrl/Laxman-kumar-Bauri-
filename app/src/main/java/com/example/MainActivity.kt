package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CallType
import com.example.ui.calling.CallingScreen
import com.example.ui.chat.ChatConversationScreen
import com.example.ui.chat.ChatListScreen
import com.example.ui.discovery.DiscoveryScreen
import com.example.ui.profile.ProfileScreen
import com.example.ui.quiz.VibeQuizScreen
import com.example.ui.theme.AmethystViolet
import com.example.ui.theme.DeepVioletContainer
import com.example.ui.theme.RoseGold
import com.example.ui.theme.SoulBorder
import com.example.ui.theme.SoulMidnight
import com.example.ui.theme.SoulObsidian
import com.example.ui.theme.SoulSyncTheme
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VividViolet
import com.example.viewmodel.SoulViewModel

enum class NavigationTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    RADAR("Radar", Icons.Filled.Explore, Icons.Outlined.Explore, "tab_radar"),
    CHATS("Messages", Icons.Filled.ChatBubble, Icons.Outlined.ChatBubbleOutline, "tab_chats"),
    QUIZ("Vibe Quiz", Icons.Filled.AutoAwesome, Icons.Filled.AutoAwesome, "tab_quiz"),
    PROFILE("You", Icons.Filled.Person, Icons.Outlined.Person, "tab_profile")
}

class MainActivity : ComponentActivity() {

    private val viewModel: SoulViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SoulSyncTheme {
                SoulSyncMainApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun SoulSyncMainApp(
    viewModel: SoulViewModel
) {
    var currentTab by remember { mutableStateOf(NavigationTab.RADAR) }
    var activeChatMatchId by remember { mutableStateOf<String?>(null) }

    val activeCall by viewModel.activeCall.collectAsState()
    val bannerMessage by viewModel.callBannerMessage.collectAsState()
    val threads by viewModel.threads.collectAsState()
    val totalUnread = threads.sumOf { it.unreadCount }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = SoulObsidian,
            bottomBar = {
                if (activeChatMatchId == null && activeCall == null) {
                    NavigationBar(
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                            .border(1.dp, SoulBorder.copy(alpha = 0.5f), RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                        containerColor = SoulMidnight,
                        tonalElevation = 8.dp
                    ) {
                        NavigationTab.entries.forEach { tab ->
                            val isSelected = currentTab == tab
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { currentTab = tab },
                                icon = {
                                    if (tab == NavigationTab.CHATS && totalUnread > 0) {
                                        androidx.compose.material3.BadgedBox(
                                            badge = {
                                                androidx.compose.material3.Badge(
                                                    containerColor = RoseGold
                                                ) {
                                                    Text(text = "$totalUnread", color = SoulObsidian)
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                                contentDescription = tab.title,
                                                tint = if (isSelected) RoseGold else TextMuted
                                            )
                                        }
                                    } else {
                                        Icon(
                                            imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                            contentDescription = tab.title,
                                            tint = if (isSelected) RoseGold else TextMuted
                                        )
                                    }
                                },
                                label = {
                                    Text(
                                        text = tab.title,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) TextPrimary else TextMuted
                                        )
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = DeepVioletContainer
                                ),
                                modifier = Modifier.testTag(tab.testTag)
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (activeChatMatchId != null) {
                    ChatConversationScreen(
                        matchId = activeChatMatchId!!,
                        viewModel = viewModel,
                        onBack = { activeChatMatchId = null },
                        onStartCall = { user, callType ->
                            viewModel.startCall(user, callType)
                        }
                    )
                } else {
                    when (currentTab) {
                        NavigationTab.RADAR -> {
                            DiscoveryScreen(
                                viewModel = viewModel,
                                onNavigateToChat = { matchId -> activeChatMatchId = matchId },
                                onNavigateToQuiz = { currentTab = NavigationTab.QUIZ },
                                onStartCall = { user, callType ->
                                    viewModel.startCall(user, callType)
                                }
                            )
                        }
                        NavigationTab.CHATS -> {
                            ChatListScreen(
                                viewModel = viewModel,
                                onOpenThread = { matchId -> activeChatMatchId = matchId },
                                onStartCall = { user, callType ->
                                    viewModel.startCall(user, callType)
                                },
                                onNavigateToDiscovery = { currentTab = NavigationTab.RADAR }
                            )
                        }
                        NavigationTab.QUIZ -> {
                            VibeQuizScreen(
                                viewModel = viewModel,
                                onNavigateToDiscovery = { currentTab = NavigationTab.RADAR }
                            )
                        }
                        NavigationTab.PROFILE -> {
                            ProfileScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }

        // Active Full-Screen Voice / Video Call Layer
        AnimatedVisibility(
            visible = activeCall != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            activeCall?.let { session ->
                CallingScreen(
                    session = session,
                    viewModel = viewModel
                )
            }
        }

        // Animated Notification / Connection Banner
        AnimatedVisibility(
            visible = bannerMessage != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 12.dp, start = 20.dp, end = 20.dp)
        ) {
            bannerMessage?.let { msg ->
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, RoseGold, RoundedCornerShape(20.dp)),
                    color = SoulMidnight.copy(alpha = 0.95f),
                    tonalElevation = 10.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = RoseGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = msg,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }
    }
}
