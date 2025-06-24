package com.peraz.exampleui.presentation.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.peraz.exampleui.R
import com.peraz.exampleui.presentation.ui.theme.dark_blue

@Composable
fun DetailsScreen(
)
{
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier=Modifier.fillMaxSize())
    {
        Column(modifier=Modifier.fillMaxWidth().background(color = dark_blue).paint(painter = painterResource(R.drawable.exclusecollection),contentScale= ContentScale.Crop) .drawBehind{
            val width =size.width
            val height =size.height*.16f

            drawArc(
                color = Color.White,
                startAngle = 180f,    // Start from the right (3 o'clock)
                sweepAngle = 180f,  // Sweep 180 degrees clockwise to the left (9 o'clock)
                useCenter = false,  // Don't connect to the center for a clean arc
                topLeft = Offset(0f,height*5.75f), // Position the top of the bounding box
//                topLeft = Offset(0f,height*5.3f), // Position the top of the bounding box
                // so the top half is visible
                size = Size(width, height) // Full width, twice the height
            )
        }.weight(.4f)) {

        }
        Column(modifier = Modifier.weight(.6f).background(color = Color.White).fillMaxSize()) {
            Text("Texto")
        }
    }
}