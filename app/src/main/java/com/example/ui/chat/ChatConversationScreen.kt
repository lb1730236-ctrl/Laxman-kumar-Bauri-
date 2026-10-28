package com.example.ui.chat

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SoulSampleData
import com.example.model.CallType
import com.example.model.ChatMessage
import com.example.model.MessageType
import com.example.model.SoulUser
import com.example.ui.components.AudioWaveformVisualizer
import com.example.ui.components.CompatibilityBadge
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
import com.example.ui.theme.StatusOnline
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VividViolet
import com.example.viewmodel.SoulViewModel

@Composable
fun ChatConversationScreen(
    matchId: String,
    viewModel: SoulViewModel,
    onBack: () -> Unit,
    onStartCall: (SoulUser, CallType) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val discoveryUsers by viewModel.discoveryUsers.collectAsState()
    val peerUser = discoveryUsers.find { it.id == matchId } ?: SoulSampleData.sampleUsers.first()

    val messagesMap by viewModel.messages.collectAsState()
    val chatMessages = messagesMap[matchId] ?: emptyList()

    val typingMap by viewModel.isPeerTyping.collectAsState()
    val isPeerTyping = typingMap[matchId] ?: false

    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(chatMessages.size, isPeerTyping) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SoulObsidian)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        // Conversation Top Bar
        ConversationTopBar(
            peer = peerUser,
            onBack = onBack,
            onStartCall = { type -> onStartCall(peerUser, type) }
        )

        // Deep Icebreakers Prompt Scroller
        IcebreakerPromptsRow(
            onSelectPrompt = { prompt ->
                viewModel.sendMessage(matchId, prompt, MessageType.ICEBREAKER)
            }
        )

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(chatMessages, key = { it.id }) { msg ->
                MessageBubble(message = msg)
            }

            if (isPeerTyping) {
                item {
                    PeerTypingBubble(peerName = peerUser.name.split(" ").first())
                }
            }
        }

        // Input Dock
        ChatInputBar(
            inputText = inputText,
            onTextChange = { inputText = it },
            onSend = {
                if (inputText.isNotBlank()) {
                    viewModel.sendMessage(matchId, inputText.trim(), MessageType.TEXT)
                    inputText = ""
                }
            },
            onSendVoiceNote = {
                viewModel.sendMessage(
                    matchId = matchId,
                    text = "Voice Memo",
                    type = MessageType.VOICE_NOTE,
                    voiceDurationSec = 14
                )
            }
        )
    }
}

@Composable
fun ConversationTopBar(
    peer: SoulUser,
    onBack: () -> Unit,
    onStartCall: (CallType) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = SoulMidnight,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("chat_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }

            SoulAvatar(
                user = peer,
                size = 44.dp
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = peer.name,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "${peer.compatibilityScore}%",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = RoseGold
                        )
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (peer.isOnline) StatusOnline else TextMuted)
                    )
                    Text(
                        text = if (peer.isOnline) "Resonating Online" else "Away in Thought",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (peer.isOnline) StatusOnline else TextMuted,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            // Voice Call Action
            IconButton(
                onClick = { onStartCall(CallType.VOICE) },
                modifier = Modifier.testTag("chat_action_voice_call")
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Start Voice Call",
                    tint = RoseGold
                )
            }

            // Video Call Action
            IconButton(
                onClick = { onStartCall(CallType.VIDEO) },
                modifier = Modifier.testTag("chat_action_video_call")
            ) {
                Icon(
                    imageVector = Icons.Default.Videocam,
                    contentDescription = "Start Video Call",
                    tint = NeonPink
                )
            }
        }
    }
}

