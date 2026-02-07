package com.albertomrmekko.todolist.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.albertomrmekko.todolist.ui.group.GroupScreen
import com.albertomrmekko.todolist.ui.task.TaskScreen
import com.albertomrmekko.todolist.ui.task.TaskViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.GroupList.route
    ) {
        composable(Screen.GroupList.route) {
            GroupScreen(navController = navController)
        }

        composable(
            route = Screen.TaskList.route,
            arguments = listOf(
                navArgument("groupId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val viewModel: TaskViewModel = hiltViewModel(backStackEntry)

            val uiState by viewModel.uiState.collectAsState()

            if (uiState.group != null) {
                TaskScreen(
                    group = uiState.group!!,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}