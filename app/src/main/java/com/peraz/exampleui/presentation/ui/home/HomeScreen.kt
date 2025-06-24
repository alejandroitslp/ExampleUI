package com.peraz.exampleui.presentation.ui.home

import android.util.Log
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.peraz.exampleui.Routes
import com.peraz.exampleui.presentation.ui.theme.dark_blue
import com.peraz.exampleui.presentation.ui.home.components.BottomBar
import com.peraz.exampleui.presentation.ui.home.components.CardItems
import com.peraz.exampleui.presentation.ui.home.components.CircleProfilePicture
import com.peraz.exampleui.presentation.ui.home.components.CollectionItems
import com.peraz.exampleui.presentation.ui.home.components.SearchBarCustom
import com.peraz.exampleui.presentation.ui.theme.light_blue
import com.peraz.exampleui.presentation.ui.theme.pink_light
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    navigateDetails: (String) -> Unit,
) {
    var idFromCard = remember { 0 }
    val collections = remember { viewModel.collections }
    val randomCol = remember { viewModel.products }
    var isLoading = remember { viewModel.isLoading }
    val context = LocalContext.current
    val progressBar= viewModel.progresBar
    var errorConexion= viewModel.error
    var productbyid=viewModel.productById

    val scope = rememberCoroutineScope()


    var isRefreshing by remember { mutableStateOf(false) }
    var estadoColor by remember { mutableStateOf(false) }
    var backgroundcolor = remember { Animatable(light_blue) }
    val mostrarImagen =remember {mutableStateOf(false)}
    val snackbarHostState=remember{ SnackbarHostState() }

    LaunchedEffect(estadoColor) {
        backgroundcolor.animateTo(
            if (estadoColor) {
                pink_light
            } else {
                dark_blue
            }, animationSpec = tween(6000)
        )

    }
    LaunchedEffect(key1 = true){

        errorConexion.collect {
            message->
            snackbarHostState.showSnackbar(message)
        }

    }


    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        BottomBar(sinchronize = {
            viewModel.getProductsFromRetrofit()
        })
    }, snackbarHost= {SnackbarHost(hostState = snackbarHostState)}) { padding ->
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
            ) {
                if (mostrarImagen.value){
                    AlertDialog(
                        containerColor = Color.Black,
                        modifier = Modifier.fillMaxWidth().height(500.dp),
                        onDismissRequest = {},
                        title = {
                            Text(text = productbyid.first().name)
                                },
                        text = {
                            Column {
                                Text(text = "Precio: $${productbyid.first().price} MxN")
                                Text(text = "Stock: ${productbyid.first().stock}")
                                Spacer(modifier = Modifier.height(5.dp))
                                LazyRow(modifier = Modifier.fillMaxSize()) {
                                    items(productbyid.first().localimagepath.size) {
                                        index->
                                        val zoomState = rememberZoomState(initialScale = 1f)
                                        AsyncImage(modifier= Modifier.fillParentMaxWidth().zoomable(
                                            zoomState = zoomState
                                        ), model = productbyid.first().localimagepath[index], contentDescription = null)
                                    }
                                }
                            }
                               },
                        confirmButton = {
                        },
                        dismissButton = {
                            Button(onClick = {
                                mostrarImagen.value=false
                            }, colors = ButtonDefaults.buttonColors(containerColor = pink_light)) {
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

                        CircleProfilePicture(name = "Alejandro", navigateMenu = {

                        }, onClick = { isAnimatable ->
                            estadoColor = isAnimatable
                        }, modifier=Modifier.padding(top=35.dp))


                        Text(
                            text = "Encuentra tu coleccion favorita aqui!",
                            color = Color.White,
                            fontSize = 23.sp,
                            modifier = Modifier.width(350.dp).padding(15.dp),
                            textAlign = TextAlign.Center
                        )

                        SearchBarCustom(modifier= Modifier, products=randomCol, onClick = {
                            idFromCard=it
                            viewModel.getProductsById(it)
                            mostrarImagen.value=true
                        })

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
                                        color = dark_blue,
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
                        .fillMaxWidth(),
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
                            textoColeccion = if (randomCol.isNotEmpty()) {
                                randomCol[0].nameCollection.toString()
                            } else {
                                "Sin Coleccion"
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
                                    navigateDetails(Routes.Details)
                                }) {
                            Text(text = "Ver detalles", color = dark_blue)
                        }
                    }
                    if (randomCol.isEmpty())
                    {
                        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(20.dp)) {
                            val animatedProgress = animateFloatAsState(
                                targetValue = progressBar.floatValue,
                                animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
                            ).value

                            LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier.fillMaxWidth(),
                            color = dark_blue,
                            trackColor = light_blue,
                            )
                        }
                    }
                    else {
                        LazyRow(modifier = Modifier.padding(top = 15.dp)) {
                            items(randomCol.size) { item ->

                                if (randomCol[item].desc == null) {
                                    CardItems(
                                        image = "${randomCol[item].localimagepath[0]}",
                                        desc = randomCol[item].name,
                                        modifier = Modifier.clickable {
                                            viewModel.getProductsById(randomCol[item].id)
                                            mostrarImagen.value = true
                                        }
                                    )

                                } else {
                                    CardItems(
                                        image = randomCol[item].localimagepath[0],
                                        desc = randomCol[item].desc.toString(),
                                        modifier = Modifier.clickable {
                                            viewModel.getProductsById(randomCol[item].id)
                                            mostrarImagen.value = true

                                            Log.d("BoleanoHomeScreen", "${mostrarImagen.value}")
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
}


@Preview
@Composable
fun HomeScreenPreview() {
}
