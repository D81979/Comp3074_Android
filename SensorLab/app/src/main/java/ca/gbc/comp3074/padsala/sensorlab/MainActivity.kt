package ca.gbc.comp3074.padsala.sensorlab

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.gbc.comp3074.padsala.sensorlab.ui.theme.SensorLabTheme

class MainActivity : ComponentActivity(), SensorEventListener {

    // SensorManager lets us find sensors and listen for readings.
    private lateinit var sensorManager: SensorManager

    // A sensor can be null if the device doesn't have it.
    private var temperatureSensor: Sensor? = null
    private var gyroscopeSensor: Sensor? = null

    // Compose refreshes the screen when these values change.
    private var temperature by mutableStateOf<Float?>(null)
    private var gyroX by mutableStateOf(0f)
    private var gyroY by mutableStateOf(0f)
    private var gyroZ by mutableStateOf(0f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Find the two sensors on this device.
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        temperatureSensor =
            sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        gyroscopeSensor =
            sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        // Show the sensor screen using the project's theme.
        setContent {
            SensorLabTheme {
                SensorScreen(
                    temperature = temperature,
                    hasTemperature = temperatureSensor != null,
                    hasGyroscope = gyroscopeSensor != null,
                    x = gyroX,
                    y = gyroY,
                    z = gyroZ,
                    onReset = {
                        // Show zero until the next gyroscope reading.
                        gyroX = 0f
                        gyroY = 0f
                        gyroZ = 0f
                    }
                )
            }
        }
    }

    // Start listening when the activity becomes active.
    override fun onResume() {
        super.onResume()

        // ?.let runs this block only if the sensor exists.
        temperatureSensor?.let { sensor ->
            sensorManager.registerListener(
                this,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        gyroscopeSensor?.let { sensor ->
            sensorManager.registerListener(
                this,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    // Stop both listeners when the activity is paused.
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    // Android calls this when either sensor sends a reading.
    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                temperature = event.values[0] // Air temperature in °C.
            }

            Sensor.TYPE_GYROSCOPE -> {
                gyroX = event.values[0] // Rotation speed around X.
                gyroY = event.values[1] // Rotation speed around Y.
                gyroZ = event.values[2] // Rotation speed around Z.
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // This lab only displays readings, so no action is needed here.
    }
}

// Arrange the title and the two cards vertically.
@Composable
private fun SensorScreen(
    temperature: Float?,
    hasTemperature: Boolean,
    hasGyroscope: Boolean,
    x: Float,
    y: Float,
    z: Float,
    onReset: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Android Sensor Monitor",
                style = MaterialTheme.typography.headlineMedium
            )

            TemperatureCard(temperature, hasTemperature)
            GyroscopeCard(x, y, z, hasGyroscope, onReset)
        }
    }
}

// Show the temperature or explain why a reading isn't available yet.
@Composable
private fun TemperatureCard(temperature: Float?, available: Boolean) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Ambient Temperature",
                style = MaterialTheme.typography.titleLarge
            )

            val reading = when {
                !available -> "Temperature sensor is unavailable."
                temperature == null -> "Waiting for a temperature reading..."
                else -> "%.1f °C".format(temperature)
            }

            Text(reading)
        }
    }
}

// Show rotation speed around each axis in radians per second.
@Composable
private fun GyroscopeCard(
    x: Float,
    y: Float,
    z: Float,
    available: Boolean,
    onReset: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Gyroscope",
                style = MaterialTheme.typography.titleLarge
            )

            if (available) {
                Text("X: %.3f rad/s".format(x))
                Text("Y: %.3f rad/s".format(y))
                Text("Z: %.3f rad/s".format(z))

                // Run the reset action defined in MainActivity.
                Button(onClick = onReset) {
                    Text("Reset gyroscope")
                }
            } else {
                Text("Gyroscope is unavailable.")
            }
        }
    }
}