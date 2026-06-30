package org.tensorflow.lite.examples.objectdetection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.robot.control.RobotController

class MainActivity : AppCompatActivity() {

    private lateinit var robotController: RobotController
    private lateinit var objectDetectorHelper: ObjectDetectorHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Initialize Robot Controller with context for BLE
        robotController = RobotController(this)
        robotController.init()

        // Initialize TensorFlow Object Detector
        objectDetectorHelper = ObjectDetectorHelper(
            context = this,
            objectDetectorListener = null
        )

        // Connect TensorFlow detection to Robot AI
        objectDetectorHelper.setExternalListener { label, score ->
            robotController.onCommand("detect:$label:$score")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cleanup robot controller
        robotController.destroy()
    }
}
