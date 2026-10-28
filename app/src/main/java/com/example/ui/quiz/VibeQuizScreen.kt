package com.example.ui.quiz

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SoulSampleData
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
fun VibeQuizScreen(
    viewModel: SoulViewModel,
    onNavigateToDiscovery: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentStep by viewModel.quizStep.collectAsState()
    val isCompleted by viewModel.quizCompleted.collectAsState()
    val userArchetype by viewModel.userArchetype.collectAsState()
    val totalQuestions = SoulSampleData.quizQuestions.size

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SoulObsidian)
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 96.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        if (!isCompleted) {
            val question = SoulSampleData.quizQuestions[currentStep.coerceIn(0, totalQuestions - 1)]

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SOUL SYNERGY TEST",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = RoseGold,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = "Step ${currentStep + 1} of $totalQuestions",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                        )
                    }

                    LinearProgressIndicator(
                        progress = { (currentStep + 1) / totalQuestions.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = RoseGold,
                        trackColor = SoulMidnight
                    )
                }
            }

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
                        modifier = Modifier.padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = question.question,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                lineHeight = 24.sp
                            )
                        )

                        Text(
                            text = "Choose the response that resonates deepest with your inner truth:",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }
                }
            }

            items(question.options.size) { idx ->
                val option = question.options[idx]
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .border(1.dp, SoulBorder, RoundedCornerShape(18.dp))
                        .clickable { viewModel.selectQuizOption(question.id, option) }
                        .testTag("quiz_option_${question.id}_$idx"),
                    color = SoulMidnight
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(DeepVioletContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${('A'.code + idx).toChar()}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = RoseGold
                                )
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = option.text,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = TextPrimary,
                                    lineHeight = 20.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${option.archetypeTrait} • ${option.synergyBoost}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = VividViolet,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }
            }
        } else {
            // Results screen
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(AmethystViolet, NeonPink, RoseGold)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = TextPrimary,
                            modifier = Modifier.size(46.dp)
                        )
                    }

                    Text(
                        text = "Soul Resonance Calibrated!",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                    )

                    Text(
                        text = "Your Core Archetype:",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )

                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.5.dp, RoseGold, RoundedCornerShape(20.dp)),
                        color = DeepVioletContainer
                    ) {
                        Text(
                            text = "✨ $userArchetype ✨",
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                    }

                    Text(
                        text = "Your soul frequencies have unlocked enhanced synergy scores across discovery candidates with matching emotional and spiritual wavelengths.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = onNavigateToDiscovery,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("explore_soulmates_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AmethystViolet,
                            contentColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Explore Calibrated Soulmates",
                            modifier = Modifier.padding(vertical = 6.dp),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    OutlinedButton(
                        onClick = { viewModel.resetQuiz() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("retake_quiz_button"),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.RestartAlt,
                            contentDescription = null,
                            tint = RoseGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(
                            text = "Retake Synergy Test",
                            color = RoseGold
                        )
                    }
                }
            }
        }
    }
}
