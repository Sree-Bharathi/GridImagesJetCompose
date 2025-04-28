package com.sree.imagelistapplication.navigations

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sree.imagelistapplication.mainScreen
import com.sree.imagelistapplication.uiview.FullImage
import com.sree.imagelistapplication.utills.AppStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenNavigations()
{
  val navController =rememberNavController()
  NavHost(navController = navController, startDestination = AppStrings.ImageListScreen , builder = {
    composable(AppStrings.ImageListScreen,)
    {
       /* mainScreen(onNavigate = {

          navController.navigate(AppStrings.FullImageScreen)
        }  ) */
      mainScreen(navController )
    }

   composable(AppStrings.FullImageScreen+"/{src}",)
   {

     val src =it.arguments?.getString("src")
     FullImage(src?:"path not found")

   }
  })
}