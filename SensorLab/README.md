# SensorLab

## Testing Note

I tested the app on a Pixel 7 emulator. 
Both the ambient temperature sensor and the gyroscope were available 
in the emulator. I set the simulated temperature to 17.5 °C, and the app 
displayed 17.5 °C. While the emulator was still, the gyroscope showed 
0.000 rad/s on the X, Y, and Z axes.

## Reflection Questions

### 1. Why must getDefaultSensor() be treated as potentially returning null?

Different Android devices have different sensors. If a device lacks the requested sensor, `getDefaultSensor()` 
returns `null`. My app checks for this before registering a listener so it can display an “unavailable” message
and keep running.

### 2. Why should sensor listeners be unregistered when the activity is paused?

A registered listener can keep receiving readings while the user is away from the screen. My app calls 
`unregisterListener(this)` in `onPause()` to stop those updates and save battery. When the user returns, 
`onResume()` registers the available sensors again.

### 3. What is the difference between gyroscope angular velocity and device orientation?

Angular velocity tells me how fast the phone is turning around an axis. Orientation tells me which way 
the phone is facing. For example, if I turn the phone from portrait to landscape and then hold it still, its
orientation is now landscape, but its gyroscope readings settle near zero because the rotation has stopped.

### 4. What units are used for the gyroscope values in this lab?

The X, Y, and Z values are measured in radians per second (`rad/s`). Each value shows the rotation 
speed around its corresponding axis.

### 5. Why is battery temperature not a valid replacement for TYPE_AMBIENT_TEMPERATURE?

The ambient temperature sensor measures the air around the device. Battery temperature measures the 
battery itself, which can warm up during charging even when the room temperature stays the same. If the
ambient sensor is missing, my app displays an unavailable message.

### 6. How does mutableStateOf allow a SensorEvent callback to update the Compose UI?

`mutableStateOf` makes a value observable by Compose. In my app, `onSensorChanged()` assigns each new 
reading to a state variable such as `temperature` or `gyroX`. When that value changes, Compose
updates the part of the screen that reads it. For example, when the emulator sends 17.5 °C, the 
temperature card displays 17.5 °C.