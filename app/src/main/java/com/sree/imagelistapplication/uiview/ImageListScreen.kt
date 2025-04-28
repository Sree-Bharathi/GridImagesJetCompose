package com.sree.imagelistapplication.uiview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sree.imagelistapplication.gridPhotos
import com.sree.imagelistapplication.utills.UiText
import com.sree.imagelistapplication.viewModel.ImageListViewModel

@OptIn(ExperimentalMaterial3Api ::class)
@Composable
fun ImageListScreen(
    modifier: Modifier=Modifier,
    viewModel:ImageListViewModel,
    onClick : (String)-> Unit) {

    val uiState = viewModel.uiState.collectAsState()
    val query = rememberSaveable() {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Pexels Photos") })
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (uiState.value.isloading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(40.dp)
                        .width(40.dp)
                        .align(Alignment.Center)
                )
            }
            if (uiState.value.error !is UiText.Idle) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxSize()
                        .align(Alignment.Center)
                )
                {
                    Text(text = uiState.value.error.getString())
                }
            }
            uiState.value.data?.let { list ->
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.padding(10.dp)
                ) {
                    items(list.size)
                    {
                        Card(
                            modifier = Modifier.padding(6.dp).shadow(1.dp, RoundedCornerShape(4.dp))
                                .clickable {
                                    onClick.invoke(list[it].src?.original.toString())
                                           },
                            shape = RoundedCornerShape(8.dp)

                        ) {
                            AsyncImage(
                                model = list[it].src?.original,
                                contentDescription = "Loaded Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()

                            )
                        }
                    }
                }
            }
        }

    }
}