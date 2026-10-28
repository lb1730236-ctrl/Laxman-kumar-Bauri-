package com.example.ui.calling

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SoulSampleData
import com.example.model.ActiveCallSession
import com.example.model.CallState
import com.example.model.CallType
import com.example.ui.components.AudioWaveformVisualizer
import com.example.ui.components.CompatibilityBadge
import com.example.ui.components.SoulAvatar
import com.example.ui.theme.ActiveCallGreen
import com.example.ui.theme.AmethystViolet
import com.example.ui.theme.DeepVioletContainer
import com.example.ui.theme.EndCallRed
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
import java.util.Locale

@Composable
fun CallingScreen(
    session: ActiveCallSession,
    viewModel: SoulViewModel,
    modifier: Modifier = Modifier
) {
    // Intercept back button to end call gracefully
    BackHandler {
        viewModel.endCall()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SoulObsidian)
    ) {
        when (session.state) {
            CallState.OUTGOING_RINGING -> {
                OutgoingCallView(session = session, onEndCall = { viewModel.endCall() })
            }
            CallState.INCOMING_RINGING -> {
                IncomingCallView(
                    session = session,
                    onAccept = { viewModel.acceptIncomingCall() },
                    onDecline = { viewModel.declineIncomingCall() }
                )
            }
            CallState.CONNECTED -> {
                ActiveCallView(
                    session = session,
                    onToggleMute = { viewModel.toggleMute() },
                    onToggleVideo = { viewModel.toggleVideo() },
                    onToggleCamera = { viewModel.toggleCamera() },
                    onToggleSpeaker = { viewModel.toggleSpeaker() },
                    onNextIcebreaker = { viewModel.nextInCallIcebreaker() },
                    onEndCall = { viewModel.endCall() }
                )
            }
            CallState.ENDED -> {
                CallEndedView(session = session)
            }
            CallState.IDLE -> {
                // Handled externally
            }
        }
    }
}

@Composable
fun OutgoingCallView(
    session: ActiveCallSession,
    onEndCall: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "outgoingRings")
    val ringScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ringScale"
    )
    val ringAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ringAlpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = RoseGold,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "End-to-End Soul Encrypted",
                    style = MaterialTheme.typography.labelSmall.copy(color = RoseGold)
                )
            }
            Text(
                text = if (session.callType == CallType.VIDEO) "Outgoing Video Call" else "Outgoing Voice Call",
                style = MaterialTheme.typography.titleMedium.copy(color = TextSecondary)
            )
            Text(
                text = "Connecting celestial wavelengths...",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
            )
        }

        // Center Pulsing Avatar
        Box(
            modifier = Modifier.size(240.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = AmethystViolet.copy(alpha = ringAlpha),
                    radius = (size.minDimension / 2f) * (ringScale / 1.6f)
                )
                drawCircle(
                    color = NeonPink.copy(alpha = ringAlpha * 0.7f),
                    radius = (size.minDimension / 2.3f) * (ringScale / 1.6f)
                )
            }

            SoulAvatar(
                user = session.peerUser,
                size = 120.dp,
                showOnlineDot = false
            )
        }

        // Peer Name & Compatibility
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = session.peerUser.name,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
            CompatibilityBadge(score = session.peerUser.compatibilityScore, isLarge = true)
            Text(
                text = session.peerUser.location,
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
            )
        }

        // End Call Button
        IconButton(
            onClick = onEndCall,
            modifier = Modifier
                .size(68.dp)
                .clip(CircleShape)
                .background(EndCallRed)
                .testTag("cancel_outgoing_call_button")
        ) {
            Icon(
                imageVector = Icons.Default.CallEnd,
                contentDescription = "Cancel Call",
                tint = TextPrimary,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun IncomingCallView(
    session: ActiveCallSession,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = if (session.callType == CallType.VIDEO) "Incoming Soul Video Call" else "Incoming Soul Voice Call",
                style = MaterialTheme.typography.titleMedium.copy(color = RoseGold, fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Your frequencies are harmonizing...",
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
            )
        }

        // Avatar
        SoulAvatar(
            user = session.peerUser,
            size = 130.dp,
            showGlowRing = true
        )

        // Peer Details
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = session.peerUser.name,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
            Text(
                text = "Soul Archetype: ${session.peerUser.soulArchetype}",
                style = MaterialTheme.typography.bodyMedium.copy(color = VividViolet)
            )
            CompatibilityBadge(score = session.peerUser.compatibilityScore, isLarge = true)
        }

        // Accept & Decline Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Decline Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = onDecline,
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(EndCallRed)
                        .testTag("decline_incoming_call_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CallEnd,
                        contentDescription = "Decline",
                        tint = TextPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Decline", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
            }

            // Accept Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = onAccept,
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(ActiveCallGreen)
                        .testTag("accept_incoming_call_button")
                ) {
                    Icon(
                        imageVector = if (session.callType == CallType.VIDEO) Icons.Default.Videocam else Icons.Default.Call,
                        contentDescription = "Accept",
                        tint = TextPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Connect", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
            }
        }
    }
}

