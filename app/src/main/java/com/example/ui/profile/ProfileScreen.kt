package com.example.ui.profile

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.model.CallType
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
fun ProfileScreen(
    viewModel: SoulViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val userArchetype by viewModel.userArchetype.collectAsState()

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }
    var hasMicPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        hasCameraPermission = results[Manifest.permission.CAMERA] ?: hasCameraPermission
        hasMicPermission = results[Manifest.permission.RECORD_AUDIO] ?: hasMicPermission
    }

    var minSynergyThreshold by remember { mutableFloatStateOf(85f) }
    var autoAnswerVibeCalls by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SoulObsidian)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Header Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(
                        1.dp,
                        Brush.horizontalGradient(listOf(AmethystViolet.copy(alpha = 0.5f), RoseGold.copy(alpha = 0.5f))),
                        RoundedCornerShape(24.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = SoulSurface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(AmethystViolet, NeonPink, RoseGold)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "YOU",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Cosmic Traveler",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Verified Soul Explorer • Scorpio ♏",
                            style = MaterialTheme.typography.bodySmall.copy(color = RoseGold)
                        )
                    }

                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, RoseGold.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
                        color = DeepVioletContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
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
                                text = "Archetype: $userArchetype",
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

        // Test Call Simulator Actions
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, SoulBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = SoulSurface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = RoseGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Experience Calling Simulation",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                    }

                    Text(
                        text = "Trigger a simulated incoming soulmate call to preview ringtones, video interfaces, and in-call icebreakers:",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.triggerSimulatedIncomingCall(callType = CallType.VIDEO) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("simulate_incoming_video_call"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonPink,
                                contentColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Video Call", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                        }

                        Button(
                            onClick = { viewModel.triggerSimulatedIncomingCall(callType = CallType.VOICE) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("simulate_incoming_voice_call"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AmethystViolet,
                                contentColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Voice Call", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }
        }

        // Hardware Permissions Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, SoulBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = SoulSurface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Real-Time Calling Permissions",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )

                    // Camera Status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, tint = RoseGold)
                            Column {
                                Text(text = "Camera Access", style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary))
                                Text(
                                    text = if (hasCameraPermission) "Enabled for video calls" else "Required for video dates",
                                    style = MaterialTheme.typography.labelSmall.copy(color = if (hasCameraPermission) StatusOnline else TextMuted)
                                )
                            }
                        }

                        if (!hasCameraPermission) {
                            Button(
                                onClick = {
                                    permissionLauncher.launch(
                                        arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                                    )
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AmethystViolet)
                            ) {
                                Text("Grant", style = MaterialTheme.typography.labelSmall)
                            }
                        } else {
                            Icon(imageVector = Icons.Default.Check, contentDescription = "Granted", tint = StatusOnline)
                        }
                    }

                    // Mic Status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Mic, contentDescription = null, tint = RoseGold)
                            Column {
                                Text(text = "Microphone Access", style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary))
                                Text(
                                    text = if (hasMicPermission) "Enabled for voice intimacy" else "Required for audio notes & calls",
                                    style = MaterialTheme.typography.labelSmall.copy(color = if (hasMicPermission) StatusOnline else TextMuted)
                                )
                            }
                        }

                        if (!hasMicPermission) {
                            Button(
                                onClick = {
                                    permissionLauncher.launch(
                                        arrayOf(Manifest.permission.RECORD_AUDIO)
                                    )
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AmethystViolet)
                            ) {
                                Text("Grant", style = MaterialTheme.typography.labelSmall)
                            }
                        } else {
                            Icon(imageVector = Icons.Default.Check, contentDescription = "Granted", tint = StatusOnline)
                        }
                    }
                }
            }
        }

        // Soul Discovery Preferences
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, SoulBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = SoulSurface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Synergy Threshold Settings",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Minimum Soul Synergy", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                        Text(
                            text = "${minSynergyThreshold.toInt()}%",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = RoseGold)
                        )
                    }

                    Slider(
                        value = minSynergyThreshold,
                        onValueChange = { minSynergyThreshold = it },
                        valueRange = 70f..95f,
                        steps = 5,
                        colors = SliderDefaults.colors(
                            thumbColor = RoseGold,
                            activeTrackColor = AmethystViolet,
                            inactiveTrackColor = SoulMidnight
                        )
                    )
                }
            }
        }

        // About SoulSync
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "SoulSync v1.0",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = RoseGold
                    )
                )
                Text(
                    text = "Connecting celestial wavelengths through authentic voice & video intimacy.",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted, fontSize = 11.sp)
                )
            }
        }
    }
}
