package ca.gbc.comp3074.padsala.comp3074_roomdatabase_lab.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Update

// This class represents the customers table.
@Entity(tableName = "customers")
data class Customer(

    // Room automatically creates a unique ID.
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val age: Int,
    val isActive: Boolean
)
