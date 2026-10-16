package ca.gbc.comp3074.padsala.comp3074_roomdatabase_lab.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ca.gbc.comp3074.padsala.comp3074_roomdatabase_lab.data.Customer


// Connects database results to the screen.
@Composable
fun CustomerApp(viewModel: CustomerViewModel) {

    var searchText by rememberSaveable {
        mutableStateOf("")
    }

    var appliedSearch by rememberSaveable {
        mutableStateOf("")
    }

    // Show all customers or matching customers.
    val customerFlow = remember(viewModel, appliedSearch) {
        if (appliedSearch.isBlank()) {
            viewModel.customers
        } else {
            viewModel.searchCustomers(appliedSearch)
        }
    }

    val customers by customerFlow.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )

    CustomerScreen(
        customers = customers,
        searchText = searchText,
        onSearchTextChange = { searchText = it },
        isSearching = appliedSearch.isNotBlank(),
        onSearch = {
            appliedSearch = searchText.trim()
        },
        onClearSearch = {
            searchText = ""
            appliedSearch = ""
        },
        onAddCustomer = viewModel::addCustomer,
        onDeleteCustomer = viewModel::deleteCustomer
    )
}

// Creates the customer form and search area.
@Composable
fun CustomerScreen(
    customers: List<Customer>,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    isSearching: Boolean,
    onSearch: () -> Unit,
    onClearSearch: () -> Unit,
    onAddCustomer: (String, Int, Boolean) -> Unit,
    onDeleteCustomer: (Customer) -> Unit
) {

    var name by rememberSaveable {
        mutableStateOf("")
    }

    var ageText by rememberSaveable {
        mutableStateOf("")
    }

    var isActive by rememberSaveable {
        mutableStateOf(false)
    }

    var message by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Room Customer Demo",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(12.dp))

        // Enter the customer's name.
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Customer name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        // Allow numeric age input only.
        OutlinedTextField(
            value = ageText,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    ageText = newValue
                }
            },
            label = { Text("Age") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Active customer")

            Spacer(Modifier.width(8.dp))

            Switch(
                checked = isActive,
                onCheckedChange = { isActive = it }
            )
        }

        // Check the input before saving.
        Button(
            onClick = {
                val age = ageText.toIntOrNull()

                if (name.isBlank() || age == null) {
                    message = "Enter a valid name and age."
                } else {
                    onAddCustomer(
                        name.trim(),
                        age,
                        isActive
                    )

                    // Clear the form.
                    name = ""
                    ageText = ""
                    isActive = false
                    message = "Customer submitted."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Customer")
        }

        message?.let {
            Spacer(Modifier.height(8.dp))
            Text(it)
        }

        Spacer(Modifier.height(12.dp))

        // Search challenge from the lab.
        OutlinedTextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            label = { Text("Search customers by name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onSearch,
                modifier = Modifier.weight(1f)
            ) {
                Text("Search")
            }

            OutlinedButton(
                onClick = onClearSearch,
                modifier = Modifier.weight(1f)
            ) {
                Text("Clear")
            }
        }

        Spacer(Modifier.height(12.dp))

        CustomerList(
            customers = customers,
            onDeleteCustomer = onDeleteCustomer,
            emptyMessage = if (isSearching) {
                "No matching customers."
            } else {
                "No customers in the database."
            },
            modifier = Modifier.weight(1f)
        )
    }
}

// Shows saved customers in a scrolling list.
@Composable
fun CustomerList(
    customers: List<Customer>,
    onDeleteCustomer: (Customer) -> Unit,
    emptyMessage: String = "No customers in the database.",
    modifier: Modifier = Modifier
) {

    if (customers.isEmpty()) {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(emptyMessage)
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = customers,
                key = { customer -> customer.id }
            ) { customer ->

                CustomerRow(
                    customer = customer,
                    onDelete = {
                        onDeleteCustomer(customer)
                    }
                )
            }
        }
    }
}

// Displays one customer and its Delete button.
@Composable
fun CustomerRow(
    customer: Customer,
    onDelete: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = customer.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Text("ID: ${customer.id}")
                Text("Age: ${customer.age}")

                Text(
                    if (customer.isActive) {
                        "Active customer"
                    } else {
                        "Inactive customer"
                    }
                )
            }

            TextButton(onClick = onDelete) {
                Text("Delete")
            }
        }
    }
}
