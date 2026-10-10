package com.budgettracker.app.data.repository

object GroceryCategories {
    const val FRUITS = "Fruits"
    const val VEGETABLES = "Vegetables"
    const val DAIRY = "Dairy"
    const val BAKERY = "Bakery"
    const val MEAT = "Meat & Fish"
    const val GRAINS = "Grains & Cereals"
    const val BEVERAGES = "Beverages"
    const val SNACKS = "Snacks"
    const val FROZEN = "Frozen Foods"
    const val OTHER = "Other"

    fun getAll(): List<String> = listOf(
        FRUITS,
        VEGETABLES,
        DAIRY,
        BAKERY,
        MEAT,
        GRAINS,
        BEVERAGES,
        SNACKS,
        FROZEN,
        OTHER
    )
}
