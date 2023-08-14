package com.livefront.codechallenge.presentation.composables

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
    exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left) },

    content = content,
)
