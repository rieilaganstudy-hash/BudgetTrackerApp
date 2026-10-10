package com.budgettracker.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.budgettracker.app.data.converter.LocalDateConverter
import com.budgettracker.app.data.dao.GroceryExpenseDao
import com.budgettracker.app.data.dao.MonthlyBudgetDao
import com.budgettracker.app.data.model.GroceryExpense
import com.budgettracker.app.data.model.MonthlyBudget

@Database(
    entities = [GroceryExpense::class, MonthlyBudget::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateConverter::class)
abstract class BudgetTrackerDatabase : RoomDatabase() {
    abstract fun groceryExpenseDao(): GroceryExpenseDao
    abstract fun monthlyBudgetDao(): MonthlyBudgetDao

    companion object {
        @Volatile
        private var INSTANCE: BudgetTrackerDatabase? = null

        fun getInstance(context: Context): BudgetTrackerDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    BudgetTrackerDatabase::class.java,
                    "budget_tracker_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
