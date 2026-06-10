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
    const val ORDER_SUCCESS = "order_success"
    const val ACCOUNT_DETAIL = "account_detail"
    const val SHIPPING_ADDRESS = "shipping_address"
    const val LANGUAGE = "language"
    const val SALES = "sales"
    const val ADD_PRODUCT = "add_product"

    fun detailRoute(productId: Int) = "detail/$productId"
    fun searchRoute(query: String = "") =
        if (query.isBlank()) "search?query=" else "search?query=$query"
}