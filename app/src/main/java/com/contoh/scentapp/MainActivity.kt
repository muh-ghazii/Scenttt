package com.contoh.scentapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.contoh.scentapp.data.repository.SessionManager
import com.contoh.scentapp.ui.navigation.AppNavigation
import com.contoh.scentapp.ui.theme.ScentAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        // Update waktu terakhir buka aplikasi
        SessionManager.getInstance(this).updateLastOpen()

        // Cek apakah sudah login sebelumnya
        val startLoggedIn = SessionManager.getInstance(this).isLoggedIn

        setContent {
            ScentAppTheme {
                AppNavigation(startLoggedIn = startLoggedIn)
            }
        }
    }
}
