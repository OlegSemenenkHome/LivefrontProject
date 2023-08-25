package com.livefront.codechallenge.presentation.detailscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.livefront.codechallenge.R
import com.livefront.codechallenge.presentation.customcomposables.CenteredText
import com.livefront.codechallenge.presentation.customcomposables.CenteredTextWithButton
import com.livefront.codechallenge.utils.checkIfUnknown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CharacterDetailScreen(
    onBackPressed: () -> Unit,
    viewModel: CharacterDetailViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.detail_screen_top_app_bar_title)) },
                    navigationIcon = {
                        IconButton(
                            onClick = onBackPressed,
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back_arrow_icon_description),
                            )
                        }
                    },
                )
            }
        ) { paddingValues ->
            when (val currentState = uiState.value) {
                is CharacterDetailState.Loading -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)

                    ) {
                        CircularProgressIndicator(
                            Modifier.size(200.dp)
                        )
                    }
                }

                is CharacterDetailState.Success -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .verticalScroll(rememberScrollState())
                    ) {
                        SubcomposeAsyncImage(
                            model = currentState.character.images.large,
                            contentDescription = stringResource(R.string.image_description) + currentState.character.name,
                        ) {
                            when (painter.state) {
                                is AsyncImagePainter.State.Loading -> {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .padding(
                                                horizontal = 10.dp,
                                                vertical = 30.dp
                                            )
                                            .fillMaxWidth()
                                            .height(350.dp)
                                    ) {
                                        CircularProgressIndicator(Modifier.size(250.dp))
                                    }
                                }

                                is AsyncImagePainter.State.Error -> {
                                    CenteredText(text = stringResource(id = R.string.load_image_error))
                                }

                                else -> {
                                    SubcomposeAsyncImageContent(
                                        modifier = Modifier
                                            .padding(
                                                horizontal = 10.dp,
                                                vertical = 30.dp
                                            )
                                            .fillMaxWidth()
                                            .height(350.dp)
                                    )
                                }
                            }
                        }

                        Text(
                            text = currentState.character.name,
                            fontSize = 26.sp,
                        )

                        Text(
                            text = stringResource(R.string.character_occupation) + currentState.character.work.occupation.checkIfUnknown(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )

                        Text(
                            text = stringResource(R.string.character_base) + currentState.character.work.base.checkIfUnknown(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )

                        Text(
                            text = stringResource(R.string.character_affiliation) + currentState.character.connections.groupAffiliation.checkIfUnknown(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }

                is CharacterDetailState.Error -> {
                    CenteredTextWithButton(
                        text = stringResource(id = R.string.load_character_error),
                        buttonText = stringResource(R.string.try_again_text),
                        onClick = viewModel::retryLoading
                    )
                }
            }
        }
    }
}
