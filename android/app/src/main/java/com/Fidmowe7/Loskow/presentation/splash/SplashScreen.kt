package com.Fidmowe7.Loskow.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Fidmowe7.Loskow.R
import com.Fidmowe7.Loskow.core.di.ServiceLocator
import com.Fidmowe7.Loskow.core.theme.Gold
import com.Fidmowe7.Loskow.core.theme.LacquerBlack
import com.Fidmowe7.Loskow.core.theme.LacquerVoid
import com.Fidmowe7.Loskow.core.theme.Ruby
import com.Fidmowe7.Loskow.core.theme.TextMuted
import com.Fidmowe7.Loskow.core.theme.TextPrimary
import com.Fidmowe7.Loskow.core.theme.TextSecondary
import com.Fidmowe7.Loskow.core.theme.VelvetBrown

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val splashViewModel: SplashViewModel = viewModel(factory = ServiceLocator.factory)
    val state by splashViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.finished) {
        if (state.finished) {
            onFinished()
        }
    }

    val motion = rememberInfiniteTransition(label = "splashMotion")
    val glow by motion.animateFloat(
        initialValue = 0.22f,
        targetValue = 0.46f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )
    val ring by motion.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring"
    )
    val mote by motion.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "mote"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(LacquerVoid, LacquerBlack, VelvetBrown)
                )
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Ruby.copy(alpha = 0.20f), Color.Transparent),
                        radius = 760f
                    )
                )
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            val centre = Offset(size.width / 2f, size.height * 0.42f)
            for (index in 0 until 3) {
                val phase = (mote + index * 0.33f) % 1f
                val y = centre.y + 190f - phase * 320f
                val x = centre.x + (index - 1) * 86f
                drawCircle(
                    color = Ruby.copy(alpha = (1f - phase) * 0.45f),
                    radius = 4f + index,
                    center = Offset(x, y)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(240.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .size(240.dp)
                        .graphicsLayer { rotationZ = ring }
                ) {
                    drawCircle(
                        color = Gold.copy(alpha = 0.30f),
                        radius = size.minDimension / 2f - 2f,
                        style = Stroke(width = 2f)
                    )
                    drawArc(
                        color = Gold.copy(alpha = 0.75f),
                        startAngle = -40f,
                        sweepAngle = 80f,
                        useCenter = false,
                        style = Stroke(width = 4f)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(196.dp)
                        .alpha(glow)
                        .background(
                            Brush.radialGradient(
                                listOf(Ruby.copy(alpha = 0.72f), Color.Transparent)
                            ),
                            CircleShape
                        )
                )
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_brand_logo),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(168.dp)
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = stringResource(R.string.app_title),
                style = MaterialTheme.typography.displaySmall,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.splash_subtitle),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 11.sp,
                letterSpacing = 4.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(38.dp))

            LinearProgressIndicator(
                color = Gold,
                trackColor = Color(0xFF241F18),
                strokeCap = StrokeCap.Round,
                modifier = Modifier
                    .width(180.dp)
                    .height(4.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.splash_loading),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                letterSpacing = 5.sp,
                color = TextMuted
            )
        }

        Text(
            text = stringResource(R.string.splash_disclaimer),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 9.sp,
            color = Color(0xFF7A7465),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .safeDrawingPadding()
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp)
        )
    }
}
