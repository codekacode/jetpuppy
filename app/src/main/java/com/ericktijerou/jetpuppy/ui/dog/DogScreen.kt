/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ericktijerou.jetpuppy.ui.dog

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.ericktijerou.jetpuppy.R
import com.ericktijerou.jetpuppy.ui.entity.Dog
import com.ericktijerou.jetpuppy.ui.theme.BlueOnboarding
import com.ericktijerou.jetpuppy.ui.theme.JetpuppyTheme
import com.ericktijerou.jetpuppy.util.EMPTY
import com.ericktijerou.jetpuppy.util.JetPuppyDataManager
import com.ericktijerou.jetpuppy.util.PageIndicator
import com.ericktijerou.jetpuppy.util.Pager
import com.ericktijerou.jetpuppy.util.PagerState
import com.ericktijerou.jetpuppy.util.ThemedPreview
import com.ericktijerou.jetpuppy.util.lerp
import com.ericktijerou.jetpuppy.util.verticalGradientScrim
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.launch

private val InfoContainerMaxHeight = 380.dp
private val InfoContainerMinHeight = 90.dp

@Composable
fun DogScreen(viewModel: DogViewModel, dogId: String, onBackPressed: () -> Unit) {
    val dog = viewModel.getPuppyById(dogId)
    DogScreenBody(dog = dog, onBackPressed)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DogScreenBody(dog: Dog, onBackPressed: () -> Unit) {
    BoxWithConstraints {
        val infoSheetState = rememberSwipeableState(SheetState.Open)
        val infoMaxHeightInPixels = with(LocalDensity.current) { InfoContainerMaxHeight.toPx() }
        val infoMinHeightInPixels = with(LocalDensity.current) { InfoContainerMinHeight.toPx() }
        val dragRange = infoMaxHeightInPixels - infoMinHeightInPixels
        val scope = rememberCoroutineScope()
        val images = listOf(dog.imageUrl, dog.imageUrl, dog.imageUrl)
        val pagerState = remember { PagerState() }
        pagerState.maxPage = (images.size - 1).coerceAtLeast(0)
        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .swipeable(
                    state = infoSheetState,
                    anchors = mapOf(
                        0f to SheetState.Closed,
                        -dragRange to SheetState.Open
                    ),
                    thresholds = { _, _ -> FractionalThreshold(0.5f) },
                    orientation = Orientation.Vertical
                )
        ) {
            val openFraction = if (infoSheetState.offset.value.isNaN()) {
                0f
            } else {
                -infoSheetState.offset.value / dragRange
            }.coerceIn(0f, 1f)
            val (image, containerInfo, topBar, imageIndicator) = createRefs()
            val offsetY = lerp(
                infoMaxHeightInPixels,
                0f,
                openFraction
            )

            Pager(
                state = pagerState,
                modifier = Modifier
                    .clickable(
                        enabled = infoSheetState.currentValue == SheetState.Open,
                        onClick = {
                            scope.launch {
                                infoSheetState.animateTo(SheetState.Closed)
                            }
                        }
                    )
                    .constrainAs(image) {
                        linkTo(
                            start = parent.start,
                            top = parent.top,
                            end = parent.end,
                            bottom = containerInfo.top,
                            bottomMargin = (-32).dp
                        )
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
            ) {
                CoilImage(
                    data = dog.imageUrl,
                    contentScale = ContentScale.Crop,
                    contentDescription = EMPTY,
                    modifier = Modifier.fillMaxSize()
                )
            }

            DogTopBar(
                onBackPressed = onBackPressed,
                modifier = Modifier
                    .height(64.dp)
                    .constrainAs(topBar) {
                        linkTo(start = parent.start, end = parent.end)
                        top.linkTo(parent.top)
                        width = Dimension.fillToConstraints
                    }
            )

            DogPageIndicator(
                modifier = Modifier
                    .height(72.dp)
                    .constrainAs(imageIndicator) {
                        bottom.linkTo(image.bottom, margin = 8.dp)
                        linkTo(start = parent.start, end = parent.end)
                        width = Dimension.fillToConstraints
                    },
                count = images.size, currentPage = pagerState.currentPage
            )

            Surface(
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                modifier = Modifier
                    .constrainAs(containerInfo) {
                        linkTo(start = parent.start, end = parent.end)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                        val actualHeight = InfoContainerMaxHeight - offsetY.dp
                        height = Dimension.value(actualHeight.coerceAtLeast(InfoContainerMinHeight))
                    }
            ) {
                InfoContainer(
                    dog,
                    Modifier
                        .height(InfoContainerMinHeight)
                        .clickable(
                            enabled = infoSheetState.currentValue == SheetState.Closed,
                            onClick = {
                                scope.launch {
                                    infoSheetState.animateTo(SheetState.Open)
                                }
                            }
                        )
                )
            }
        }
    }
}

@Composable
fun DogPageIndicator(modifier: Modifier, count: Int, currentPage: Int) {
    Box(modifier, contentAlignment = Alignment.Center) {
        DogGradient(modifier = Modifier.fillMaxSize(), 0f, 1f)
        PageIndicator(pagesCount = count, currentPageIndex = currentPage, color = Color.White)
    }
}

@Composable
fun DogTopBar(modifier: Modifier, onBackPressed: () -> Unit) {
    ConstraintLayout(modifier) {
        val (back, share) = createRefs()
        DogGradient(modifier = Modifier.fillMaxSize(), 1f, 0f)

        IconButton(
            onClick = onBackPressed,
            Modifier.constrainAs(back) {
                start.linkTo(parent.start, margin = 8.dp)
                top.linkTo(parent.top, margin = 8.dp)
            }
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = EMPTY,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }

        IconButton(
            onClick = { },
            Modifier.constrainAs(share) {
                end.linkTo(parent.end, margin = 8.dp)
                top.linkTo(parent.top, margin = 8.dp)
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = EMPTY,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun InfoContainer(dog: Dog, modifier: Modifier) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.primary)
    ) {

        val rowsModifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, bottom = 20.dp)
        TitleRow(title = "${dog.name}, ${dog.age}", breed = dog.breed, modifier)
        Text(
            text = stringResource(id = dog.summary),
            style = MaterialTheme.typography.body2,
            overflow = TextOverflow.Ellipsis,
            modifier = rowsModifier
        )
        listOf(
            R.string.label_sex to dog.gender,
            R.string.label_weight to dog.weight,
            R.string.label_color to dog.color
        ).forEach {
            InfoItemRow(modifier = rowsModifier, label = it.first, value = it.second)
        }
        AdoptButton()
    }
}

@Composable
fun AdoptButton() {
    Button(
        onClick = {},
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp, top = 16.dp)
            .height(48.dp)
            .fillMaxSize(),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = BlueOnboarding),
        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = stringResource(R.string.label_adopt),
            style = MaterialTheme.typography.button,
            color = Color.White
        )
    }
}

