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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.livefront.codechallenge.R
import com.livefront.codechallenge.utils.TestTags.CHARACTER_CARD
import com.livefront.codechallenge.utils.TestTags.CHARACTER_LIST
import com.livefront.codechallenge.data.Character
import com.livefront.codechallenge.data.Connections
import com.livefront.codechallenge.data.Images
import com.livefront.codechallenge.data.Work
import com.livefront.codechallenge.presentation.customcomposables.CenteredTextWithButton
import com.livefront.codechallenge.ui.theme.LivefrontCodeChallengeTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun HomeScreen(
    navigateToDetailScreen: (Long) -> Unit,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    var active by rememberSaveable { mutableStateOf(false) }

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = uiState.value.character) {
        uiState.value.character?.id?.let {
            navigateToDetailScreen(it)
            viewModel.characterNavigated()
            active = false
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.secondary
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(stringResource((R.string.app_name)))
                    },
                    actions = {
                        if (uiState.value.list.isNotEmpty()) {
                            IconButton(
                                onClick = { navigateToDetailScreen(uiState.value.list.random().id) }
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.dice_6_outline),
                                    contentDescription = stringResource(id = R.string.random_icon)
                                )
                            }
                            IconButton(onClick = { active = true }) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = stringResource(id = R.string.search_icon)
                                )
                            }
                        }
                    })
                if (active) {
                    SearchBar(
                        query = viewModel.searchQuery.value,
                        onQueryChange = viewModel::onSearchQueryChanged,
                        onSearch = {
                            viewModel.searchForCharacter(it)
                        },
                        active = true,
                        onActiveChange = { active = it },
                        placeholder = { Text(stringResource(R.string.search_placeholder_text)) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = stringResource(R.string.search_icon)
                            )
                        }
                    )
                    {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            items(viewModel.uiState.value.filteredCharacters) { character ->
                                ListItem(
                                    headlineContent = { Text(text = character.name) },
                                    modifier = Modifier
                                        .clickable {
                                            navigateToDetailScreen(character.id)
                                            active = false
                                            viewModel.clearQuery()
                                        }
                                        .animateItemPlacement()
                                )
                            }
                        }
                    }
                }
            }
        )
        { padding ->
            when {
                uiState.value.isLoading -> {
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
                }

                uiState.value.hasError -> {
                    CenteredTextWithButton(
                        text = stringResource(R.string.unable_to_load_text),
                        buttonText = stringResource(R.string.try_again_text),
                        onClick = viewModel::retryLoading
                    )
                }

                uiState.value.list.isNotEmpty() -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = padding,
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag(CHARACTER_LIST)
                    ) {
                        items(uiState.value.list) { character ->
                            CharacterCard(character = character, navigateToDetailScreen)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CharacterCard(
    character: Character,
    navigateToDetailScreen: (Long) -> Unit
) {
    OutlinedCard(
        shape = RoundedCornerShape(10),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        modifier = Modifier
            .wrapContentHeight()
            .padding(horizontal = 10.dp)
    ) {
        ListItem(
            leadingContent = {
                AsyncImage(
                    model = character.images.small,
                    contentDescription = stringResource(R.string.image_description) + character.name,
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
            modifier = Modifier
                .testTag(CHARACTER_CARD)
                .clickable(
                    onClickLabel = stringResource(id = R.string.character_card_click_label) + character.name,
                    onClick = { navigateToDetailScreen(character.id) }
                )
        )
    }
}

@Preview
@Composable
private fun CardPreview() {
    LivefrontCodeChallengeTheme {
        CharacterCard(
            character = Character(
                id = 1,
                name = "Superman",
                work = Work(occupation = "", base = ""),
                connections = Connections(groupAffiliation = "", relatives = ""),
                images = Images(extraSmall = "", small = "", medium = "", large = "")
            )
        ) {}
    }
}
