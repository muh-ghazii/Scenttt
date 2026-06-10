package com.contoh.scentapp.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.contoh.scentapp.data.repository.CartRepository
import com.contoh.scentapp.data.repository.ProductRepository
import com.contoh.scentapp.ui.auth.AuthViewModel
import com.contoh.scentapp.ui.cart.CartViewModel
import com.contoh.scentapp.ui.detail.DetailViewModel
import com.contoh.scentapp.ui.favorite.FavoriteViewModel
import com.contoh.scentapp.ui.home.HomeViewModel
import com.contoh.scentapp.ui.profile.ProfileViewModel
import com.contoh.scentapp.ui.sales.SalesViewModel
import com.contoh.scentapp.ui.search.SearchViewModel


object ViewModelFactory {

    private val productRepo: ProductRepository
        get() = ProductRepository.getInstance()

    private val cartRepo: CartRepository
        get() = CartRepository.getInstance()

    fun authFactory(application: Application) = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthViewModel(application) as T
        }
    }

    fun homeFactory() = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(repository = productRepo) as T
        }
    }

    fun searchFactory() = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SearchViewModel(repository = productRepo) as T
        }
    }

    fun detailFactory(productId: Int) = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DetailViewModel(
                productId  = productId,
                repository = productRepo
            ) as T
        }
    }

    fun favoriteFactory() = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoriteViewModel(repository = productRepo) as T
        }
    }

    fun cartFactory() = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CartViewModel(repository = cartRepo) as T
        }
    }

    fun profileFactory() = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfileViewModel() as T
        }
    }

    fun salesFactory() = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SalesViewModel() as T
        }
    }
}