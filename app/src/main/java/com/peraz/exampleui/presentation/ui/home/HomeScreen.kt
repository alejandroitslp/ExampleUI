package com.peraz.exampleui.presentation.ui.home

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHost
import coil.compose.AsyncImage
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
import java.text.DecimalFormat
import kotlin.math.pow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    navigateDetails: (String, String?, Boolean) -> Unit,
) {
    val collections = remember { viewModel.collections }
    val randomCol = remember { viewModel.products }
    val isLoading = remember { viewModel.isLoading }
    val context = LocalContext.current
    val progressBar= viewModel.progresBar
    val errorConexion= viewModel.error
    val productbyid=viewModel.productById
    val name = viewModel.name.value
    val role = viewModel.role.value
    val scope = rememberCoroutineScope()


    var isRefreshing by remember { mutableStateOf(false) }
    var estadoColor by remember { mutableStateOf(false) }
    val backgroundcolor = remember { Animatable(light_blue) }
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

    BackHandler(enabled = true) {

        Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
        viewModel.resetUser()
        navigateDetails("","",false)
    }

    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        BottomBar(sinchronize = {
            viewModel.getProductsFromRetrofit()
//            viewModel.getProductsFromRetrofit()
        },
            logout = {
                viewModel.resetUser()
                navigateDetails("","",false)
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

                val randomizer = (1..collections.size).random()
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
                        modifier = Modifier.fillMaxWidth().height(650.dp),
                        onDismissRequest = {},
                        title = {
                            Text(text = productbyid.first().name, color = Color.White)
                                },
                        text = {
                            Column {
                                var quantity = remember {mutableStateOf("1")}
                                var prodprice=productbyid.first().price.replace(",","")
                                Text(text = "Precio: $${productbyid.first().price} MxN", color = Color.White)
                                if (role!=null && role.equals(1)){
                                    Text(text = "Stock: ${productbyid.first().stock}", color = Color.White)
                                }

                                OutlinedTextField(onValueChange = {quantity.value= it}, value = quantity.value, placeholder = {
                                    Text(text = "Inserte numero de semanas.", color = Color.White)
                                }, colors = TextFieldColors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.Yellow,
                                    focusedContainerColor = Color.Black,
                                    unfocusedContainerColor = Color.DarkGray,
                                    disabledTextColor = Color.White,
                                    errorTextColor = Color.White,
                                    disabledContainerColor = Color.White,
                                    errorContainerColor = Color.White,
                                    cursorColor = Color.White,
                                    errorCursorColor = Color.White,
                                    textSelectionColors = TextSelectionColors(backgroundColor = Color.LightGray, handleColor = Color.Red),
                                    focusedIndicatorColor = Color.White,
                                    unfocusedIndicatorColor = Color.White,
                                    disabledIndicatorColor = Color.White,
                                    errorIndicatorColor = Color.White,
                                    focusedLeadingIconColor = Color.White,
                                    unfocusedLeadingIconColor = Color.White,
                                    disabledLeadingIconColor = Color.White,
                                    errorLeadingIconColor = Color.White,
                                    focusedTrailingIconColor = Color.White,
                                    unfocusedTrailingIconColor = Color.White,
                                    disabledTrailingIconColor = Color.White,
                                    errorTrailingIconColor = Color.White,
                                    focusedLabelColor = Color.White,
                                    unfocusedLabelColor = Color.White,
                                    disabledLabelColor = Color.White,
                                    errorLabelColor = Color.White,
                                    focusedPlaceholderColor = Color.White,
                                    unfocusedPlaceholderColor = Color.White,
                                    disabledPlaceholderColor = Color.White,
                                    errorPlaceholderColor = Color.White,
                                    focusedSupportingTextColor = Color.White,
                                    unfocusedSupportingTextColor = Color.White,
                                    disabledSupportingTextColor = Color.White,
                                    errorSupportingTextColor = Color.White,
                                    focusedPrefixColor = Color.White,
                                    unfocusedPrefixColor = Color.White,
                                    disabledPrefixColor = Color.White,
                                    errorPrefixColor = Color.White,
                                    focusedSuffixColor = Color.White,
                                    unfocusedSuffixColor = Color.White,
                                    disabledSuffixColor = Color.White,
                                    errorSuffixColor = Color.White,
                                ), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) )
                                var pagos=0.00
                                if (quantity.value!="1"&& quantity.value!=""){
                                    if (prodprice.toDouble()<1000)
                                        pagos= prodprice.toDouble()*((1+.07).pow(quantity.value.toDouble()))
                                    else
                                        pagos= prodprice.toDouble()*((1+.05).pow(quantity.value.toDouble()))

                                }else{
                                    pagos= prodprice.toDouble()
                                }
                                val dec=DecimalFormat("#,###.00")

                                Text(text = "El valor en precio a ${quantity.value} semanas, es: ${dec.format(pagos)}", color = Color.White)
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
                        CircleProfilePicture(name = name, navigateMenu = {

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
                            if (!isLoading.value) {
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
                            Log.d("HomeScreenTitle", "${randomCol.size}")
                            val textoColeccion: String = if (randomCol.isNotEmpty()) {
                                randomCol[0].nameCollection
                            } else {
                                "Sin Coleccion"
                            }
                            Text(
                                text = textoColeccion,
                                color = dark_blue,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        if (!randomCol.isEmpty()){
                            Column(
                                horizontalAlignment = Alignment.End, modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 18.dp)
                                    .clickable {
                                        //Aqui se envia el color.
                                        navigateDetails("#${backgroundcolor.value.toArgb().toUInt().toString(16).padStart(8,'0')}",
                                            randomCol[0].idCollection.toString(), true
                                        )
                                    }) {
                                Text(text = "Ver detalles", color = dark_blue)
                            }
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
