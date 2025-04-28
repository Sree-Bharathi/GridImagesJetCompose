package com.sree.imagelistapplication.uiview

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.URL

@Composable
fun ActionButton(text: String, icon: Any, onClick: () -> Unit) {

}

@ExperimentalMaterial3Api
@Composable
fun FullImage(path: String)
{
  //  println("Selected Image------------>"+path)
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Selected Image") })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(padding)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(screenHeight * 0.65f), // 3/4 of screen height
                contentAlignment = Alignment.Center
            ) {

                AsyncImage(
                    model = path,
                    contentDescription = "Loaded Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()

                )
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp) ,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Button(onClick = {
                    coroutineScope.launch {
                    downloadAndShareImage(context, path)
                }}) {
                    Icon(Icons.Filled.Share, contentDescription = "Share")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Share")
                }

                Button(onClick = {
                    coroutineScope.launch {
                        downloadAndSaveImage(context, path)
                    }}) {
                    Icon(Icons.Filled.Save, contentDescription = "Share")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save")
                }
            }
        }


        }
        }


private suspend fun downloadBitmapFromUrl(imageUrl: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL(imageUrl)
            val connection = url.openConnection()
            connection.connect()
            val input = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}


private suspend fun downloadAndShareImage(context: Context, imageUrl: String) {
    val bitmap = downloadBitmapFromUrl(imageUrl)
    bitmap?.let {
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        val file = File(cachePath, "shared_image.png")
        val fileOutputStream = FileOutputStream(file)
        it.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.close()

        val contentUri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, contentUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share via"))
    }
}

private suspend fun downloadAndSaveImage(context: Context, imageUrl: String) {
    val bitmap = downloadBitmapFromUrl(imageUrl)
    bitmap?.let {
        saveBitmapToGallery(context, it)
    }
}

private fun saveBitmapToGallery(context: Context, bitmap: Bitmap) {
    val filename = "IMG_${System.currentTimeMillis()}.png"
    Log.e("gallery", "saveBitmapToGallery: filename------>"+filename )
    val fos: OutputStream?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        fos = imageUri?.let { resolver.openOutputStream(it) }
    } else {
        val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File(imagesDir, filename)
        fos = FileOutputStream(image)
    }

    fos?.use {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
    }
    Toast.makeText(context, "Image saved in Gallery", Toast.LENGTH_SHORT).show()
}