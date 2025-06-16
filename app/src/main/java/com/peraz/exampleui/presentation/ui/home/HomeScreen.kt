package com.peraz.exampleui.presentation.ui.home

import android.util.Log
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.peraz.exampleui.R
import com.peraz.exampleui.presentation.ui.theme.dark_blue
import com.peraz.exampleui.presentation.ui.home.components.BottomBar
import com.peraz.exampleui.presentation.ui.home.components.CardItems
import com.peraz.exampleui.presentation.ui.home.components.CircleProfilePicture
import com.peraz.exampleui.presentation.ui.home.components.CollectionItems
import com.peraz.exampleui.presentation.ui.theme.light_blue
import com.peraz.exampleui.presentation.ui.theme.pink_light
import com.peraz.exampleui.presentation.ui.theme.searchbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {

    var itemFromCard = remember { 0 }
    val collections = remember { viewModel.collections }
    val randomCol = remember { viewModel.products }
    var isLoading = remember { viewModel.isLoading }
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    var isRefreshing by remember { mutableStateOf(false) }
    var estadoColor by remember { mutableStateOf(false) }
    var backgroundcolor = remember { Animatable(light_blue) }
    val mostrarImagen =remember {mutableStateOf(false)}

    LaunchedEffect(estadoColor) {
        backgroundcolor.animateTo(
            if (estadoColor) {
                pink_light
            } else {
                dark_blue
            }, animationSpec = tween(6000)
        )
    }


    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        BottomBar()
    }) { padding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            state = PullToRefreshState(),
            onRefresh = {
                scope.launch {
                    isRefreshing = true
                    delay(2000)
                    isRefreshing = false
                }

                viewModel.refreshCollectionsDao()

                var randomizer = (1..collections.size).random()
                viewModel.refreshProductsDao(randomizer)


            }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .border(width = 5.dp, color = Color.Black)
            ) {
                if (mostrarImagen.value){
                    AlertDialog(
                        containerColor = Color.Black,
                        modifier = Modifier.fillMaxWidth().height(500.dp),
                        onDismissRequest = {},
                        title = {
                            Text(text = randomCol[itemFromCard].name)
                                },
                        text = {
                            Column {
                                Text(text = "Precio: $${randomCol[itemFromCard].price} MxN")
                                Text(text = "Stock: ${randomCol[itemFromCard].stock}")
                                Spacer(modifier = Modifier.height(5.dp))
                                LazyRow(modifier = Modifier.fillMaxSize()) {
                                    items(randomCol[itemFromCard].localimagepath.size) {
                                        index->
                                        AsyncImage(modifier= Modifier.fillParentMaxWidth(), model = randomCol[itemFromCard].localimagepath[index], contentDescription = null)
                                    }
                                }
                            }
                               },
                        confirmButton = {},
                        dismissButton = {
                            Button(onClick = {
                                mostrarImagen.value=false
                            }) {
                                Text(text = "Cerrar")
                            }
                            }
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .drawBehind {
                            val width = size.width
                            val height = size.height * .16f
                            val rectheight = size.height
                            drawRect(
                                color = backgroundcolor.value,
                                topLeft = Offset(0f, 0f),
                                size = Size(width, rectheight / 1.26f)
                            )
                            drawArc(
                                color = backgroundcolor.value,
                                startAngle = 0f,    // Start from the right (3 o'clock)
                                sweepAngle = 180f,  // Sweep 180 degrees clockwise to the left (9 o'clock)
                                useCenter = false,  // Don't connect to the center for a clean arc
//                            topLeft = Offset(0f, 800f), // Position the top of the bounding box
                                topLeft = Offset(
                                    0f,
                                    rectheight / 1.37f
                                ), // Position the top of the bounding box
                                // so the top half is visible
                                size = Size(width, height / 1.4f) // Full width, twice the height
                            )
                        }
                        .weight(.5f)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(top = 40.dp)
                            .weight(.7f)
                    )
                    {
                        CircleProfilePicture(name = "Alejandro", navigateMenu = {

                        }, onClick = { isAnimatable ->
                            estadoColor = isAnimatable
                        })
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(.8f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Encuentra tu coleccion favorita aqui!",
                            color = Color.White,
                            fontSize = 23.sp,
                            modifier = Modifier.width(350.dp)
                        )
                        SearchBar(
                            inputField = {
                            var oltxtfield by remember { mutableStateOf("") }
                            OutlinedTextField(
                                value = oltxtfield,
                                onValueChange = { oltxtfield = it },
                                placeholder = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(R.drawable.zoomfilled),
                                            contentDescription = null
                                        )
                                        Text(
                                            text = "Busca tu coleccion!",
                                            fontSize = 20.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.Light
                                        )
                                    }
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedBorderColor = Color.Transparent
                                )
                            )
                        },
                            expanded = false,
                            onExpandedChange = {},
                            colors = SearchBarDefaults.colors(containerColor = searchbar),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 3.dp)
                                .height(55.dp)
                        ) { }

                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 20.dp)
                            .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LazyRow(horizontalArrangement = Arrangement.Center) {
                            if (isLoading.value == false) {
                                items(1) { index ->
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .size(90.dp)
                                            .padding(16.dp),
                                        color = light_blue,
                                        strokeWidth = 8.dp,
                                        trackColor = Color.LightGray,
                                        strokeCap = StrokeCap.Round
                                    )
                                }
                            } else {
                                items(collections.size) { index ->

                                    CollectionItems(
                                        collectioName = collections[index].nombre,
                                        image = "${collections[index].localImagePath}",
                                        onClick = { fromId ->
                                            viewModel.refreshProductsDao(fromId)
                                        },
                                        scope = scope,
                                        context = context,
                                        id = collections[index].id
                                    )
                                }
                            }

                        }
                    }


                }
                Column(
                    modifier = Modifier
                        .background(color = Color.White)
                        .weight(.5f)
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                                .weight(1f)
                                .padding(end = 18.dp)
                        ) {
                            var textoColeccion = ""
                            Log.d("HomeScreenTitle", "${randomCol.size}")
                            if (randomCol.isNotEmpty()) {
                                textoColeccion = randomCol[0].nameCollection.toString()
                            } else {
                                textoColeccion = "Sin Coleccion"
                            }
                            Text(
                                text = textoColeccion,
                                color = dark_blue,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.End, modifier = Modifier
                                .weight(1f)
                                .padding(end = 18.dp)
                                .clickable {

                                }) {
                            Text(text = "Ver detalles", color = dark_blue)
                        }
                    }
                    LazyRow(modifier = Modifier.padding(top = 15.dp)) {
                        items(randomCol.size) { item ->
                            if (randomCol[item].localimagepath != null) {
                                if (randomCol[item].desc == null) {
                                    CardItems(
                                        image = "${randomCol[item].localimagepath[0]}",
                                        desc = randomCol[item].name,
                                        modifier = Modifier.clickable{
                                            itemFromCard=item
//
                                            mostrarImagen.value=true
                                        }
                                    )

                                } else {
                                    CardItems(
                                        image = randomCol[item].localimagepath[0],
                                        desc = randomCol[item].desc.toString(),
                                        modifier = Modifier.clickable{
                                            itemFromCard=item

                                            mostrarImagen.value=true

                                            Log.d("BoleanoHomeScreen","${mostrarImagen.value}")
                                        }
                                    )
                                }
                            } else {
                                CardItems(
                                    image = "R.drawable.abrazaditos",
                                    desc = randomCol[item].desc.toString(),
                                    modifier = Modifier.clickable{
                                        itemFromCard=item
                                        mostrarImagen.value=true
                                    }
                                )
                            }
                        }
                    }
                }

            }
        }

    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
