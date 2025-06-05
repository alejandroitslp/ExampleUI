package com.peraz.exampleui.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.peraz.exampleui.Routes
import com.peraz.exampleui.presentation.ui.home.HomeScreen
import com.peraz.exampleui.presentation.ui.theme.ExampleUITheme
import com.peraz.exampleui.presentation.ui.welcome.WelcomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExampleUITheme {
                Surface {
                    val navController = rememberNavController()

                    NavHost(
                        startDestination = Routes.Welcome,
                        navController=navController
                    ){
                        composable(Routes.Welcome) {
                            WelcomeScreen(navigateNext={
                                route->
                                navController.navigate(route)
                            })
                        }
                        composable(Routes.Home){
                            HomeScreen()
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
    HomeScreen(modifier = Modifier)
}
