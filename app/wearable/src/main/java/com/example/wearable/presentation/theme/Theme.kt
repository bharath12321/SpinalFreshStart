package com.example.wearable.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Shapes
import androidx.wear.compose.material.Typography

@Composable
fun SpinalFreshStartWearTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = MyColors,
        typography = MyTypography,
        shapes = MyShapes,
        content = content
    )
}

internal val MyColors: Colors = Colors(
    primary = Color(220/255f,236/255f,240/255f,1.0f),
    secondary = Color(0xFFE6F4F2),
    background = Color.White,
    surface = Color.White,
    onPrimary = Color(12,173,181,255),
    onSecondary = Color.Gray,
    onBackground = Color.Black,
    onSurface = Color(0xFF008B8B)
)

private val MyTypography = Typography(
    title1 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        letterSpacing = 1.0.sp
    ),
    title2 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        letterSpacing = 1.15.sp
    ),
    title3 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 10.sp,
        letterSpacing = 1.15.sp
    ),
    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.5.sp
    ),
    body2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 8.sp,
        letterSpacing = 0.25.sp
    )
)

private val MyShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(12.dp)
)