@Composable
fun IcebreakerPromptsRow(
    onSelectPrompt: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = SoulObsidian
    ) {
        Column(modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = RoseGold,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "Soul Probing Icebreakers",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = RoseGold
                    )
                )
            }

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(SoulSampleData.deepIcebreakers) { prompt ->
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, AmethystViolet.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                            .clickable { onSelectPrompt(prompt) },
                        color = SoulSurface
                    ) {
                        Text(
                            text = prompt,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextPrimary,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MessageBubble(
    message: ChatMessage
) {
    val isMe = message.isFromMe
    var isVoicePlaying by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
    ) {
        if (message.messageType == MessageType.ICEBREAKER) {
            // Distinctive Soul Prompt card
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(18.dp))
                    .border(
                        1.5.dp,
                        Brush.horizontalGradient(listOf(RoseGold, AmethystViolet)),
                        RoundedCornerShape(18.dp)
                    ),
                color = DeepVioletContainer.copy(alpha = 0.85f)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = RoseGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "SOUL ICEBREAKER PROMPT",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = RoseGold,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "\"${message.text}\"",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextPrimary,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 20.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = message.timestamp,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextMuted,
                            fontSize = 10.sp
                        ),
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        } else if (message.messageType == MessageType.VOICE_NOTE) {
            // Voice note bubble
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .clip(
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp,
                            bottomStart = if (isMe) 18.dp else 4.dp,
                            bottomEnd = if (isMe) 4.dp else 18.dp
                        )
                    ),
                color = if (isMe) AmethystViolet else SoulSurface
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isMe) SoulObsidian else NeonPink)
                            .clickable { isVoicePlaying = !isVoicePlaying },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isVoicePlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = "Play voice note",
                            tint = TextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        AudioWaveformVisualizer(
                            isPulsing = isVoicePlaying,
                            barCount = 16,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp),
                            barColor = if (isMe) TextPrimary else RoseGold
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "0:${message.voiceDurationSec.toString().padStart(2, '0')}",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 10.sp)
                            )
                            Text(
                                text = message.timestamp,
                                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp)
                            )
                        }
                    }
                }
            }
        } else {
            // Standard Text bubble
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp,
                            bottomStart = if (isMe) 18.dp else 4.dp,
                            bottomEnd = if (isMe) 4.dp else 18.dp
                        )
                    )
                    .background(
                        if (isMe) {
                            Brush.linearGradient(listOf(AmethystViolet, NeonPink))
                        } else {
                            Brush.linearGradient(listOf(SoulSurface, SoulSurfaceVariant))
                        }
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Column {
                    Text(
                        text = message.text,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextPrimary,
                            lineHeight = 20.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = message.timestamp,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isMe) TextPrimary.copy(alpha = 0.7f) else TextMuted,
                            fontSize = 10.sp
                        ),
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    }
}

@Composable
fun PeerTypingBubble(peerName: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "typing")
    val dotAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dotAlpha"
    )

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(SoulSurface)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "$peerName is composing a thought",
            style = MaterialTheme.typography.bodySmall.copy(color = RoseGold, fontSize = 12.sp)
        )
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(RoseGold.copy(alpha = dotAlpha))
        )
    }
}

@Composable
fun ChatInputBar(
    inputText: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    onSendVoiceNote: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = SoulMidnight,
        tonalElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Voice Memo Button
            IconButton(
                onClick = onSendVoiceNote,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(SoulSurface)
                    .border(1.dp, RoseGold.copy(alpha = 0.3f), CircleShape)
                    .testTag("send_voice_note_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Send voice note",
                    tint = RoseGold,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Text input field
            OutlinedTextField(
                value = inputText,
                onValueChange = onTextChange,
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_text_input"),
                placeholder = {
                    Text(
                        text = "Speak to their soul...",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AmethystViolet,
                    unfocusedBorderColor = SoulBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = RoseGold
                ),
                shape = RoundedCornerShape(22.dp),
                maxLines = 4,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { onSend() })
            )

            // Send Button
            IconButton(
                onClick = onSend,
                enabled = inputText.isNotBlank(),
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (inputText.isNotBlank()) AmethystViolet else SoulSurfaceVariant)
                    .testTag("chat_send_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send message",
                    tint = if (inputText.isNotBlank()) TextPrimary else TextMuted,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
