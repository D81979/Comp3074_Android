package ca.gbc.comp3074.padsala.paycalculator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText etHours, etHourlyRate, etTaxRate;
    private TextView tvPay, tvOvertimePay, tvTotalPay, tvTax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Keep the content clear of the phone's system bars.
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                });

        // Connect Java variables to the input fields in XML.
        etHours = findViewById(R.id.etHours);
        etHourlyRate = findViewById(R.id.etHourlyRate);
        etTaxRate = findViewById(R.id.etTaxRate);

        // Connect Java variables to the result labels in XML.
        tvPay = findViewById(R.id.tvPay);
        tvOvertimePay = findViewById(R.id.tvOvertimePay);
        tvTotalPay = findViewById(R.id.tvTotalPay);
        tvTax = findViewById(R.id.tvTax);

        Button btnCalculate = findViewById(R.id.btnCalculate);

        // Run calculatePay() whenever Calculate is tapped.
        btnCalculate.setOnClickListener(v -> calculatePay());
        Button btnAbout = findViewById(R.id.btnAbout);

        btnAbout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });
    }

    private void calculatePay() {
        // Clear previous results before validating new input.
        tvPay.setText("Regular pay: --");
        tvOvertimePay.setText("Overtime pay: --");
        tvTotalPay.setText("Total pay: --");
        tvTax.setText("Tax: --");

        Double hours = readNumber(etHours);
        Double hourlyRate = readNumber(etHourlyRate);
        Double taxPercent = readNumber(etTaxRate);

        // Stop if any field contains invalid input.
        if (hours == null || hourlyRate == null || taxPercent == null) {
            return;
        }

        if (taxPercent > 100) {
            etTaxRate.setError("Enter a tax percentage from 0 to 100");
            etTaxRate.requestFocus();
            return;
        }

        double pay;
        double overtimePay;

        if (hours <= 40) {
            pay = hours * hourlyRate;
            overtimePay = 0;
        } else {
            pay = 40 * hourlyRate;
            overtimePay = (hours - 40) * hourlyRate * 1.5;
        }

        double totalPay = pay + overtimePay;

        // Example: 10 percent becomes 0.10.
        double taxRate = taxPercent / 100.0;

        // Assignment formula: tax applies to regular pay.
        double tax = pay * taxRate;

        if (Double.isInfinite(totalPay)) {
            Toast.makeText(
                    this,
                    "The values are too large. Enter smaller numbers.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        // Display money with two decimal places.
        tvPay.setText(String.format(
                Locale.CANADA, "Regular pay: $%.2f", pay));

        tvOvertimePay.setText(String.format(
                Locale.CANADA, "Overtime pay: $%.2f", overtimePay));

        tvTotalPay.setText(String.format(
                Locale.CANADA, "Total pay: $%.2f", totalPay));

        tvTax.setText(String.format(
                Locale.CANADA, "Tax: $%.2f", tax));
    }

    // Read and validate one input field.
    private Double readNumber(EditText field) {
        field.setError(null);
        String input = field.getText().toString().trim();

        if (input.isEmpty()) {
            field.setError("This field is required");
            return null;
        }

        try {
            double value = Double.parseDouble(input);

            if (Double.isNaN(value)
                    || Double.isInfinite(value)
                    || value < 0) {
                field.setError("Enter a valid number of 0 or more");
                return null;
            }

            return value;

        } catch (NumberFormatException e) {
            field.setError("Enter a valid number");
            return null;
        }
    }
}