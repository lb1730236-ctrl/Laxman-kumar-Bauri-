package com.example.ui.discovery

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.CallType
import com.example.model.SoulUser
import com.example.ui.components.AudioWaveformVisualizer
import com.example.ui.components.CompatibilityBadge
import com.example.ui.components.CompatibilityBreakdownCard
import com.example.ui.components.SoulAvatar
import com.example.ui.theme.AmethystViolet
import com.example.ui.theme.DeepVioletContainer
import com.example.ui.theme.NeonPink
import com.example.ui.theme.RoseGold
import com.example.ui.theme.SoulBorder
import com.example.ui.theme.SoulMidnight
import com.example.ui.theme.SoulObsidian
import com.example.ui.theme.SoulSurface
import com.example.ui.theme.SoulSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VividViolet
import com.example.viewmodel.SoulViewModel

@Composable
fun DiscoveryScreen(
    viewModel: SoulViewModel,
    onNavigateToChat: (String) -> Unit,
    onNavigateToQuiz: () -> Unit,
    onStartCall: (SoulUser, CallType) -> Unit,
    modifier: Modifier = Modifier
) {
    val users = viewModel.getFilteredUsers()
    val currentFilter = viewModel.selectedFilter

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SoulObsidian)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Hero Header Banner
        item {
            HeroHeaderSection(onNavigateToQuiz = onNavigateToQuiz)
        }

        // Filter Chips Row
        item {
            FilterSection(
                selectedFilter = viewModel.selectedFilter.value,
                onSelectFilter = { viewModel.setFilter(it) }
            )
        }

        // Soulmate Cards
        items(users, key = { it.id }) { user ->
            SoulCandidateCard(
                user = user,
                onConnect = {
                    viewModel.connectWithSoulmate(user)
                    onNavigateToChat(user.id)
                },
                onStartCall = { type -> onStartCall(user, type) }
            )
        }
    }
}

@Composable
fun HeroHeaderSection(
    onNavigateToQuiz: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(22.dp))
            .border(1.dp, Brush.horizontalGradient(listOf(AmethystViolet.copy(alpha = 0.5f), RoseGold.copy(alpha = 0.5f))), RoundedCornerShape(22.dp))
    ) {
        // Hero Graphic
        Image(
            painter = painterResource(id = R.drawable.img_soul_banner),
            contentDescription = "Cosmic Soulmates Banner",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay for readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, SoulMidnight.copy(alpha = 0.88f), SoulObsidian)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = RoseGold,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "RESONANCE RADAR",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = RoseGold,
                        letterSpacing = 1.2.sp
                    )
                )
            }

            Text(
                text = "Discover Your True Soulmate",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Psychological, spiritual & lifestyle alignment",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )

                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onNavigateToQuiz() }
                        .testTag("take_vibe_quiz_button"),
                    color = AmethystViolet
                ) {
                    Text(
                        text = "Vibe Quiz ✨",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun FilterSection(
    selectedFilter: String,
    onSelectFilter: (String) -> Unit
) {
    val filters = listOf("All", ">90% Synergy", "Online Now")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { filter ->
            val isSelected = filter == selectedFilter
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .border(
                        1.dp,
                        if (isSelected) RoseGold else SoulBorder,
                        RoundedCornerShape(20.dp)
                    )
                    .clickable { onSelectFilter(filter) }
                    .testTag("filter_${filter.replace(" ", "_")}"),
                color = if (isSelected) DeepVioletContainer else SoulMidnight
            ) {
                Text(
                    text = filter,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) TextPrimary else TextSecondary
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SoulCandidateCard(
    user: SoulUser,
    onConnect: () -> Unit,
    onStartCall: (CallType) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isPlayingVoiceIntro by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, SoulBorder, RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = SoulSurface)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Row: Avatar + Info + Compatibility Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                SoulAvatar(
                    user = user,
                    size = 64.dp,
                    showGlowRing = user.compatibilityScore >= 95
                )

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "${user.name}, ${user.age}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = user.zodiac,
                            style = MaterialTheme.typography.bodySmall.copy(color = RoseGold)
                        )
                    }

                    Text(
                        text = user.location,
                        style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                    )

                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Archetype: ${user.soulArchetype}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = VividViolet
                        )
                    )
                }

                CompatibilityBadge(score = user.compatibilityScore, isLarge = true)
            }

            // Bio
            Text(
                text = user.bio,
                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary, lineHeight = 20.sp)
            )

            // Deep Prompt Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, SoulBorder.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                color = SoulMidnight
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = user.deepPromptQuestion,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = RoseGold,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "\"${user.deepPromptAnswer}\"",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextPrimary,
                            lineHeight = 18.sp
                        )
                    )
                }
            }

            // Audio Intro Preview Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { isPlayingVoiceIntro = !isPlayingVoiceIntro }
                    .border(1.dp, AmethystViolet.copy(alpha = 0.3f), RoundedCornerShape(14.dp)),
                color = SoulSurfaceVariant
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(if (isPlayingVoiceIntro) NeonPink else AmethystViolet),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlayingVoiceIntro) Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = "Play voice intro",
                            tint = TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isPlayingVoiceIntro) "Playing Soul Voice Note..." else "Voice Note Intro (${user.voiceIntroDurationSec}s)",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold)
                        )
                        AudioWaveformVisualizer(
                            isPulsing = isPlayingVoiceIntro,
                            barCount = 18,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp),
                            barColor = if (isPlayingVoiceIntro) NeonPink else RoseGold
                        )
                    }
                }
            }

            // Interests tags
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                user.interests.forEach { tag ->
                    Surface(
                        modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                        color = SoulMidnight
                    ) {
                        Text(
                            text = "#$tag",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                        )
                    }
                }
            }

            // Toggle Matrix Details
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isExpanded) "Hide Synergy Breakdown" else "View Synergy Breakdown",
                    style = MaterialTheme.typography.labelSmall.copy(color = RoseGold, fontWeight = FontWeight.Bold)
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = RoseGold
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                CompatibilityBreakdownCard(breakdown = user.breakdown)
            }

            // Action Buttons: Chat & Instant Calls!
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Connect & Chat Button
                Button(
                    onClick = onConnect,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("connect_chat_${user.id}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmethystViolet,
                        contentColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubble,
                        contentDescription = "Chat",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Connect",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                // Voice Call Quick Action
                IconButton(
                    onClick = { onStartCall(CallType.VOICE) },
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(SoulMidnight)
                        .border(1.dp, RoseGold.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                        .testTag("voice_call_${user.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Voice Call",
                        tint = RoseGold
                    )
                }

                // Video Call Quick Action
                IconButton(
                    onClick = { onStartCall(CallType.VIDEO) },
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(SoulMidnight)
                        .border(1.dp, NeonPink.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                        .testTag("video_call_${user.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Videocam,
                        contentDescription = "Video Call",
                        tint = NeonPink
                    )
                }
            }
        }
    }
}
