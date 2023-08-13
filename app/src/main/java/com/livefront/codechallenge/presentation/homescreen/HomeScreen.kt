package com.livefront.codechallenge.presentation.homescreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.livefront.codechallenge.R
import com.livefront.codechallenge.core.CenteredText
import com.livefront.codechallenge.core.TestTags.CHARACTER_CARD
import com.livefront.codechallenge.core.TestTags.CHARACTER_LIST

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun HomeScreen(navController: NavHostController) {
    val viewModel = hiltViewModel<HomeScreenViewModel>()

    var active by rememberSaveable { mutableStateOf(false) }
    val isListEmpty = viewModel.characterList.isEmpty()

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.secondary
    ) {
        Scaffold(topBar = {
            TopAppBar(title = {
                Text(stringResource((R.string.app_name)))
            }, actions = {
                IconButton(onClick = {
                    if (!isListEmpty) {
                        navController.navigate(route = "detailView/${(1..viewModel.characterList.size).random()}")
                    }
                }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.dice_6_outline),
                        contentDescription = stringResource(id = R.string.random_icon)
                    )
                }
                IconButton(onClick = {
                    if (!isListEmpty) {
                        active = true
                    }
                }) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = stringResource(id = R.string.search_icon)
                    )
                }
            })
            if (active) {
                SearchBar(
                    query = viewModel.searchQuery,
                    onQueryChange = viewModel::onSearchQueryChanged,
                    onSearch = {
                        viewModel.getCharacter(it)?.let { character ->
                            navController.navigate(route = "detailView/${character.id}")
                        }
                        active = false
                    },
                    active = true,
                    onActiveChange = { active = it },
                    placeholder = { Text(stringResource(R.string.search_placeholder_text)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = stringResource(R.string.search_icon)
                        )
                    })
                {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(viewModel.filteredCharacters) { character ->
                            ListItem(headlineContent = { Text(text = character.name) },
                                modifier = Modifier
                                    .clickable {
                                        navController.navigate(route = "detailView/${character.id}")
                                        active = false
                                        viewModel.clearQuery()
                                    }
                                    .animateItemPlacement()
                            )
                        }
                    }
                }
            }
        })
        { padding ->
            if (viewModel.loading) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(top = 50.dp)
                    ) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier
                                .width(100.dp)
                                .height(100.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.loading_character_text),
                            fontSize = 20.sp,
                            modifier = Modifier.padding(top = 20.dp)
                        )
                    }
                }
            } else if (isListEmpty) {
                CenteredText(text = stringResource(R.string.unable_to_load_text))
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .testTag(CHARACTER_LIST)
                ) {
                    items(viewModel.characterList) { character ->
                        OutlinedCard(
                            shape = RoundedCornerShape(10),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 5.dp
                            ),
                            modifier = Modifier
                                .wrapContentHeight()
                                .padding(horizontal = 10.dp)
                                .testTag(CHARACTER_CARD)
                                .clickable(
                                    onClickLabel = stringResource(id = R.string.character_card_click_label) + " " + character.name,
                                    onClick = { navController.navigate(route = "detailView/${character.id}") }
                                )
                        ) {
                            ListItem(
                                leadingContent = {
                                    AsyncImage(
                                        model = character.images.sm,
                                        contentDescription = "An image of ${character.name}",
                                        modifier = Modifier
                                            .size(100.dp)
                                            .padding(
                                                start = 10.dp, end = 10.dp
                                            )
                                            .clip(CircleShape)
                                    )
                                },

                                headlineContent = {
                                    Text(
                                        fontSize = 22.sp,
                                        text = character.name,
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}