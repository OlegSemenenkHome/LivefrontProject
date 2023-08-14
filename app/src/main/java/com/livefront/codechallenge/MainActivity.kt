package com.livefront.codechallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
                NavHost(
                    enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
                    exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
                    popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right) },
                    popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right) },
                    navController = navController,
                    startDestination = "homeScreen"
                ) {
                    composable("homeScreen") { HomeScreen(navController) }
                    composable(
                        "detailView/{detailKey}",
                        arguments = listOf(navArgument("detailKey") { type = NavType.StringType })
                    ) { CharacterDetailScreen(navController = navController) }
                }
            }
        }
    }
}
