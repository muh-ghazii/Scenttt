package com.contoh.scentapp.data.model

data class SalesUiState(
    val totalPendapatan : Long              = 42_890_000L,
    val growthPercent   : String            = "+12.4% dari bulan lalu",
    val totalPenjualan  : Int               = 1_248,
    val products        : List<SalesProduct> = emptyList(),
    val activeOrders    : List<ActiveOrder>  = emptyList(),
    val isLoading       : Boolean            = true
) {
    val formattedPendapatan: String
        get() = "Rp ${"%,d".format(totalPendapatan).replace(",", ".")}"
}
