package com.livefront.codechallenge.presentation.detailscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.livefront.codechallenge.R
import com.livefront.codechallenge.presentation.composables.CenteredText
import com.livefront.codechallenge.utils.checkIfUnknown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CharacterDetailScreen(navController: NavController) {

    val viewModel = hiltViewModel<CharacterDetailViewModel>()

    val character = viewModel.character

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
                            onClick = { navController.navigateUp() },
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
            if (character != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    SubcomposeAsyncImage(
                        model = character.images.lg,
                        contentDescription = "An image of ${character.name}",
                        loading = { CircularProgressIndicator() },
                        error = { CenteredText(text = stringResource(id = R.string.load_image_error)) },
                        modifier = Modifier
                            .padding(
                                horizontal = 10.dp,
                                vertical = 30.dp
                            )
                            .fillMaxWidth()
                            .height(350.dp)
                    )

                    Text(
                        text = character.name,
                        fontSize = 26.sp,
                    )

                    Text(
                        text = "Occupation: ${character.work.occupation.checkIfUnknown()}",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )

                    Text(
                        text = "Base: ${character.work.base.checkIfUnknown()}",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )

                    Text(
                        text = "Affiliation(s): ${character.connections.groupAffiliation.checkIfUnknown()}",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            } else {
                CenteredText(text = stringResource(id = R.string.load_character_error))
            }
        }
    }
}
