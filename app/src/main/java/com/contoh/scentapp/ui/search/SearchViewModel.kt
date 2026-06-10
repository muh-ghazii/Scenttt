package com.contoh.scentapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.contoh.scentapp.data.model.AromaFilter
import com.contoh.scentapp.data.model.Product
import com.contoh.scentapp.data.model.SearchUiState
import com.contoh.scentapp.data.model.UsageFilter
import com.contoh.scentapp.data.repository.ProductRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val repository: ProductRepository = ProductRepository.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                aromaFilters = repository.aromaFilters,
                usageFilters = repository.usageFilters
            )
        }
        viewModelScope.launch {
            _uiState
                .map { Triple(it.query, it.selectedAromaFilters, it.selectedUsage) }
                .distinctUntilChanged()
                .debounce(300L)
                .collect { (q, aroma, usage) ->
                    runSearch(q, aroma, usage)
                }
        }
        runSearch("", emptySet(), null)
    }

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    fun toggleAromaFilter(filterId: String) {
        _uiState.update { state ->
            val current = state.selectedAromaFilters.toMutableSet()
            if (filterId in current) current.remove(filterId) else current.add(filterId)
            state.copy(selectedAromaFilters = current)
        }
    }

    fun toggleUsageFilter(filterId: String) {
        _uiState.update { state ->
            val newUsage = if (state.selectedUsage == filterId) null else filterId
            state.copy(selectedUsage = newUsage)
        }
    }

    fun clearAllFilters() {
        _uiState.update {
            it.copy(
                selectedAromaFilters = emptySet(),
                selectedUsage        = null,
                query                = ""
            )
        }
    }

    fun toggleFavorite(productId: Int) {
        _uiState.update { state ->
            val updated = state.results.map {
                if (it.id == productId) it.copy(isFavorite = !it.isFavorite) else it
            }
            state.copy(results = updated)
        }
    }

        fun applyFilters() {
        val state = _uiState.value
        runSearch(state.query, state.selectedAromaFilters, state.selectedUsage)
    }

    private fun runSearch(
        query        : String,
        aromaFilters : Set<String>,
        usageFilter  : String?
    ) {
        val results = repository.searchProducts(query, aromaFilters, usageFilter)
        _uiState.update { it.copy(results = results, isLoading = false) }
    }
}

class SearchViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}