package com.ufv.court.core.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object MySchedule : Screen("myschedule")
    object Profile : Screen("profile")
}

sealed class LeafScreen(
    val root: Screen,
    val route: String
) {

    open val arguments: List<NamedNavArgument> = emptyList()

    fun createRoute() = "${root.route}/$route"

    object Login : LeafScreen(Screen.Login, "login")
    object Register : LeafScreen(Screen.Login, "register")
    object ForgotPassword : LeafScreen(Screen.Login, "forgotpassword")

    object MySchedule : LeafScreen(Screen.MySchedule, "myschedule")
    class ScheduleDetails(screen: Screen) : LeafScreen(screen, "scheduledetails/{id}") {

        override val arguments = listOf(
            navArgument("id") {
                type = NavType.StringType
            }
        )

        fun createRoute(id: String): String {
            return "${root.route}/scheduledetails/$id"
        }
    }

    class Participants(screen: Screen) : LeafScreen(screen, "participants/{eventId}") {

        override val arguments = listOf(
            navArgument("eventId") {
                type = NavType.StringType
            }
        )

        fun createRoute(eventId: String): String {
            return "${root.route}/participants/$eventId"
        }
    }

    object EditEvent : LeafScreen(Screen.MySchedule, "editevent/{id}") {
        override val arguments = listOf(
            navArgument("id") {
                type = NavType.StringType
            }
        )

        fun createRoute(id: String): String {
            return "${root.route}/editevent/$id"
        }
    }

    object Home : LeafScreen(Screen.Home, "home")
    object Manage : LeafScreen(Screen.Home, "manage")
    object Calendar : LeafScreen(Screen.Home, "calendar")
    object Schedule : LeafScreen(Screen.Home, "schedule/{date}") {

        override val arguments = listOf(
            navArgument("date") {
                type = NavType.StringType
            }
        )

        fun createRoute(date: String): String {
            return "${root.route}/schedule/$date"
        }
    }

    object Profile : LeafScreen(Screen.Profile, "profile")
    object ChangePassword : LeafScreen(Screen.Profile, "changepassword")
    object EditProfile : LeafScreen(Screen.Profile, "editprofile")

    class Photo(screen: Screen) : LeafScreen(screen, "photo/{url}") {

        override val arguments = listOf(
            navArgument("url") {
                type = NavType.StringType
            }
        )

        fun createRoute(url: String): String {
            return "${root.route}/photo/$url"
        }
    }
}
