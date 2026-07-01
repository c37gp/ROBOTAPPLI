package org.tensorflow.lite.examples.objectdetection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.robot.control.RobotController

class MainActivity : AppCompatActivity() {

    private lateinit var robotController: RobotController
    private lateinit var objectDetectorHelper: ObjectDetectorHelper
    private val analyzer = SceneAnalyzer()
    private lateinit var robot: RobotController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Initialize Robot Controller with context for BLE
        robotController = RobotController(this)
        robotController.init()
        robot = RobotController()
        objectDetectorHelper.navigationListener = { objects ->

            val action = analyzer.decide(objects)
        
            when(action){
                SceneAnalyzer.Action.FORWARD -> robot.forward()
                SceneAnalyzer.Action.LEFT -> robot.left()
                SceneAnalyzer.Action.RIGHT -> robot.right()
                SceneAnalyzer.Action.BACK -> robot.back()
                SceneAnalyzer.Action.STOP -> robot.stop()
                SceneAnalyzer.Action.FOLLOW_GARBAGE -> robot.followGarbage()
            }
        }

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
        
        // ✅ FIX: Clear TensorFlow detector to prevent memory leaks
        objectDetectorHelper.clearObjectDetector()
        
        // Cleanup robot controller
        robotController.destroy()
    }
}
