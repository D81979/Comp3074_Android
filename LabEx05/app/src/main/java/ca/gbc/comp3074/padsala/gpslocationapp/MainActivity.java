package ca.gbc.comp3074.padsala.gpslocationapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements LocationListener {

    private TextView tvLatitude;
    private TextView tvLongitude;
    private TextView tvStatus;
    private Button btnGetLocation;

    private LocationManager locationManager;
    private boolean waitingForLocation = false;

    private final Handler handler = new Handler(Looper.getMainLooper());

    // Stop waiting if GPS takes too long.
    private final Runnable timeout = () -> {
        stopLocationUpdates();
        tvStatus.setText(
                "No GPS signal yet. Try outdoors, or set an emulator location, "
                        + "then tap Get Location again."
        );
    };

    // Handle the user's permission choice.
    private final ActivityResultLauncher<String[]> permissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestMultiplePermissions(),
                    result -> {
                        if (hasPrecisePermission()) {
                            startGps();
                        } else {
                            tvStatus.setText(
                                    "Precise location is needed for GPS. "
                                            + "Allow it in Settings > Apps > "
                                            + "GPSLocationApp > Permissions > Location."
                            );
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Keep the screen clear of the phone's system bars.
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (view, insets) -> {
                    Insets bars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );
                    view.setPadding(
                            bars.left, bars.top, bars.right, bars.bottom
                    );
                    return insets;
                }
        );

        // Connect Java to the items in the XML screen.
        tvLatitude = findViewById(R.id.tvLatitude);
        tvLongitude = findViewById(R.id.tvLongitude);
        tvStatus = findViewById(R.id.tvStatus);
        btnGetLocation = findViewById(R.id.btnGetLocation);

        locationManager =
                (LocationManager) getSystemService(LOCATION_SERVICE);

        btnGetLocation.setOnClickListener(view -> {
            if (hasPrecisePermission()) {
                startGps();
            } else {
                permissionLauncher.launch(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                });
            }
        });
    }

    private boolean hasPrecisePermission() {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void startGps() {
        // Always check permission before accessing GPS.
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            tvStatus.setText("Please allow precise location access.");
            return;
        }

        if (locationManager == null
                || !locationManager.getAllProviders().contains(
                LocationManager.GPS_PROVIDER)) {
            tvStatus.setText("GPS is not available on this device.");
            return;
        }

        if (!locationManager.isProviderEnabled(
                LocationManager.GPS_PROVIDER)) {
            tvStatus.setText("Turn on Location, then tap Get Location again.");

            new AlertDialog.Builder(this)
                    .setTitle("Turn on Location")
                    .setMessage("GPS needs your device's Location setting turned on.")
                    .setPositiveButton("Open Settings", (dialog, which) ->
                            startActivity(new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS
                            )))
                    .setNegativeButton("Cancel", null)
                    .show();
            return;
        }

        tvLatitude.setText("Latitude: --");
        tvLongitude.setText("Longitude: --");
        tvStatus.setText("Waiting for a GPS location...");
        btnGetLocation.setEnabled(false);
        waitingForLocation = true;

        try {
            // Ask GPS for a fresh location.
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000L,
                    0f,
                    this
            );

            handler.postDelayed(timeout, 60000L);
        } catch (SecurityException exception) {
            stopLocationUpdates();
            tvStatus.setText("Location permission was removed. Please allow it again.");
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (!waitingForLocation) {
            return;
        }

        // Display the numbers received from GPS.
        tvLatitude.setText(String.format(
                Locale.US, "Latitude: %.6f", location.getLatitude()
        ));

        tvLongitude.setText(String.format(
                Locale.US, "Longitude: %.6f", location.getLongitude()
        ));

        tvStatus.setText("Location received from GPS.");
        stopLocationUpdates();
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        if (LocationManager.GPS_PROVIDER.equals(provider)
                && waitingForLocation) {
            stopLocationUpdates();
            tvStatus.setText("GPS was turned off. Turn on Location and try again.");
        }
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        // The button starts a new GPS request.
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Included for older Android versions.
    }

    private void stopLocationUpdates() {
        handler.removeCallbacks(timeout);

        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }

        waitingForLocation = false;
        btnGetLocation.setEnabled(true);
    }

    @Override
    protected void onStop() {
        super.onStop();

        boolean wasWaiting = waitingForLocation;
        stopLocationUpdates();

        if (wasWaiting) {
            tvStatus.setText("Location request paused. Tap Get Location to retry.");
        }
    }
}