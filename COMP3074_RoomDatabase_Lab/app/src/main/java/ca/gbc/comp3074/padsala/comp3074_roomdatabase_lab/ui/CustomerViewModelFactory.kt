package ca.gbc.comp3074.padsala.comp3074_roomdatabase_lab.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ca.gbc.comp3074.padsala.comp3074_roomdatabase_lab.data.CustomerDao

class CustomerViewModelFactory(
    private val customerDao: CustomerDao
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        // Creates the requested CustomerViewModel.
        if (modelClass.isAssignableFrom(
                CustomerViewModel::class.java
            )
        ) {
            return CustomerViewModel(customerDao) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}
