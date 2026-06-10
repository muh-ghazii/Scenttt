package com.contoh.scentapp.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.contoh.scentapp.data.model.Product
import com.contoh.scentapp.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FavoriteUiState(
    val favorites : List<Product> = emptyList(),
    val isLoading : Boolean       = true
) {
    val isEmpty: Boolean get() = favorites.isEmpty()
}

class FavoriteViewModel(
    private val repository: ProductRepository = ProductRepository.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteUiState())
    val uiState: StateFlow<FavoriteUiState> = _uiState.asStateFlow()

    init { observeFavorites() }

    private fun observeFavorites() {
        viewModelScope.launch {
            repository.products.collect { products ->
                _uiState.update {
                    it.copy(
                        favorites = products.filter { p -> p.isFavorite },
                        isLoading = false
                    )
                }
            }
        }
    }

    fun removeFromFavorite(productId: Int) {
        repository.toggleFavorite(productId)
    }
}

class FavoriteViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}