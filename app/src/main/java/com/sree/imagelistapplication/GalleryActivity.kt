package com.sree.imagelistapplication

import android.content.Context
import android.net.http.HttpException
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sree.imagelistapplication.network.RestApis
import com.sree.imagelistapplication.network.RetrofitHelper
import com.sree.imagelistapplication.ui.theme.ImageListApplicationTheme
import com.sree.imagelistapplication.viewModel.PhotosViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.sree.imagelistapplication.network.Photos
import java.io.IOException

var categories :String = ""



class GalleryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        categories = intent.getStringExtra("category").toString()
        //callPexelImages(categories)
        setContent {
            ImageListApplicationTheme {

                mainScreen()
            }
        }

    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mainScreen(viewModel :PhotosViewModel =viewModel())
{
    val photos by viewModel.photos.collectAsState()
    val isloading by viewModel.is_loading.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.fetchPhotos()
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Pexels Photos") })
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (isloading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(40.dp)
                        .width(40.dp)
                        .align(Alignment.Center))
            } else {
                gridPhotos(LocalContext.current,photos,)
            }
        }
    }
}
@Composable
fun gridPhotos(context: Context, photos: List<Photos>)
{
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(10.dp)
    ) {
     items(photos.size)
     {
           Card(
               modifier = Modifier.padding(6.dp).shadow(1.dp, RoundedCornerShape(4.dp)),
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

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
fun callPexelImages(categories: String) {
    var api_key :String = "OPZQAtdSFauDsuizXp6CePWA7tIoQtc9jaNg6nNwnXJhNmRV8b5inCFE"
    Log.d("callPexelImages",categories)
    val quotesApi = RetrofitHelper.getInstance().create(RestApis::class.java)
       GlobalScope.launch {

        try {
       val  response = quotesApi.searchPhotos(api_key,categories,10)

          //  val response = RetrofitHelper.api.searchPhotos(api_key, categories, 10)
         //   Log.d("Response: ", response.body().toString())
            if (response.isSuccessful) {
                val photos = response.body()?.photos ?: emptyList()
                photos.forEach {
                    println("Photo: ${it.url}, Photographer: ${it.photographer}")
                    println("Photo: ${it.src?.original}, Photographer: ${it.photographer}")

                }
            } else {
                println("API Error: ${response.errorBody()?.string()}")
            }
        } catch (e: IOException) {
            println("Network Error: ${e.message}")
        } catch (e: HttpException) {
            println("HTTP Error: ${e.message}")
        }
    }

}

