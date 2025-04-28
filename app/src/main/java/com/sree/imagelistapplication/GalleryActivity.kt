package com.sree.imagelistapplication

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sree.imagelistapplication.ui.theme.ImageListApplicationTheme
import com.sree.imagelistapplication.viewModel.PhotosViewModel
import coil.compose.AsyncImage
import com.sree.imagelistapplication.local.PhotosEntity
import com.sree.imagelistapplication.navigations.ScreenNavigations
import com.sree.imagelistapplication.network.Photos
import com.sree.imagelistapplication.uiview.ImageListScreen
import com.sree.imagelistapplication.utills.AppStrings
import com.sree.imagelistapplication.viewModel.ImageListViewModel
import dagger.hilt.android.AndroidEntryPoint

var categories :String = ""
//var navController :NavHostController = TODO()


@AndroidEntryPoint
class GalleryActivity : ComponentActivity( ) {

  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        categories = intent.getStringExtra("category").toString()
        //callPexelImages(categories)
        setContent {
            ImageListApplicationTheme {
                //mainScreen()
                ScreenNavigations()
            }
           /* Surface (modifier = Modifier.safeContentPadding()){
                val viewModel: ImageListViewModel by viewModels()
                loadList(viewModel)
            }*/
        }

    }


}

@Composable
fun loadList(viewModel: ImageListViewModel)
{
    val uiState = viewModel.uiState.collectAsState()
    val query = rememberSaveable { mutableStateOf("") }

    // Now you call your ImageListScreen
    ImageListScreen(
        viewModel = viewModel,
        onClick = { imageId ->
            // Handle the click on an image here
            println("Clicked image id: $imageId")
            // maybe navigate or do something else
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mainScreen(navController: NavHostController)
{




    val viewModel = hiltViewModel<PhotosViewModel>()
    val photos by viewModel.photos.collectAsState()
    val photos_entity by viewModel.photosEntity.collectAsState()
    val isloading by viewModel.is_loading.collectAsState()
    var isLocal : Boolean =false
    LaunchedEffect(Unit) {
        viewModel.fetchPhotos()
        viewModel.getCategoryPhotos(categories)

    }
    if(photos_entity.size>0)
    {
        isLocal=true
    }else{
        isLocal=false
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Pexels Photos") })
        }
    ) { padding ->
        Box(modifier = Modifier
            .padding(padding)
            .fillMaxSize()) {
            if(isLocal)
                gridPhotoEntity(LocalContext.current,photos_entity,navController)
            else
            if (isloading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(40.dp)
                        .width(40.dp)
                        .align(Alignment.Center))
            } else {
                gridPhotos(LocalContext.current,photos,navController)
            }
        }
    }
}
@Composable
fun gridPhotos(context: Context, photos: List<Photos>, navController: NavHostController,) {
    Log.e("Gallery", "gridPhotos: ", )
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(10.dp)
    ) {
        items(photos.size)
        {
            Card(
                modifier = Modifier
                    .padding(6.dp)
                    .shadow(1.dp, RoundedCornerShape(4.dp))
                    .clickable ( enabled = true,
                        onClick = {onImageSelected(photos[it].src?.original,navController)}) ,
                shape = RoundedCornerShape(8.dp)
            ) {
                AsyncImage(
                    model = photos[it].src?.original,
                    contentDescription = "Loaded Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()

                )
            }
        }

    }
}

fun onImageSelected(src: String?, navController: NavHostController) {
    println("onImageSelected------>"+src)
    val encodedUrl = Uri.encode(src)
    navController.navigate("${AppStrings.FullImageScreen}/$encodedUrl")

   // navController.navigate(AppStrings.FullImageScreen+"/"+src)

}


@Composable
fun gridPhotoEntity(context: Context, photos: List<PhotosEntity>, navController: NavHostController) {
        Log.e("Gallery", "gridPhotoEntity: ", )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(10.dp)
        ) {
            items(photos.size)
            {
                Card(
                    modifier = Modifier
                        .padding(6.dp)
                        .shadow(1.dp, RoundedCornerShape(4.dp))
                        .clickable ( enabled = true,
                            onClick = {onImageSelected(photos[it].url,navController)}) ,
                    shape = RoundedCornerShape(8.dp)

                ) {
                    AsyncImage(
                        model = photos[it].url,
                        contentDescription = "Loaded Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()

                    )
                }
            }

        }
    }





