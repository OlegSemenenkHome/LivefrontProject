package com.livefront.codechallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.livefront.codechallenge.core.animatedSlideComposable
import com.livefront.codechallenge.presentation.detailscreen.CharacterDetailScreen
import com.livefront.codechallenge.presentation.homescreen.HomeScreen
import com.livefront.codechallenge.ui.theme.LivefrontCodeChallengeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LivefrontCodeChallengeTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "homeScreen") {
                    animatedSlideComposable("homeScreen") { HomeScreen(navController) }
                    animatedSlideComposable(
                        "detailView/{detailKey}",
                        arguments = listOf(navArgument("detailKey") { type = NavType.StringType })
                    ) { CharacterDetailScreen(navController = navController) }
                }
            }
        }
    }
}
