package org.tensorflow.lite.examples.objectdetection

/**
 * Upgrade v2 : interface de hook pour récupérer les résultats IA
 * sans modifier le code TensorFlow original
 */
interface ObjectDetectorFragmentHook {

    fun setDetectionListener(listener: (label: String, score: Float) -> Unit)
}
