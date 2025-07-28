package com.peraz.exampleui.presentation.ui.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.peraz.exampleui.R
import com.peraz.exampleui.presentation.ui.theme.dark_blue

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    welcomeScreenViewModel: WelcomeScreenViewModel= hiltViewModel(),
    onNavigatetoHome: () -> Unit,
){
    val isReady=remember {welcomeScreenViewModel.isReady}
    val isError=remember{welcomeScreenViewModel.isError}
    val passwordVisible=remember{mutableStateOf(true)}

    LaunchedEffect(isReady.value) {
        if (isReady.value){
            onNavigatetoHome()
        }
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier=Modifier.fillMaxSize()) {
        Column(modifier=Modifier.fillMaxWidth().background(color = dark_blue).paint(painter = painterResource(R.drawable.twoexecutives),contentScale= ContentScale.Crop) .drawBehind{
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
            Text(color = Color.Black, text="Bienvenido a nuestro catalogo online!", fontWeight = FontWeight.Bold, fontSize = 30.sp, fontFamily = FontFamily.Serif, textAlign = TextAlign.Center)
            Column(modifier=Modifier.weight(.3f).width(300.dp),
                verticalArrangement = Arrangement.Center) {
                Text(color = Color.Black ,fontSize = 17.sp ,text = "Aqui podrás encontrar una gran colección de joyeria en plata para revisar inventario, precios y detalles de tus items favoritos..",)
            }
            Column(modifier=Modifier.weight(.8f).width(280.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                val nametxt=remember{ mutableStateOf("") }
                val passwordtxt = remember {mutableStateOf("")}
                OutlinedTextField(
                    textStyle = TextStyle(color = dark_blue),
                    value=nametxt.value,
                    onValueChange = {
                        nametxt.value=it},
                    placeholder = {Text(text = "Escriba su usuario: ", color = Color.DarkGray)},
                )
                Spacer(modifier=Modifier.height(10.dp))
                Row{
                    OutlinedTextField(
                        supportingText = {
                            if (isError.value){
                                Text(text = "Revise sus contraseñas", color = Color.Red)
                            }
                        },
                        textStyle = TextStyle(color = dark_blue),
                        value=passwordtxt.value,
                        onValueChange = {
                            passwordtxt.value=it},
                        visualTransformation = if (passwordVisible.value) PasswordVisualTransformation() else VisualTransformation.None ,
                        placeholder = {Text(text = "Escriba su contraseña: " , color = Color.DarkGray)},
                        modifier=Modifier.weight(.9f)
                    )
                    Spacer(modifier=Modifier.width(3.dp))
                    IconButton(onClick = {
                        passwordVisible.value=!passwordVisible.value
                    }) {
                        Icon(modifier=Modifier.height(50.dp).weight(.2f),painter = painterResource(R.drawable.eye), contentDescription = null)
                    }
                }

                Spacer(modifier=Modifier.height(25.dp))
                Row {
                    Button(onClick = {
                        onNavigatetoHome()
                    }, colors = ButtonDefaults.buttonColors(containerColor = dark_blue), shape = RectangleShape, modifier=Modifier.clip(RoundedCornerShape(15.dp))) {
                        Text(text = "Entrar como invitado", color = Color.White)
                    }
                    Spacer(modifier=Modifier.width(3.dp))
                    Button(onClick = {
                        if (!isReady.value){
                            welcomeScreenViewModel.login(nametxt.value, passwordtxt.value)
                        }
                                     }, colors = ButtonDefaults.buttonColors(containerColor = dark_blue), shape = RectangleShape, modifier=Modifier.clip(RoundedCornerShape(15.dp))) {
                        Text(text = "Entrar", color = Color.White)
                    }
                }
            }
        }

    }


}

@Preview
@Composable
fun HomeScreenPreview(){
}
