package com.contoh.scentapp.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.contoh.scentapp.data.model.Routes
import com.contoh.scentapp.data.repository.SessionManager
import com.contoh.scentapp.ui.auth.LoginScreen
import com.contoh.scentapp.ui.auth.RegisterScreen
import com.contoh.scentapp.ui.cart.CartScreen
import com.contoh.scentapp.ui.cart.UploadPaymentProofScreen
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
import com.contoh.scentapp.ui.sales.SalesViewModel
import com.contoh.scentapp.ui.sales.SalesViewModelFactory
import com.contoh.scentapp.ui.search.SearchScreen
import com.contoh.scentapp.ui.shipping.ShippingScreen
import com.contoh.scentapp.ui.theme.ScentBlack
import com.contoh.scentapp.ui.theme.components.ScentBottomNavBar
import com.contoh.scentapp.ui.order.OrderHistoryScreen
import com.contoh.scentapp.ui.order.OrderDetailScreen

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
                    onBack           = { navController.popBackStack() },
                    onDetailAkun     = { navController.navigate(Routes.ACCOUNT_DETAIL) },
                    onAlamat         = { navController.navigate(Routes.SHIPPING_ADDRESS) },
                    onRiwayatPesanan = { navController.navigate(Routes.ORDER_HISTORY) },
                    onBahasa         = { navController.navigate(Routes.LANGUAGE) },
                    onPenjualan      = { navController.navigate(Routes.SALES) },
                    onLogout         = {
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
                    productId        = productId,
                    onBack           = { navController.popBackStack() },
                    onNavigateToCart = { navController.navigate(Routes.CART) }
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
                    onBack       = { navController.popBackStack() },
                    onCODSuccess = { navController.navigate(Routes.orderSuccessRoute(isTransfer = false)) },
                    onTransfer   = { navController.navigate(Routes.UPLOAD_BUKTI) }
                )
            }
            composable(Routes.UPLOAD_BUKTI) {
                UploadPaymentProofScreen(
                    onBack   = { navController.popBackStack() },
                    onSubmit = {
                        navController.navigate(Routes.orderSuccessRoute(true)) {
                            popUpTo(Routes.CART) { inclusive = true }
                        }
                    }
                )
            }
            composable(
                route     = Routes.ORDER_SUCCESS,
                arguments = listOf(navArgument("isTransfer") { type = NavType.BoolType })
            ) { backStack ->
                val isTransfer = backStack.arguments?.getBoolean("isTransfer") ?: false
                OrderSuccessScreen(
                    isTransfer = isTransfer,
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

            // ── ✅ FIX: SALES — ikat ViewModel ke backStackEntry ini ──────────
            // Tujuannya agar AddProduct bisa ambil ViewModel yang SAMA
            composable(Routes.SALES) { backStackEntry ->
                val salesViewModel: SalesViewModel = viewModel(
                    viewModelStoreOwner = backStackEntry,
                    factory             = SalesViewModelFactory()
                )
                SalesScreen(
                    viewModel    = salesViewModel,
                    onBack       = { navController.popBackStack() },
                    onAddProduct = { navController.navigate(Routes.ADD_PRODUCT) }
                )
            }

            // ── ✅ FIX: ADD_PRODUCT — pakai ViewModel yang SAMA dari SALES ────
            // Tanpa ini onSave() tidak akan update list di SalesScreen
            composable(Routes.ADD_PRODUCT) { currentEntry ->
                val salesEntry = remember(currentEntry) {
                    navController.getBackStackEntry(Routes.SALES)
                }
                val salesViewModel: SalesViewModel = viewModel(
                    viewModelStoreOwner = salesEntry,
                    factory             = SalesViewModelFactory()
                )
                AddProductScreen(
                    onBack = { navController.popBackStack() },
                    onSave = { newProduct ->
                        // ✅ Simpan ke Repository via ViewModel
                        // HomeViewModel otomatis ter-update karena collect repository.products
                        salesViewModel.addProduct(newProduct)
                        navController.popBackStack()
                    }
                )
            }

            // ── Order History & Detail ─────────────────────────────────────────
            composable(Routes.ORDER_HISTORY) {
                OrderHistoryScreen(
                    onBack             = { navController.popBackStack() },
                    onOrderDetailClick = { orderId ->
                        navController.navigate(Routes.orderDetailRoute(orderId))
                    }
                )
            }
            composable(
                route     = Routes.ORDER_DETAIL,
                arguments = listOf(navArgument("orderId") { type = NavType.StringType })
            ) { backStack ->
                val orderId = backStack.arguments?.getString("orderId") ?: ""
                OrderDetailScreen(
                    orderId = orderId,
                    onBack  = { navController.popBackStack() }
                )
            }
        }
    }
}