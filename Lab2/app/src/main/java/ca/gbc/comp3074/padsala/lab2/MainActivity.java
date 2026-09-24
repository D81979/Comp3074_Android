package ca.gbc.comp3074.padsala.lab2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView textOutput;

    private Button buttonAdd;
    private Button buttonSubtract;
    private Button buttonReset;
    private Button buttonStep;

    private int counter = 0;
    private int step = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textOutput = findViewById(R.id.textOutput);

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonSubtract = findViewById(R.id.buttonSubtract);
        buttonReset = findViewById(R.id.buttonReset);
        buttonStep = findViewById(R.id.buttonStep);

        buttonAdd.setOnClickListener(v -> {

            counter = counter + step;

            textOutput.setText(String.valueOf(counter));

        });

        buttonSubtract.setOnClickListener(v -> {

            counter = counter - step;

            textOutput.setText(String.valueOf(counter));

        });

        buttonReset.setOnClickListener(v -> {

            counter = 0;

            step = 1;

            textOutput.setText(String.valueOf(counter));

        });

        buttonStep.setOnClickListener(v -> {

            step = 2;

        });
    }
}