package com.albertomrmekko.todolist.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.albertomrmekko.todolist.ui.group.GroupScreen
import com.albertomrmekko.todolist.ui.task.TaskScreen
import com.albertomrmekko.todolist.ui.task.TaskViewModel

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
            TaskScreen(viewModel = viewModel)
        }
    }
}