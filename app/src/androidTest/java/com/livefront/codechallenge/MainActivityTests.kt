package com.livefront.codechallenge

import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavType
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.test.platform.app.InstrumentationRegistry
import com.livefront.codechallenge.utils.TestTags
import com.livefront.codechallenge.presentation.detailscreen.CharacterDetailScreen
import com.livefront.codechallenge.presentation.homescreen.HomeScreen
import com.livefront.codechallenge.ui.theme.LivefrontCodeChallengeTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.runners.JUnit4

@HiltAndroidTest
@RunWith(JUnit4::class)
class MainActivityTests {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @OptIn(ExperimentalTestApi::class)
    @Before
    fun setup() {
        hiltRule.inject()
        composeTestRule.activity.setContent {
            val navController = rememberNavController()
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            LivefrontCodeChallengeTheme {
                NavHost(
                    navController = navController,
                    startDestination = "homeScreen"
                ) {
                    composable("homeScreen") { HomeScreen(navController) }
                    composable(
                        "detailView/{detailKey}",
                        arguments = listOf(navArgument("detailKey") { type = NavType.StringType })
                    ) {
                        CharacterDetailScreen(navController = navController)
                    }
                }
            }
        }
        composeTestRule.waitUntilAtLeastOneExists(
            hasTestTag(TestTags.CHARACTER_CARD),
            5000
        )
    }

    @Test
    fun shouldDisplayLoadedCards() {
        composeTestRule.onNodeWithTag(TestTags.CHARACTER_LIST)
            .onChildren()
            .assertCountEquals(2)
    }

    @Test
    fun shouldBeAbleToClickOnCard() {
        composeTestRule.onNodeWithText("Superman").performClick()
        composeTestRule.onNodeWithText("Occupation: Reporter").assertExists()
    }

    @Test
    fun shouldBeAbleToNavigateAwayFromDetailPage() {
        composeTestRule.onNodeWithText("Superman").performClick()
        composeTestRule.onNodeWithText("Occupation: Reporter").assertExists()
        composeTestRule.onNode(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button))
            .performClick()
        composeTestRule.onNodeWithTag(TestTags.CHARACTER_LIST)
            .onChildren()
            .assertCountEquals(2)
    }

    @Test
    fun shouldBeAbleToUseRandomCharacterButton() {
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(
                SemanticsProperties.ContentDescription,
                listOf(appContext.getString(R.string.random_icon))
            )
        ).performClick()
        composeTestRule.onNodeWithTag(TestTags.CHARACTER_LIST)
            .assertDoesNotExist()
    }

    @Test
    fun shouldBeAbleToSearchForACharacter() {
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(
                SemanticsProperties.ContentDescription,
                listOf(appContext.getString(R.string.search_icon))
            )
        ).performClick()

        composeTestRule.onNodeWithText(appContext.getString(R.string.search_placeholder_text))
            .performTextInput("Batman")

        composeTestRule.onNode(
            SemanticsMatcher.expectValue(
                SemanticsProperties.ContentDescription,
                listOf("Search", appContext.getString(R.string.search_icon))
            )
        ).performImeAction()
        composeTestRule.onNodeWithText("Occupation: Businessman").assertExists()
    }
}
