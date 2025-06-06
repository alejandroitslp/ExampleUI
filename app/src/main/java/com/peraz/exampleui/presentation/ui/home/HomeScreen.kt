package com.peraz.exampleui.presentation.ui.home

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.peraz.exampleui.R
import com.peraz.exampleui.data.CollectionsModel
import com.peraz.exampleui.data.RandomCollModel
import com.peraz.exampleui.data.RetrofitInstance
import com.peraz.exampleui.presentation.ui.theme.dark_blue
import com.peraz.exampleui.presentation.ui.home.components.BottomBar
import com.peraz.exampleui.presentation.ui.home.components.CardItems
import com.peraz.exampleui.presentation.ui.home.components.CircleProfilePicture
import com.peraz.exampleui.presentation.ui.home.components.CollectionItems
import com.peraz.exampleui.presentation.ui.theme.searchbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.collections.get


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
){
    var collections: CollectionsModel? by remember { mutableStateOf(CollectionsModel()) }
    val context= LocalContext.current
    val scope= rememberCoroutineScope()
    var randomCol: RandomCollModel? by remember{ mutableStateOf(RandomCollModel())}
    var isRefreshing by remember {mutableStateOf(false)}

    LaunchedEffect(key1=true) {

        scope.launch(Dispatchers.IO) {
            val response = try{
                RetrofitInstance.api.getCollections()
            }catch(e: IOException){
                Toast.makeText(context, "$e" , Toast.LENGTH_LONG).show()
                return@launch
            }
            withContext(Dispatchers.Main) {
                collections=response.body()
            }
        }

        scope.launch(Dispatchers.IO) {
            val response= try{
                RetrofitInstance.api.getRandomCollections()
            }catch(e: IOException){
                Toast.makeText(context, "$e" , Toast.LENGTH_LONG).show()
                return@launch
            }
            withContext(Dispatchers.Main) {
                randomCol=response.body()
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
         BottomBar()
    }) {
        padding->
        PullToRefreshBox(
            isRefreshing= isRefreshing,
            state = PullToRefreshState(),
            onRefresh = {
                scope.launch {
                    isRefreshing = true
                    delay(2000)
                    isRefreshing = false
                }
                getCollections(scope=scope, context=context) {
                    collectionsfun->
                    collections=collectionsfun
                }

                getRandomCol(scope=scope, context= context){
                    random->
                    randomCol=random
                }
            }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .border(width = 5.dp, color = Color.Black)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .drawBehind {
                            val width = size.width
                            val height = size.height * .16f
                            val rectheight = size.height
                            drawRect(
                                color = dark_blue,
                                topLeft = Offset(0f, 0f),
                                size = Size(width, rectheight / 1.26f)
                            )
                            drawArc(
                                color = dark_blue,
                                startAngle = 0f,    // Start from the right (3 o'clock)
                                sweepAngle = 180f,  // Sweep 180 degrees clockwise to the left (9 o'clock)
                                useCenter = false,  // Don't connect to the center for a clean arc
//                            topLeft = Offset(0f, 800f), // Position the top of the bounding box
                                topLeft = Offset(0f, rectheight/1.37f), // Position the top of the bounding box
                                // so the top half is visible
                                size = Size(width, height/1.4f) // Full width, twice the height
                            )
                        }
                        .weight(.5f)
                ) {
                    Column(modifier = Modifier
                        .padding(top = 40.dp)
                        .weight(.7f)) {
                        CircleProfilePicture(name = "Alejandro", navigateMenu = {})
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
                        SearchBar(inputField = {
                            var oltxtfield by remember { mutableStateOf("") }
                            OutlinedTextField(value = oltxtfield, onValueChange = { oltxtfield=it}, placeholder = {
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Image(painter = painterResource(R.drawable.zoomfilled), contentDescription = null)
                                    Text(text = "Busca tu coleccion!", fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Light)
                                }
                            }, colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.Transparent, focusedBorderColor = Color.Transparent))
                        }, expanded = false, onExpandedChange = {}, colors = SearchBarDefaults.colors(containerColor = searchbar),
                            modifier=Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 3.dp)
                                .height(55.dp)) { }

                    }
                    Column(modifier = Modifier
                        .weight(1f)
                        .padding(top = 20.dp)
                        .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        LazyRow(horizontalArrangement =  Arrangement.Center) {
                            items(collections?.resultado?.size ?: 0) { index ->

                                CollectionItems(collectioName = collections?.resultado?.get(index)?.nombre, image = "${collections?.resultado?.get(index)?.image}")
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
                    Row(modifier=Modifier.fillMaxWidth()){
                        Column(horizontalAlignment = Alignment.Start, modifier=Modifier
                            .weight(1f)
                            .padding(start = 18.dp)
                            .clickable {

                            }){
                            Text(text = "Trend", fontWeight = FontWeight.Bold, color = dark_blue)
                        }
                        Column( horizontalAlignment = Alignment.CenterHorizontally ,modifier= Modifier
                            .weight(1f)
                            .padding(end = 18.dp)){
                            var textoColeccion: String=""
                            Log.d("HomeScreenTitle", "${ randomCol?.randomCol?.size }")
                            if(randomCol?.randomCol?.size!=null && randomCol?.randomCol?.size!=0){
                                textoColeccion=randomCol?.randomCol?.get(0)?.name.toString()
                            }
                            else{
                                textoColeccion="Sin Coleccion"
                            }
                            Text(text = textoColeccion, color= dark_blue, fontWeight = FontWeight.ExtraBold)
                        }
                        Column(horizontalAlignment = Alignment.End, modifier= Modifier
                            .weight(1f)
                            .padding(end = 18.dp)
                            .clickable {

                            }){
                            Text(text = "View details", color= dark_blue)
                        }
                    }
                    LazyRow(modifier=Modifier.padding(top = 15.dp)) {
                        items(randomCol?.randomCol?.size ?: 0){
                                item->
                            var randomizer= (1..3).random()
                            if (randomCol?.randomCol?.get(item)?.image!=null){
                                if (randomCol?.randomCol?.get(item)?.desc==null){
                                    CardItems(image= randomCol?.randomCol?.get(item)?.image, desc= "Aun no existe una descripcion para este producto, porfavor modifique la descripcion en la pagina web")

                                }
                                else{
                                CardItems(image= randomCol?.randomCol?.get(item)?.image, desc= randomCol?.randomCol?.get(item)?.desc.toString())
                                }
                            }
                            else{
                                CardItems(image= "R.drawable.abrazaditos", desc= randomCol?.randomCol?.get(item)?.desc.toString())
                            }
                        }
                    }
                }

            }
        }

    }
}



fun getCollections(
    scope: CoroutineScope,
    context: Context,
    collection: (CollectionsModel?) -> Unit
){
    scope.launch(Dispatchers.IO) {
        val response = try{
            RetrofitInstance.api.getCollections()
        }catch(e: IOException){
            Toast.makeText(context, "$e" , Toast.LENGTH_LONG).show()
            return@launch
        }
        withContext(Dispatchers.Main) {
            collection(response.body())
        }
    }
}



fun getRandomCol(
    scope: CoroutineScope,
    context: Context,
    random: (RandomCollModel?) -> Unit
){
    scope.launch(Dispatchers.IO) {
        val response= try{
            RetrofitInstance.api.getRandomCollections()
        }catch(e: IOException){
            Toast.makeText(context, "$e" , Toast.LENGTH_LONG).show()
            return@launch
        }
        withContext(Dispatchers.Main) {
            random( response.body() )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview(){
    HomeScreen()
}
