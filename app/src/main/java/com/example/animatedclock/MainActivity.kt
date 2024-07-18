package com.example.animatedclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.LocalTime
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DateDisplay()
                Spacer(modifier = Modifier.height(32.dp))
                Clock()
            }
        }
    }
}

@Composable
fun DateDisplay() {
    val currentDate = LocalDate.now()
    val dayOfWeek = currentDate.dayOfWeek.name.lowercase().capitalize()
    val dayOfMonth = currentDate.dayOfMonth.toString()

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$dayOfWeek $dayOfMonth",
            color = Color(0xFFFF9C9C),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun Clock() {
    val currentTime = remember { mutableStateOf(LocalTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime.value = LocalTime.now()
            delay(1000L)
        }
    }

    val secondRotation = currentTime.value.second * 6f
    val minuteRotation = currentTime.value.minute * 6f + currentTime.value.second * 0.1f
    val hourRotation = currentTime.value.hour * 30f + currentTime.value.minute * 0.5f

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(200.dp)
    ) {
        Canvas(modifier = Modifier.size(200.dp)) {
            drawCircle(
                brush = Brush.sweepGradient(
                    colors = listOf(Color.Red, Color.Blue, Color.Red)
                ),
                style = Stroke(width = 4.dp.toPx())
            )
        }
        ClockHands(secondRotation, minuteRotation, hourRotation)
    }
}

@Composable
fun ClockHands(secondRotation: Float, minuteRotation: Float, hourRotation: Float) {
    Box(modifier = Modifier.size(200.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(200.dp)) {
            val center = Offset(size.width / 2, size.height / 2)
            val centerAdjustment = 10.dp.toPx()

            // Minute Hand
            drawLine(
                color = Color(0xFF87CEEB), // Sky blue color
                start = Offset(
                    center.x - centerAdjustment * kotlin.math.cos(Math.toRadians(minuteRotation.toDouble() - 90)).toFloat(),
                    center.y - centerAdjustment * kotlin.math.sin(Math.toRadians(minuteRotation.toDouble() - 90)).toFloat()
                ),
                end = Offset(
                    center.x + 60.dp.toPx() * kotlin.math.cos(Math.toRadians(minuteRotation.toDouble() - 90)).toFloat(),
                    center.y + 60.dp.toPx() * kotlin.math.sin(Math.toRadians(minuteRotation.toDouble() - 90)).toFloat()
                ),
                strokeWidth = 4f // Thinner minute hand
            )

            // Hour Hand (adjusted to half of minute hand)
            drawLine(
                color = Color(0xFFFF9C9C), // Light red color
                start = Offset(
                    center.x - centerAdjustment * kotlin.math.cos(Math.toRadians(hourRotation.toDouble() - 90)).toFloat(),
                    center.y - centerAdjustment * kotlin.math.sin(Math.toRadians(hourRotation.toDouble() - 90)).toFloat()
                ),
                end = Offset(
                    center.x + 30.dp.toPx() * kotlin.math.cos(Math.toRadians(hourRotation.toDouble() - 90)).toFloat(),
                    center.y + 30.dp.toPx() * kotlin.math.sin(Math.toRadians(hourRotation.toDouble() - 90)).toFloat()
                ),
                strokeWidth = 4f // Thinner hour hand
            )
        }
    }
}

@Composable
fun Text(text: String, color: Color, fontSize: TextUnit, fontWeight: FontWeight) {
    androidx.compose.material3.Text(
        text = text,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight
    )
}
