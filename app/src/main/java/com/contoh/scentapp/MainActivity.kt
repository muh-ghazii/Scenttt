package com.contoh.scentapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.contoh.scentapp.ui.navigation.AppNavigation
import com.contoh.scentapp.ui.theme.ScentAppTheme  // ← ganti ScentTheme → ScentAppTheme

class MainActivity : ComponentActivity() {

    companion object {
        var isDarkModeState by mutableStateOf(value = true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ScentAppTheme(darkTheme = isDarkModeState) { 
                AppNavigation(startLoggedIn = false)
            }
        }
    }
}