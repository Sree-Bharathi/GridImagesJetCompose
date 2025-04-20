package com.sree.imagelistapplication

import android.content.Intent

import android.os.Bundle
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.sree.imagelistapplication.ui.theme.ImageListApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImageListApplicationTheme {
                              OnBoardingScreen()

            }
        }
    }
}

  fun onCategoryClicked(categories: String, context: Context) {

    val intent = Intent(context,GalleryActivity::class.java)
    intent.putExtra("category", categories)
    context.startActivity(intent)

}

@Composable
fun OnBoardingScreen(modifier: Modifier = Modifier) {
    Surface(
      //  color = MaterialTheme.colorScheme.background,
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        val LightBlue = Color(0xFF0066FF)
        val Purple = Color(0xFF800080)
        val gradientColors = listOf(Cyan, LightBlue, Purple)
        val context = LocalContext.current
       var categories : List<String> = listOf( "Nature","Tigers","Animals","People")

        Column( modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)) {

            Text(

                "Select the type for the list of images",
                style = TextStyle(
                    brush = Brush.linearGradient(gradientColors)
                ),
                fontSize = 24.sp,
                modifier = Modifier.padding(top = 30.dp)

            )

            for(categories in categories)
            {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp, 40.dp)
                    .shadow(1.dp, RoundedCornerShape(4.dp))
                    .clickable {
                        onCategoryClicked(categories, context)
                    }
                    .padding(4.dp),
                contentAlignment = Alignment.Center,


            ) {
                Text(text = categories,
                    Modifier.align(Alignment.CenterStart)
                        .padding(start = 5.dp),
                    fontSize = 20.sp)
            }


        }
                }

    }
    }
}



@Preview(showBackground = true)
@Composable
fun OnBoardingPreview() {
    ImageListApplicationTheme {
        OnBoardingScreen()
    }
}
