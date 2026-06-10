package com.contoh.scentapp.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.ui.platform.LocalContext
import com.contoh.scentapp.data.model.Routes
import com.contoh.scentapp.data.repository.SessionManager
import com.contoh.scentapp.ui.auth.LoginScreen
import com.contoh.scentapp.ui.auth.RegisterScreen
import com.contoh.scentapp.ui.cart.CartScreen
import com.contoh.scentapp.ui.detail.DetailScreen
import com.contoh.scentapp.ui.favorite.FavoriteScreen
import com.contoh.scentapp.ui.home.HomeScreen
import com.contoh.scentapp.ui.ordersuccess.OrderSuccessScreen
import com.contoh.scentapp.ui.profile.AccountDetailScreen
import com.contoh.scentapp.ui.profile.LanguageScreen
import com.contoh.scentapp.ui.profile.ProfileScreen
import com.contoh.scentapp.ui.profile.ShippingAddressScreen
import com.contoh.scentapp.ui.sales.AddProductScreen
import com.contoh.scentapp.ui.sales.SalesScreen
import com.contoh.scentapp.ui.search.SearchScreen
import com.contoh.scentapp.ui.shipping.ShippingScreen
import com.contoh.scentapp.ui.theme.ScentBlack
import com.contoh.scentapp.ui.theme.components.ScentBottomNavBar


private val bottomNavRoutes = setOf(
    Routes.HOME, Routes.FAVORITE, Routes.CART, Routes.PROFILE
)

@Composable
fun AppNavigation(startLoggedIn: Boolean = false) {
    val context        = LocalContext.current
    val navController  = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute   = backStackEntry?.destination?.route ?: Routes.LOGIN

    val showBottomBar = currentRoute in bottomNavRoutes

    Scaffold(
        containerColor = ScentBlack,
        bottomBar = {
            if (showBottomBar) {
                ScentBottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate   = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState    = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = if (startLoggedIn) Routes.HOME else Routes.LOGIN,
            modifier         = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(Routes.LOGIN) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onRegister = { navController.navigate(Routes.REGISTER) }
                )
            }
            composable(Routes.REGISTER) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onLogin = { navController.popBackStack() }
                )
            }
            composable(Routes.HOME) {
                HomeScreen(
                    onProductClick = { productId ->
                        navController.navigate(Routes.detailRoute(productId))
                    },
                    onSearchClick = {
                        navController.navigate(Routes.searchRoute())
                    }
                )
            }
            composable(Routes.FAVORITE) {
                FavoriteScreen(
                    onBack         = { navController.popBackStack() },
                    onProductClick = { productId ->
                        navController.navigate(Routes.detailRoute(productId))
                    }
                )
            }
            composable(Routes.CART) {
                CartScreen(
                    onBack             = { navController.popBackStack() },
                    onCheckout         = { navController.navigate(Routes.SHIPPING) },
                    onContinueShopping = { navController.navigate(Routes.HOME) }
                )
            }
            composable(Routes.PROFILE) {
                ProfileScreen(
                    onBack       = { navController.popBackStack() },
                    onDetailAkun = { navController.navigate(Routes.ACCOUNT_DETAIL) },
                    onAlamat     = { navController.navigate(Routes.SHIPPING_ADDRESS) },
                    onBahasa     = { navController.navigate(Routes.LANGUAGE) },
                    onPenjualan  = { navController.navigate(Routes.SALES) },
                    onLogout     = {
                        SessionManager.getInstance(context).clearSession()
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                )
            }
            composable(
                route     = Routes.DETAIL,
                arguments = listOf(navArgument("productId") { type = NavType.IntType })
            ) { backStack ->
                val productId = backStack.arguments?.getInt("productId") ?: return@composable
                DetailScreen(
                    productId = productId,
                    onBack    = { navController.popBackStack() }
                )
            }
            composable(
                route     = Routes.SEARCH,
                arguments = listOf(
                    navArgument("query") {
                        type         = NavType.StringType
                        defaultValue = ""
                        nullable     = false
                    }
                )
            ) { backStack ->
                SearchScreen(
                    initialQuery   = backStack.arguments?.getString("query") ?: "",
                    onBack         = { navController.popBackStack() },
                    onProductClick = { productId ->
                        navController.navigate(Routes.detailRoute(productId))
                    }
                )
            }
            composable(Routes.SHIPPING) {
                ShippingScreen(
                    onBack    = { navController.popBackStack() },
                    onConfirm = { navController.navigate(Routes.ORDER_SUCCESS) }
                )
            }
            composable(Routes.ORDER_SUCCESS) {
                OrderSuccessScreen(
                    onBackHome = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Routes.ACCOUNT_DETAIL) {
                AccountDetailScreen(onBack = { navController.popBackStack() })
            }

            composable(Routes.SHIPPING_ADDRESS) {
                ShippingAddressScreen(onBack = { navController.popBackStack() })
            }

            composable(Routes.LANGUAGE) {
                LanguageScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.SALES) {
                SalesScreen(
                    onBack       = { navController.popBackStack() },
                    onAddProduct = { navController.navigate(Routes.ADD_PRODUCT) }
                )
            }
            composable(Routes.ADD_PRODUCT) {
                AddProductScreen(
                    onBack = { navController.popBackStack() },
                    onSave = { navController.popBackStack() }
                )
            }
        }
    }
}