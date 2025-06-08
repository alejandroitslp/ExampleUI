package com.peraz.exampleui.presentation.ui.home.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peraz.exampleui.R
import com.peraz.exampleui.presentation.ui.theme.dark_blue

@Composable
fun CircleProfilePicture(
    navigateMenu: () -> Unit,
    name: String,
    modifier: Modifier=Modifier,
    onClick: (Boolean)-> Unit,
){
    var boleano=false
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Image(painter = painterResource(R.drawable.profilepic), contentDescription = null, modifier=Modifier.size(50.dp).weight(.5f).padding(start = 20.dp)
            .clickable{

            })
        Text(text = "Hi $name", modifier=Modifier.size(50.dp).weight(.6f).padding(0.dp,15.dp), color = Color.White)
        Image(painter = painterResource(R.drawable.bellcircle), contentDescription = null, modifier=Modifier.size(35.dp).weight(1f).padding(start = 100.dp)
            .clickable{
                if (boleano){
                    Log.d("Pendejo","$boleano")
                    boleano=!boleano
                }else{
                    Log.d("Pendejo","$boleano")
                    boleano=!boleano
                }
                onClick(boleano)
            })
    }
}

@Preview(showBackground = true, backgroundColor = 0)
@Composable
fun CircleProfilePicturePreview(){

}