package com.contoh.scentapp.data.model

data class SearchUiState(
    val query                : String           = "",
    val aromaFilters         : List<AromaFilter> = emptyList(),
    val usageFilters         : List<UsageFilter>  = emptyList(),
    val selectedAromaFilters : Set<String>       = emptySet(),
    val selectedUsage        : String?           = null,
    val results              : List<Product>     = emptyList(),
    val isLoading            : Boolean           = false
) {
    val resultCount     : Int     get() = results.size
    val hasActiveFilters: Boolean get() =
        selectedAromaFilters.isNotEmpty() || selectedUsage != null
}