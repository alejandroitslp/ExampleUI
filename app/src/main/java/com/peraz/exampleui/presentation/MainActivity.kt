package com.peraz.exampleui.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.peraz.exampleui.presentation.ui.details.DetailsScreen
import com.peraz.exampleui.presentation.ui.home.HomeScreen
import com.peraz.exampleui.presentation.ui.theme.ExampleUITheme
import com.peraz.exampleui.presentation.ui.theme.light_blue
import com.peraz.exampleui.presentation.ui.theme.pink_light
import com.peraz.exampleui.presentation.ui.welcome.WelcomeScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Serializable
    class Welcome
    @Serializable
    class Home
    @Serializable
    data class Details(val colorBackground: String, val id: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            ExampleUITheme {
                Surface {

                    val navController = rememberNavController()

                    NavHost(
                        navController=navController,
                        startDestination = Welcome()
                    ){
                        composable<Welcome>{
                            backStackEntry ->
                            WelcomeScreen(onNavigatetoHome = {
                                navController.navigate(route = Home())
                            })
                        }
                        composable<Home>{
                            backStackEntry ->
                            HomeScreen(
                                navigateDetails = {
                                    colorBG, collectionId->
                                navController.navigate(route = Details(colorBackground = colorBG, id=collectionId))
                                    //Desde HomeScreen pasa el color adquirido. y el ID de coleccion
                            })
                        }
                        composable<Details>{
                            backStackEntry->
                            val detailsArgs: Details = backStackEntry.toRoute()
                            DetailsScreen(details = detailsArgs)//Recibe el color desde homescreen
                        }
                    }
                }
            }
        }
    }

}

@Preview
@Composable
fun AppPreview(){
}
