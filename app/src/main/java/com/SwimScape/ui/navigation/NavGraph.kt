package com.swimscape.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.swimscape.ui.screens.AccountScreen
import com.swimscape.ui.screens.AlertsScreen
import com.swimscape.ui.screens.FavouritesScreen
import com.swimscape.ui.screens.LoginScreen
import com.swimscape.ui.screens.RegisterScreen
import com.swimscape.ui.screens.SpotDetailsScreen
import com.swimscape.ui.screens.SpotsScreen

sealed class Route(val route: String) {
    data object Login : Route("login")
    data object Register : Route("register")
    data object Spots : Route("spots")
    data object SpotDetails : Route("spot/{spotId}") {
        fun create(spotId: String) = "spot/$spotId"
    }
    data object Favourites : Route("favourites")
    data object Alerts : Route("alerts")
    data object Account : Route("account")
}

@Composable
fun SwimScapeNavGraph(
    navController: NavHostController = rememberNavController(),
    repository: com.swimscape.repository.SwimRepository
) {
    NavHost(
        navController = navController,
        startDestination = Route.Login.route
    ) {
        composable(Route.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Route.Register.route) },
                onLoginSuccess = { navController.navigate(Route.Spots.route) { popUpTo(0) { inclusive = true } } }
            )
        }
        composable(Route.Register.route) {
            RegisterScreen(
                onNavigateBack = { navController.popBackStack() },
                onRegisterSuccess = { navController.navigate(Route.Spots.route) { popUpTo(0) { inclusive = true } } }
            )
        }
        composable(Route.Spots.route) {
            SpotsScreen(
                repository = repository,
                onSpotClick = { spotId -> navController.navigate(Route.SpotDetails.create(spotId)) },
                onNavigateToFavourites = { navController.navigate(Route.Favourites.route) },
                onNavigateToAlerts = { navController.navigate(Route.Alerts.route) },
                onNavigateToAccount = { navController.navigate(Route.Account.route) }
            )
        }
        composable(
            route = Route.SpotDetails.route,
            arguments = listOf(navArgument("spotId") { type = NavType.StringType })
        ) { backStackEntry ->
            val spotId = backStackEntry.arguments?.getString("spotId") ?: ""
            SpotDetailsScreen(
                spotId = spotId,
                savedStateHandle = backStackEntry.savedStateHandle,
                repository = repository,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Route.Favourites.route) {
            FavouritesScreen(
                repository = repository,
                onSpotClick = { spotId -> navController.navigate(Route.SpotDetails.create(spotId)) },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Route.Alerts.route) {
            AlertsScreen(
                repository = repository,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Route.Account.route) {
            AccountScreen(
                repository = repository,
                onSignOut = { navController.navigate(Route.Login.route) { popUpTo(0) { inclusive = true } } },
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
