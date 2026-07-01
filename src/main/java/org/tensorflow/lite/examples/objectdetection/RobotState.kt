package org.tensorflow.lite.examples.objectdetection

object RobotState {

    var lastCommand: String = "STOP"
    var battery: Int = 100

    var mode: String = "SEARCH"

    var detectedObjects: List<DetectionData> = emptyList()

    data class DetectionData(
        val label: String,
        val score: Float,
        val xCenter: Float
    )
}
