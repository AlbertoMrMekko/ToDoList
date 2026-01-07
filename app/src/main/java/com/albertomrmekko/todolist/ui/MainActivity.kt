package com.albertomrmekko.todolist.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.albertomrmekko.todolist.domain.model.AppTheme
import com.albertomrmekko.todolist.ui.apptheme.AppViewModel
import com.albertomrmekko.todolist.ui.navigation.NavGraph
import com.albertomrmekko.todolist.ui.theme.ToDoListTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appViewModel: AppViewModel = hiltViewModel()
            val theme by appViewModel.theme.collectAsState()

            ToDoListTheme(
                darkTheme = theme == AppTheme.DARK
            ) {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}