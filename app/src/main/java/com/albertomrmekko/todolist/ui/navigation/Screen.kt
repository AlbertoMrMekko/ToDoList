package com.albertomrmekko.todolist.ui.navigation

sealed class Screen(val route: String) {

    object GroupList : Screen("groups")

    object TaskList : Screen("tasks/{groupId}") {
        fun createRoute(groupId: Long) = "tasks/$groupId"
    }
}