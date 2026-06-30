# TensorFlow Lite Models

This directory must contain the following model files for the object detection to work:

## Required Model Files
- [mobilenetv1.tflite](https://storage.googleapis.com/download.tensorflow.org/models/tflite/task_library/object_detection/mobile_object_localizer_v1_224_quant.tflite)
- [efficientdet-lite0.tflite](https://storage.googleapis.com/download.tensorflow.org/models/tflite/task_library/object_detection/efficientdet_lite0_quant.tflite)
- [efficientdet-lite1.tflite](https://storage.googleapis.com/download.tensorflow.org/models/tflite/task_library/object_detection/efficientdet_lite1_quant.tflite)
- [efficientdet-lite2.tflite](https://storage.googleapis.com/download.tensorflow.org/models/tflite/task_library/object_detection/efficientdet_lite2_quant.tflite)

## How to Add Models

### Option 1: Manual Download
1. Download the model files from the links above
2. Place them in this directory (src/main/assets/)
3. Rebuild your APK

### Option 2: Use the download script
Uncomment the following line in your build.gradle:
```gradle
apply from: file("download_models.gradle")
```

This will automatically download the models during the build process.

## Notes
- These models are used by ObjectDetectorHelper.kt for object detection
- The default model used is mobilenetv1.tflite
- If models are missing, the app will crash with FileNotFoundException or IllegalStateException