@Composable
fun ActiveCallView(
    session: ActiveCallSession,
    onToggleMute: () -> Unit,
    onToggleVideo: () -> Unit,
    onToggleCamera: () -> Unit,
    onToggleSpeaker: () -> Unit,
    onNextIcebreaker: () -> Unit,
    onEndCall: () -> Unit
) {
    var showIcebreakerDrawer by remember { mutableStateOf(true) }

    val minutes = session.durationSeconds / 60
    val seconds = session.durationSeconds % 60
    val timerText = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

    Box(modifier = Modifier.fillMaxSize()) {
        if (session.callType == CallType.VIDEO && session.isVideoOn) {
            // Simulated Remote Video Feed with breathing ambiance
            RemoteVideoSimulationView(user = session.peerUser)

            // Local Camera Picture-in-Picture Preview (Top-Right)
            LocalCameraPipPreview(
                isFrontCamera = session.isFrontCamera,
                isVideoOn = session.isVideoOn,
                onFlip = onToggleCamera,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(top = 16.dp, end = 16.dp)
            )
        } else {
            // Voice Call Ambient Canvas
            VoiceCallAmbianceView(
                user = session.peerUser,
                isMuted = session.isMuted
            )
        }

        // Top Status Header
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, SoulBorder, RoundedCornerShape(16.dp)),
                color = SoulMidnight.copy(alpha = 0.85f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(ActiveCallGreen)
                    )
                    Text(
                        text = timerText,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                }
            }

            // Icebreaker Toggle Button
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { showIcebreakerDrawer = !showIcebreakerDrawer }
                    .border(1.dp, RoseGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                color = DeepVioletContainer.copy(alpha = 0.85f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
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
                        text = if (showIcebreakerDrawer) "Hide Prompt" else "Deep Prompt",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                }
            }
        }

        // Bottom Controls Container
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 20.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // In-Call Icebreaker Card Overlay
            AnimatedVisibility(
                visible = showIcebreakerDrawer,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                InCallIcebreakerCard(
                    prompt = SoulSampleData.deepIcebreakers[session.currentIcebreakerIndex % SoulSampleData.deepIcebreakers.size],
                    onNext = onNextIcebreaker
                )
            }

            // Call Controls Dock
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(32.dp))
                    .border(1.dp, SoulBorder, RoundedCornerShape(32.dp)),
                color = SoulMidnight.copy(alpha = 0.92f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Mute Toggle
                    IconButton(
                        onClick = onToggleMute,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(if (session.isMuted) EndCallRed else SoulSurface)
                            .testTag("call_toggle_mute")
                    ) {
                        Icon(
                            imageVector = if (session.isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = "Toggle Mute",
                            tint = TextPrimary
                        )
                    }

                    // Video Camera Toggle (if video call)
                    if (session.callType == CallType.VIDEO) {
                        IconButton(
                            onClick = onToggleVideo,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(if (!session.isVideoOn) SoulSurfaceVariant else SoulSurface)
                                .testTag("call_toggle_video")
                        ) {
                            Icon(
                                imageVector = if (session.isVideoOn) Icons.Default.Videocam else Icons.Default.VideocamOff,
                                contentDescription = "Toggle Video",
                                tint = if (session.isVideoOn) NeonPink else TextMuted
                            )
                        }

                        // Switch Camera (Front/Back)
                        IconButton(
                            onClick = onToggleCamera,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(SoulSurface)
                                .testTag("call_toggle_camera")
                        ) {
                            Icon(
                                imageVector = Icons.Default.FlipCameraAndroid,
                                contentDescription = "Switch Camera",
                                tint = RoseGold
                            )
                        }
                    }

                    // Speakerphone Toggle
                    IconButton(
                        onClick = onToggleSpeaker,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(if (session.isSpeakerOn) AmethystViolet else SoulSurface)
                            .testTag("call_toggle_speaker")
                    ) {
                        Icon(
                            imageVector = if (session.isSpeakerOn) Icons.Default.VolumeUp else Icons.Default.VolumeDown,
                            contentDescription = "Toggle Speaker",
                            tint = TextPrimary
                        )
                    }

                    // Red End Call Button
                    IconButton(
                        onClick = onEndCall,
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(EndCallRed)
                            .testTag("call_end_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CallEnd,
                            contentDescription = "End Call",
                            tint = TextPrimary,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RemoteVideoSimulationView(
    user: com.example.model.SoulUser
) {
    val infiniteTransition = rememberInfiniteTransition(label = "videoAtmosphere")
    val ambientShift by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ambientShift"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    listOf(
                        Color(0xFF2E1A47),
                        SoulObsidian
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(bottom = 80.dp)
        ) {
            SoulAvatar(
                user = user,
                size = (160 * ambientShift).dp,
                showOnlineDot = false,
                showGlowRing = true
            )
            Text(
                text = user.name,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(ActiveCallGreen)
                )
                Text(
                    text = "HD 1080p Video • Live",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                )
            }
        }
    }
}

@Composable
fun LocalCameraPipPreview(
    isFrontCamera: Boolean,
    isVideoOn: Boolean,
    onFlip: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(105.dp)
            .height(145.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(2.dp, RoseGold, RoundedCornerShape(16.dp))
            .background(SoulSurfaceVariant)
            .clickable { onFlip() },
        contentAlignment = Alignment.Center
    ) {
        if (isVideoOn) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(AmethystViolet),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "You",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
                Text(
                    text = if (isFrontCamera) "Front Cam" else "Rear Cam",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                )
            }
        } else {
            Icon(
                imageVector = Icons.Default.VideocamOff,
                contentDescription = "Video Muted",
                tint = TextMuted,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun VoiceCallAmbianceView(
    user: com.example.model.SoulUser,
    isMuted: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        SoulAvatar(
            user = user,
            size = 140.dp,
            showGlowRing = true
        )

        Text(
            text = user.name,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )

        Text(
            text = if (isMuted) "Your microphone is muted" else "Harmonizing voice audio...",
            style = MaterialTheme.typography.bodySmall.copy(
                color = if (isMuted) EndCallRed else RoseGold
            )
        )

        // Real-time audio waveform reacting to call
        AudioWaveformVisualizer(
            isPulsing = !isMuted,
            barCount = 26,
            modifier = Modifier
                .width(220.dp)
                .height(40.dp),
            barColor = if (isMuted) TextMuted else RoseGold
        )
    }
}

@Composable
fun InCallIcebreakerCard(
    prompt: String,
    onNext: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(
                1.5.dp,
                Brush.horizontalGradient(listOf(RoseGold, AmethystViolet)),
                RoundedCornerShape(20.dp)
            ),
        color = SoulMidnight.copy(alpha = 0.95f)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = RoseGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "DEEP IN-CALL QUESTION",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = RoseGold,
                            letterSpacing = 1.sp
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "\"$prompt\"",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 18.sp
                    )
                )
            }

            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onNext() }
                    .testTag("next_in_call_prompt_button"),
                color = AmethystViolet
            ) {
                Text(
                    text = "Next ➔",
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

@Composable
fun CallEndedView(
    session: ActiveCallSession
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CallEnd,
                contentDescription = null,
                tint = EndCallRed,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "Call Ended",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
            val minutes = session.durationSeconds / 60
            val seconds = session.durationSeconds % 60
            val timerText = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
            Text(
                text = "Duration: $timerText with ${session.peerUser.name}",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
            )
        }
    }
}
