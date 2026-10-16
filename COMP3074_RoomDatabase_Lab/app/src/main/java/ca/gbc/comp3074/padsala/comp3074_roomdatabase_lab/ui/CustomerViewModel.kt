package ca.gbc.comp3074.padsala.comp3074_roomdatabase_lab.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.gbc.comp3074.padsala.comp3074_roomdatabase_lab.data.Customer
import ca.gbc.comp3074.padsala.comp3074_roomdatabase_lab.data.CustomerDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CustomerViewModel(
    private val customerDao: CustomerDao
) : ViewModel() {

    // Observes all saved customers.
    val customers: Flow<List<Customer>> =
        customerDao.getAllCustomers()

    // Searches customers by name.
    fun searchCustomers(
        searchText: String
    ): Flow<List<Customer>> {
        return customerDao.searchCustomers(searchText)
    }

    // Adds a new customer.
    fun addCustomer(
        name: String,
        age: Int,
        isActive: Boolean
    ) {
        viewModelScope.launch {
            customerDao.insert(
                Customer(
                    name = name,
                    age = age,
                    isActive = isActive
                )
            )
        }
    }

    // Deletes the selected customer.
    fun deleteCustomer(customer: Customer) {
        viewModelScope.launch {
            customerDao.delete(customer)
        }
    }
}
