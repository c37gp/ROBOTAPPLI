package org.tensorflow.lite.examples.objectdetection

import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import android.util.Log
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.Rot90Op
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.detector.Detection
import org.tensorflow.lite.task.vision.detector.ObjectDetector

class ObjectDetectorHelper(
    var threshold: Float = 0.5f,
    var numThreads: Int = 2,
    var maxResults: Int = 3,
    var currentDelegate: Int = 0,
    var currentModel: Int = 0,
    val context: Context,
    val objectDetectorListener: DetectorListener?
) {

    private var objectDetector: ObjectDetector? = null
    
    // External listener for robot integration
    private var externalListener: ((String, Float) -> Unit)? = null

    fun setExternalListener(listener: (String, Float) -> Unit) {
        externalListener = listener
    }

    init {
        setupObjectDetector()
    }

    fun clearObjectDetector() {
        objectDetector = null
    }

    // ✅ FIX: Add close method to properly clean up
    fun close() {
        objectDetector = null
    }

    fun setupObjectDetector() {

        val optionsBuilder =
            ObjectDetector.ObjectDetectorOptions.builder()
                .setScoreThreshold(threshold)
                .setMaxResults(maxResults)

        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(numThreads)

        when (currentDelegate) {

            DELEGATE_CPU -> {
                // default
            }

            DELEGATE_GPU -> {
                if (CompatibilityList().isDelegateSupportedOnThisDevice) {
                    baseOptionsBuilder.useGpu()
                } else {
                    objectDetectorListener?.onError("GPU not supported")
                }
            }

            DELEGATE_NNAPI -> {
                baseOptionsBuilder.useNnapi()
            }
        }

        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        // ✅ FIX: Use the correct model file names that user has downloaded
        val modelName =
            when (currentModel) {
                MODEL_MOBILENETV1 -> "lite-model_ssd_mobilenet_v1_1_metadata_2.tflite"
                MODEL_EFFICIENTDETV0 -> "lite-model_efficientdet_lite0_detection_metadata_1.tflite"
                MODEL_EFFICIENTDETV1 -> "lite-model_efficientdet_lite1_detection_metadata_1.tflite"
                MODEL_EFFICIENTDETV2 -> "lite-model_efficientdet_lite2_detection_metadata_1.tflite"
                else -> "lite-model_ssd_mobilenet_v1_1_metadata_2.tflite"
            }

        try {
            objectDetector =
                ObjectDetector.createFromFileAndOptions(
                    context,
                    modelName,
                    optionsBuilder.build()
                )

        } catch (e: IllegalStateException) {
            objectDetectorListener?.onError("Init failed")
            Log.e("ObjectDetector", e.message ?: "error")
        }
    }

    fun detect(image: Bitmap, imageRotation: Int) {

        if (objectDetector == null) {
            setupObjectDetector()
        }

        var inferenceTime = SystemClock.uptimeMillis()

        val imageProcessor =
            ImageProcessor.Builder()
                .add(Rot90Op(-imageRotation / 90))
                .build()

        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(image))

        val results = objectDetector?.detect(tensorImage)

        if (results != null) {
            for (detection in results) {

                val label =
                    detection.categories.firstOrNull()?.label ?: continue

                val score =
                    detection.categories.firstOrNull()?.score ?: continue

                // Call external listener for robot integration
                externalListener?.invoke(label, score)
            }

            inferenceTime = SystemClock.uptimeMillis() - inferenceTime

            // Call original TensorFlow UI callback
            objectDetectorListener?.onResults(
                results,
                inferenceTime,
                tensorImage.height,
                tensorImage.width
            )
        }
    }

    interface DetectorListener {
        fun onError(error: String)
        fun onResults(
            results: MutableList<Detection>?,
            inferenceTime: Long,
            imageHeight: Int,
            imageWidth: Int
        )
    }

    companion object {
        const val DELEGATE_CPU = 0
        const val DELEGATE_GPU = 1
        const val DELEGATE_NNAPI = 2

        const val MODEL_MOBILENETV1 = 0
        const val MODEL_EFFICIENTDETV0 = 1
        const val MODEL_EFFICIENTDETV1 = 2
        const val MODEL_EFFICIENTDETV2 = 3
    }
}