@Composable
fun TitleRow(title: String, breed: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(start = 24.dp)
                .align(Alignment.CenterVertically)
                .weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h5
            )
            Text(
                text = breed,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(top = 4.dp),
                color = JetpuppyTheme.colors.textSecondaryColor
            )
        }

        IconButton(
            onClick = { },
            Modifier
                .padding(end = 24.dp)
                .align(Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = Icons.Filled.FavoriteBorder,
                contentDescription = EMPTY,
                Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun InfoItemRow(@StringRes label: Int, value: String, modifier: Modifier) {
    Row(modifier) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.body2,
            modifier = Modifier.width(120.dp),
            color = JetpuppyTheme.colors.textSecondaryColor
        )
        Text(
            text = value,
            style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.W500)
        )
    }
}

@Composable
fun DogGradient(modifier: Modifier, startYPercentage: Float, endYPercentage: Float) {
    Spacer(
        modifier = modifier.verticalGradientScrim(
            color = Color.Black.copy(alpha = 0.3f),
            startYPercentage = startYPercentage,
            endYPercentage = endYPercentage
        )
    )
}

enum class SheetState { Open, Closed }

@Preview("Dog screen body")
@Composable
fun PreviewDogScreenBody() {
    ThemedPreview {
        val puppySample = JetPuppyDataManager.puppy
        DogScreenBody(puppySample) {}
    }
}

@Preview("Dog screen body dark")
@Composable
fun PreviewDogScreenBodyDark() {
    ThemedPreview(darkTheme = true) {
        val puppySample = JetPuppyDataManager.puppy
        DogScreenBody(puppySample) {}
    }
}
