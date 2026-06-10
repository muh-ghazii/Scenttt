package com.contoh.scentapp.ui.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.contoh.scentapp.data.model.ActiveOrder
import com.contoh.scentapp.data.model.OrderStatus
import com.contoh.scentapp.data.model.SalesProduct
import com.contoh.scentapp.data.model.SalesUiState
import com.contoh.scentapp.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SalesViewModel(
    // ✅ Pakai getInstance() agar instance-nya sama dengan HomeViewModel
    private val repository: ProductRepository = ProductRepository.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SalesUiState())
    val uiState: StateFlow<SalesUiState> = _uiState.asStateFlow()

    init {
        loadData()
        observeSalesProducts()
    }

    // ── Load orders (static) ─────────────────────────────────────────────────
    private fun loadData() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading    = false,
                    activeOrders = demoOrders()
                )
            }
        }
    }

    // ── ✅ Observe salesProducts dari Repository secara real-time ─────────────
    // Setiap kali addProduct() dipanggil, list ini otomatis ter-update
    private fun observeSalesProducts() {
        viewModelScope.launch {
            repository.salesProducts.collect { products ->
                _uiState.update { it.copy(products = products) }
            }
        }
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    // ✅ FIX: addProduct sekarang ke Repository, bukan hanya ke _uiState lokal.
    // Repository akan notify HomeViewModel secara otomatis via products Flow.
    fun addProduct(product: SalesProduct) {
        repository.addProduct(product)
    }

    // ✅ FIX: deleteProduct juga ke Repository agar sinkron dengan HomeScreen
    fun deleteProduct(productId: Int) {
        repository.deleteProduct(productId)
    }

    fun markAsPacked(orderId: String) {
        _uiState.update { state ->
            state.copy(
                activeOrders = state.activeOrders.map {
                    if (it.orderId == orderId) it.copy(status = OrderStatus.DIKEMAS) else it
                }
            )
        }
    }

    fun markAsShipped(orderId: String) {
        _uiState.update { state ->
            state.copy(
                activeOrders = state.activeOrders.map {
                    if (it.orderId == orderId) it.copy(status = OrderStatus.DIKIRIM) else it
                }
            )
        }
    }

    private fun demoOrders() = listOf(
        ActiveOrder(
            orderId   = "SC-8921",
            buyerName = "Julianne V.",
            itemCount = 2,
            status    = OrderStatus.DALAM_PROSES
        ),
        ActiveOrder(
            orderId   = "SC-8924",
            buyerName = "Marcus L.",
            itemCount = 1,
            status    = OrderStatus.DIKEMAS
        )
    )
}

class SalesViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SalesViewModel::class.java)) {
            return SalesViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}