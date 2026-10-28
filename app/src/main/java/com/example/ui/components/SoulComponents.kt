package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CompatibilityBreakdown
import com.example.model.SoulUser
import com.example.ui.theme.AmethystViolet
import com.example.ui.theme.NeonPink
import com.example.ui.theme.RoseGold
import com.example.ui.theme.SoulBorder
import com.example.ui.theme.SoulMidnight
import com.example.ui.theme.SoulSurface
import com.example.ui.theme.StatusOnline
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VividViolet
import kotlin.random.Random

@Composable
fun CompatibilityBadge(
    score: Int,
    modifier: Modifier = Modifier,
    isLarge: Boolean = false
) {
    val gradient = Brush.linearGradient(
        listOf(AmethystViolet, NeonPink, RoseGold)
    )

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, Brush.horizontalGradient(listOf(AmethystViolet.copy(alpha = 0.6f), RoseGold.copy(alpha = 0.6f))), RoundedCornerShape(20.dp)),
        color = SoulMidnight.copy(alpha = 0.85f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = if (isLarge) 12.dp else 8.dp, vertical = if (isLarge) 6.dp else 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = RoseGold,
                modifier = Modifier.size(if (isLarge) 16.dp else 12.dp)
            )
            Text(
                text = "$score% Synergy",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isLarge) 13.sp else 11.sp,
                    color = TextPrimary
                )
            )
        }
    }
}

@Composable
fun SoulAvatar(
    user: SoulUser,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    showOnlineDot: Boolean = true,
    showGlowRing: Boolean = false
) {
    val brushColors = user.avatarGradient.map { Color(it) }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        if (showGlowRing) {
            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
            val pulseScale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 1.15f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1400, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulseScale"
            )

            Box(
                modifier = Modifier
                    .size(size * pulseScale)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(AmethystViolet.copy(alpha = 0.45f), Color.Transparent)
                        )
                    )
            )
        }

        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(Brush.linearGradient(brushColors))
                .border(1.5.dp, RoseGold.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            val initials = user.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
            Text(
                text = initials,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = (size.value * 0.38f).sp
            )
        }

        if (showOnlineDot && user.isOnline) {
            Box(
                modifier = Modifier
                    .size((size.value * 0.28f).coerceAtLeast(10f).dp)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(StatusOnline)
                    .border(2.dp, SoulMidnight, CircleShape)
            )
        }
    }
}

@Composable
fun CompatibilityBreakdownCard(
    breakdown: CompatibilityBreakdown,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SoulSurface)
            .border(1.dp, SoulBorder.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Compatibility Matrix",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
            )
            Text(
                text = "Multi-Dimensional",
                style = MaterialTheme.typography.labelSmall.copy(color = RoseGold)
            )
        }

        BreakdownRow(label = "Emotional Harmony", percent = breakdown.emotional, color = RoseGold)
        BreakdownRow(label = "Intellectual Resonance", percent = breakdown.intellectual, color = AmethystViolet)
        BreakdownRow(label = "Spiritual Depth", percent = breakdown.spiritual, color = VividViolet)
        BreakdownRow(label = "Lifestyle Rhythm", percent = breakdown.lifestyle, color = NeonPink)
    }
}

@Composable
private fun BreakdownRow(
    label: String,
    percent: Int,
    color: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
            Text(text = "$percent%", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = color))
        }
        LinearProgressIndicator(
            progress = { percent / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = SoulMidnight,
            strokeCap = StrokeCap.Round
        )
    }
}

@Composable
fun AudioWaveformVisualizer(
    isPulsing: Boolean = true,
    barCount: Int = 20,
    modifier: Modifier = Modifier,
    barColor: Color = RoseGold
) {
    val infiniteTransition = rememberInfiniteTransition(label = "waveform")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wavePhase"
    )

    Canvas(modifier = modifier) {
        val totalWidth = size.width
        val barWidth = (totalWidth / (barCount * 1.5f))
        val maxHeight = size.height

        for (i in 0 until barCount) {
            val progress = i.toFloat() / barCount
            val wave = if (isPulsing) {
                kotlin.math.abs(kotlin.math.sin((progress * 4f + phase).toDouble())).toFloat()
            } else {
                0.2f
            }
            val barHeight = ((wave * 0.8f + 0.2f) * maxHeight).coerceIn(4f, maxHeight)
            val left = i * (barWidth * 1.5f)
            val top = (maxHeight - barHeight) / 2f

            drawRoundRect(
                color = barColor,
                topLeft = Offset(left, top),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(barWidth / 2, barWidth / 2)
            )
        }
    }
}
