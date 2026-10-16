package ca.gbc.comp3074.padsala.comp3074_roomdatabase_lab.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Defines our Room database.
@Database(
    entities = [Customer::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun customerDao(): CustomerDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Creates the database only when needed.
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "customer_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}
