package com.peraz.exampleui.presentation.ui.home.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peraz.exampleui.R
import com.peraz.exampleui.presentation.ui.theme.dark_blue
import jakarta.inject.Inject
import kotlin.io.path.moveTo
import kotlin.math.sin

@Composable
fun BottomBar(
    sinchronize: ()->Unit
){
    Row(modifier=Modifier.fillMaxWidth().background(color = Color.White).border(width = 0.3.dp, color=Color.LightGray, shape= RectangleShape), horizontalArrangement = Arrangement.Center) {
        Column(modifier = Modifier.weight(.33f).padding(all = 20.dp).clickable{}) {
            Image(painter = painterResource(R.drawable.home), contentDescription = null, modifier=Modifier.size(30.dp))
            Text(text = "Home", fontSize = 13.sp, color = dark_blue)
        }
        Column(modifier = Modifier.weight(.33f).padding(15.dp).clickable{
            sinchronize()
        }, horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(R.drawable.arrowsexchangewhite), contentDescription = null, modifier=Modifier.size(50.dp).clip(shape = CircleShape).background(color= dark_blue))
        }
        Column(modifier = Modifier.weight(.33f).padding(all = 20.dp).clickable{}, horizontalAlignment = Alignment.End) {
            Image(painter = painterResource(R.drawable.settings), contentDescription = null, modifier=Modifier.size(30.dp))
            Text(text = "Config", fontSize = 13.sp, color = dark_blue)
        }

    }
}




@Preview
@Composable
fun BottomBarPreview(){
}