package com.contoh.scentapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.contoh.scentapp.data.model.CartItem          // ← tambah
import com.contoh.scentapp.data.model.DetailUiState
import com.contoh.scentapp.data.repository.CartRepository    // ← tambah
import com.contoh.scentapp.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val productId   : Int,
    private val repository  : ProductRepository = ProductRepository.getInstance(),
    private val cartRepository: CartRepository  = CartRepository.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadDetail()
    }

    private fun loadDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val product = repository.getProductById(productId)
            val sizeOptions = repository.getSizeOptions(productId)
            val reviews = repository.getReviews(productId)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    product = product,
                    sizeOptions = sizeOptions,
                    reviews = reviews,
                    errorMessage = if (product == null) "Produk tidak ditemukan" else null
                )
            }
        }
    }

    fun onSizeSelected(sizeId: String) {
        _uiState.update { it.copy(selectedSizeId = sizeId) }
    }

    fun addToCart() {
        val product = _uiState.value.product ?: return
        cartRepository.addToCart(
            CartItem(
                productId = product.id,
                name = product.name,
                brand = product.brand,
                aromaProfile = product.aromaProfile.joinToString(", "), // List<String> → String
                volume = product.volume,
                pricePerItem = product.price.replace("[^0-9]".toRegex(), "").toInt() // String → Int
            )
        )
    }

    class DetailViewModelFactory(private val productId: Int) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                return DetailViewModel(productId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}