package com.example.gweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gweather.ui.homepage.HomeScreen
import com.example.gweather.ui.auth.AuthScreen
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding(),
                color = MaterialTheme.colorScheme.background
            ) {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "auth") {
                    composable("auth") {
                        AuthScreen(
                            windowSizeClass = windowSizeClass,
                            onAuthenticated = { uid ->
                                navController.navigate("home/$uid")
                            }
                        )
                    }

                    composable("home/{uid}") { backStackEntry ->
                        val uid = backStackEntry.arguments?.getString("uid") ?: return@composable
                        HomeScreen(
                            uid = uid,
                            windowSizeClass = windowSizeClass
                        )
                    }
                }
            }
        }
    }
}