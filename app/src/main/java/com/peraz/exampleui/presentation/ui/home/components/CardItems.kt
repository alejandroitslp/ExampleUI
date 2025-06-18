package com.peraz.exampleui.presentation.ui.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.peraz.exampleui.R
import com.peraz.exampleui.presentation.ui.theme.dark_blue

@Composable
fun CardItems(
    desc: String? = null,
    image: String?= null,
    modifier: Modifier
) {
    Card(border = BorderStroke(width = .1.dp, color = Color.Black),
        modifier = modifier.width(170.dp).height(280.dp).padding(10.dp)) {
        Column(modifier=Modifier.weight(.8f)) {
            Image(painter = rememberAsyncImagePainter(image), contentDescription = null, modifier = Modifier.fillMaxWidth(), contentScale = ContentScale.Crop)
        }
        Column(modifier=Modifier.weight(.3f).background(color = Color.White)) {
            Text(text = desc.toString(),
                textAlign = TextAlign.Center, color = dark_blue, modifier= Modifier.padding(start = 5.dp, end = 5.dp, top = 5.dp).verticalScroll(state = rememberScrollState(),enabled = true).fillMaxWidth())
        }
    }
}

@Preview
@Composable
fun CardItemsPreview(){
}