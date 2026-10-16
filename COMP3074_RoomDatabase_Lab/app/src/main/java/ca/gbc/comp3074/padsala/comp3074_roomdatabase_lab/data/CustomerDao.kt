package ca.gbc.comp3074.padsala.comp3074_roomdatabase_lab.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {

    // Adds a customer to the database.
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(customer: Customer)

    // Deletes an existing customer.
    @Delete
    suspend fun delete(customer: Customer)

    // Watches all customers, ordered by ID.
    @Query("SELECT * FROM customers ORDER BY id")
    fun getAllCustomers(): Flow<List<Customer>>

    // Finds customers whose names contain searchText.
    @Query(
        "SELECT * FROM customers " +
                "WHERE name LIKE '%' || :searchText || '%' " +
                "ORDER BY name"
    )
    fun searchCustomers(
        searchText: String
    ): Flow<List<Customer>>
}

