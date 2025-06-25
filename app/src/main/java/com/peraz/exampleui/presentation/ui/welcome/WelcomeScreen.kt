package com.peraz.exampleui.presentation.ui.welcome

import android.graphics.Paint.Align
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peraz.exampleui.R
import com.peraz.exampleui.Routes
import com.peraz.exampleui.presentation.ui.home.HomeScreen
import com.peraz.exampleui.presentation.ui.theme.dark_blue

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onNavigatetoHome: () -> Unit,
){
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier=Modifier.fillMaxSize()) {
        Column(modifier=Modifier.fillMaxWidth().background(color = dark_blue).paint(painter = painterResource(R.drawable.abrazaditos),contentScale= ContentScale.Crop) .drawBehind{
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
        }.weight(.4f),

            ) {

        }
        Column(modifier=Modifier.background(color = Color.White).weight(.6f).fillMaxWidth().padding(top = 10.dp),
            verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(color = Color.Black, text="Bienvenido a nuestra tienda online!", fontWeight = FontWeight.Bold, fontSize = 30.sp, fontFamily = FontFamily.Serif, textAlign = TextAlign.Center)
            Column(modifier=Modifier.weight(1f).width(300.dp),
                verticalArrangement = Arrangement.Center) {
                Text(color = Color.Black ,fontSize = 17.sp ,text = "Experimente la mas extraordinaria y visionaria experiencia de un CRM completamente desarrollado para sus necesidades. Donde la imaginacion da lugar a cosas increibles.",)
            }
            Column(modifier=Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = {
                    onNavigatetoHome()
                }, colors = ButtonDefaults.buttonColors(containerColor = dark_blue), shape = RectangleShape, modifier=Modifier.clip(RoundedCornerShape(15.dp))) {
                    Text(text = "Empezar a explorar", color = Color.White)
                }
            }
        }

    }


}

@Preview
@Composable
fun HomeScreenPreview(){
}
