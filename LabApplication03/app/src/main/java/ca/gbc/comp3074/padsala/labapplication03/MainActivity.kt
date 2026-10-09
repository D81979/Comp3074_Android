package ca.gbc.comp3074.padsala.labapplication03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ca.gbc.comp3074.padsala.labapplication03.ui.theme.LabApplication03Theme
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LabApplication03Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    // Sample messages matching the lab.
                    val messages = listOf(
                        Message("Joe", "Hi!"),
                        Message("Jim", "How are you?"),
                        Message("Joe", "Test..1..2...3"),
                        Message("Joe", "I hate coding!!!")
                    )

                    // A scrollable list of message rows.
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        // Repeat the messages to make a longer list.
                        items(messages + messages + messages) { message ->
                            MessageCard(message = message)
                        }
                    }
                }
            }
        }
    }
}

// Draws one message row.
@Composable
fun MessageCard(message: Message, modifier: Modifier = Modifier) {
    Row(modifier = modifier.padding(8.dp)) {

        // Circular Android image with a red border.
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = "Profile image for ${message.author}",
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Red, CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Places the name above the message.
        Column {
            Text(
                text = message.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Rounded message bubble with a small shadow.
            Surface(
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 1.dp
            ) {
                Text(
                    text = message.body,
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LabApplication03Theme {
        Greeting("Android")
    }
}

// Stores the sender and message text
data class Message(
    val author: String,
    val body: String
)