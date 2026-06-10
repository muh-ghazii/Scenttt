package com.contoh.scentapp.data.model

object Routes {
    const val LOGIN    = "login"
    const val REGISTER = "register"
    const val HOME     = "home"
    const val FAVORITE = "favorite"
    const val CART     = "cart"
    const val PROFILE  = "profile"
    const val DETAIL = "detail/{productId}"
    const val SEARCH = "search?query={query}"
    const val SHIPPING      = "shipping"
    const val UPLOAD_BUKTI  = "upload_bukti"
    const val ORDER_SUCCESS = "order_success/{isTransfer}"
    const val ACCOUNT_DETAIL   = "account_detail"
    const val SHIPPING_ADDRESS = "shipping_address"
    const val LANGUAGE         = "language"
    const val SALES       = "sales"
    const val ADD_PRODUCT = "add_product"

    // --- TAMBAHAN BARU UNTUK STEP 5 & 6 ---
    const val ORDER_HISTORY = "order_history"
    const val ORDER_DETAIL  = "order_detail/{orderId}"

    fun detailRoute(productId: Int) = "detail/$productId"

    fun searchRoute(query: String = "") =
        if (query.isBlank()) "search?query=" else "search?query=$query"

    fun orderSuccessRoute(isTransfer: Boolean) = "order_success/$isTransfer"

    // --- FUNGSI BARU UNTUK STEP 5 ---
    fun orderDetailRoute(orderId: String) = "order_detail/$orderId"
}