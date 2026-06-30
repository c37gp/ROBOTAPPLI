# ROBOTAPPLI - Robot AI Detection

An Android application for robot object detection and obstacle avoidance using TensorFlow Lite.

## Features

- Real-time object detection using TensorFlow Lite models
- Obstacle avoidance AI for robot navigation
- Bluetooth LE communication with micro:bit
- Web server for remote control
- Multiple detection models supported (MobileNet V1, EfficientDet Lite0/1/2)

## Project Structure

```
ROBOTAPPLI/
├── android/
│   └── app/
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/
│       │   │   │   ├── com/robot/ai/          # AI components
│       │   │   │   ├── com/robot/control/    # Robot control components
│       │   │   │   └── org/tensorflow/       # TensorFlow detection
│       │   │   └── res/                      # Resources
│       │   └── androidTest/                 # Instrumented tests
│       ├── build.gradle                     # App module build config
│       └── download_models.gradle          # Model download tasks
├── build.gradle                            # Project build config
├── settings.gradle                         # Project settings
├── codemagic.yaml                          # CodeMagic CI configuration
└── gradle.properties                       # Gradle properties
```

## Prerequisites

- Android Studio 2023.2+ (Chipmunk or later)
- Java 17 JDK
- Android SDK 34
- Gradle 8.4+

## Building with CodeMagic

The project is configured to build automatically with CodeMagic. The `codemagic.yaml` file contains the build configuration.

### Required Environment Variables

- `JAVA_HOME` - Path to Java 17 JDK
- Android SDK with API level 34

### Build Commands

```bash
# Clean the project
./gradlew clean

# Build debug APK
./gradlew :android:app:assembleDebug

# Build release APK
./gradlew :android:app:assembleRelease

# Run tests
./gradlew :android:app:testDebugUnitTest
./gradlew :android:app:connectedAndroidTest
```

## Building Locally

1. Clone the repository:
   ```bash
   git clone https://github.com/c37gp/ROBOTAPPLI.git
   cd ROBOTAPPLI
   ```

2. Make gradlew executable:
   ```bash
   chmod +x gradlew
   ```

3. Build the project:
   ```bash
   ./gradlew :android:app:assembleDebug
   ```

4. The APK will be generated at:
   ```
   android/app/build/outputs/apk/debug/app-debug.apk
   ```

## Required Permissions

The app requires the following permissions:
- `CAMERA` - For object detection
- `BLUETOOTH`, `BLUETOOTH_ADMIN`, `BLUETOOTH_CONNECT`, `BLUETOOTH_SCAN` - For micro:bit communication
- `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` - For Bluetooth scanning (Android 12+)
- `INTERNET` - For web server functionality

## Configuration

### TensorFlow Models

The app downloads the following models on first run:
- MobileNet V1
- EfficientDet Lite0
- EfficientDet Lite1  
- EfficientDet Lite2

Models are downloaded to the `assets` directory.

### AI Configuration

The obstacle avoidance AI uses the following logic:
- **High priority obstacles** (person, car, chair, table, wall): Trigger avoidance
- **Trash detection** (bottle, plastic, can, trash): Highest priority
- **Distance estimation**: Based on detection confidence score
- **Decision smoothing**: Uses 3-frame history for stable decisions

## Usage

1. Connect to a micro:bit via Bluetooth
2. Launch the app
3. Point the camera at objects
4. The robot will automatically avoid obstacles and detect trash

### Commands

- `avant` - Move forward
- `gauche` - Turn left
- `droite` - Turn right
- `stop` - Stop
- `reculer` - Move backward
- `dechet` - Trash detected
- `auto_on` - Enable auto mode
- `auto_off` - Disable auto mode

## Web Server

The app includes a web server on port 8765 for remote control:

```bash
# Send command via HTTP POST
curl -X POST http://<device-ip>:8765 -d "cmd=avant"
```

## Troubleshooting

### Common Issues

1. **Build fails with Java version error**:
   - Ensure Java 17 is installed and `JAVA_HOME` is set
   - Update `gradle.properties` to use Java 17

2. **Bluetooth connection issues**:
   - Ensure location services are enabled (required for Android 12+)
   - Grant all required permissions

3. **Model download fails**:
   - Check internet connection
   - Models will be downloaded on first run

4. **Camera not working**:
   - Ensure camera permission is granted
   - Test on a physical device (emulators may have camera issues)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Run tests
5. Submit a pull request

## License

This project is licensed under the Apache License, Version 2.0. See the [LICENSE](LICENSE) file for details.

## Acknowledgments

- TensorFlow Lite for object detection
- Android CameraX for camera functionality
- NanoHTTPD for web server implementation
