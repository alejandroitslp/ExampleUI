package com.peraz.exampleui.presentation.ui.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peraz.exampleui.R
import com.peraz.exampleui.presentation.ui.theme.dark_blue

@Composable
fun BottomBar(){
    Row(modifier=Modifier.fillMaxWidth().background(color = Color.White).border(width = 0.3.dp, color=Color.LightGray, shape= RectangleShape), horizontalArrangement = Arrangement.Center) {
        Column(modifier = Modifier.weight(.15f).padding(15.dp).clickable{}) {
            Image(painter = painterResource(R.drawable.home), contentDescription = null, modifier=Modifier.size(30.dp))
            Text(text = "Home", fontSize = 13.sp, color = dark_blue)
        }
        Column(modifier = Modifier.weight(.15f).padding(15.dp).clickable{}) {
            Image(painter = painterResource(R.drawable.wallet), contentDescription = null, modifier=Modifier.size(30.dp))
            Text(text = "Wallet", fontSize = 13.sp, color = dark_blue)
        }
        Column(modifier = Modifier.weight(.2f).padding(start=20.dp, bottom = 40.dp).clickable{}) {
            Image(painter = painterResource(R.drawable.arrowsexchangewhite), contentDescription = null, modifier=Modifier.size(50.dp).clip(shape = CircleShape).background(color= dark_blue))
        }
        Column(modifier = Modifier.weight(.15f).padding(15.dp).clickable{}) {
            Image(painter = painterResource(R.drawable.user), contentDescription = null, modifier=Modifier.size(30.dp))
            Text(text = "User", fontSize = 13.sp, color = dark_blue)
        }
        Column(modifier = Modifier.weight(.15f).padding(15.dp).clickable{}) {
            Image(painter = painterResource(R.drawable.settings), contentDescription = null, modifier=Modifier.size(30.dp))
            Text(text = "Config", fontSize = 13.sp, color = dark_blue)
        }

    }
}

@Preview
@Composable
fun BottomBarPreview(){
    BottomBar()
}