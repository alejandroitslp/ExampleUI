package com.peraz.exampleui.presentation.ui.details

import android.graphics.Color.parseColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.peraz.exampleui.R
import com.peraz.exampleui.presentation.MainActivity.Details
import com.peraz.exampleui.presentation.ui.theme.dark_blue

@Composable
fun DetailsScreen(
    details: Details,
    viewModel: DetailsScreenViewModel= hiltViewModel()
)
{


    val products= remember{viewModel.products}

    val backgroundColor=Color(parseColor(details.colorBackground))

    LaunchedEffect(key1=true) {
        if (details.id!=null){
            viewModel.refreshProductsDao(details.id.toInt())
        }
        else{
            viewModel.refreshProductsDao()
        }
    }


    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier=Modifier.fillMaxSize())
    {
        Column(modifier=Modifier.fillMaxWidth().background(color = backgroundColor).paint(painter = painterResource(R.drawable.exclusecollection),contentScale= ContentScale.Crop) .drawBehind{
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

                if (!products.isEmpty()){
                    LazyColumn{
                        items(products.size){
                            item->
                            Column(modifier = Modifier.padding(30.dp)) {
                                Text(text = "Id: ${products[item].id}", color = dark_blue)
                                Text(text = "Descripcion: ${products[item].desc ?: "Sin descripcion"}", color = dark_blue)
                                Text(text = "Nombre: ${products[item].name}", color = dark_blue)
                                Text(text = "Precio: ${products[item].price}", color = dark_blue)
                                Text(text = "Stock: ${products[item].stock}", color = dark_blue)
                            }
                        }
                    }
                }else{
                    Text(text = "Sin items")
                }
            }
        }
    }
