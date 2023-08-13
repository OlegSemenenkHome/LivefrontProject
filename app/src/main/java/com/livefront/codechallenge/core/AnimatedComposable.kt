package com.livefront.codechallenge.core

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/*
 * Some slide in animations. Placed here for reusability, but could also be placed in
 * navGraph since we only have one animation
 */
internal fun NavGraphBuilder.animatedSlideComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit,
) = composable(
    route = route,
    arguments = arguments,
    enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
    exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
    popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right) },
    popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right) },
    content = content,
)
