
package ca.gbc.comp3074.padsala.comp3074_roomdatabase_lab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel

// Import database and UI classes from your actual project.
import ca.gbc.comp3074.padsala.comp3074_roomdatabase_lab.data.AppDatabase
import ca.gbc.comp3074.padsala.comp3074_roomdatabase_lab.ui.CustomerApp
import ca.gbc.comp3074.padsala.comp3074_roomdatabase_lab.ui.CustomerViewModel
import ca.gbc.comp3074.padsala.comp3074_roomdatabase_lab.ui.CustomerViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the Room database.
        val database = AppDatabase.getDatabase(applicationContext)

        setContent {
            MaterialTheme {

                // Create the ViewModel using the database DAO.
                val viewModel: CustomerViewModel = viewModel(
                    factory = CustomerViewModelFactory(
                        database.customerDao()
                    )
                )

                // Display the customer screen.
                CustomerApp(viewModel)
            }
        }
    }
}
