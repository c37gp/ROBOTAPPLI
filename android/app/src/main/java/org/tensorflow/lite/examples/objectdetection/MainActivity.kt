package org.tensorflow.lite.examples.objectdetection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.robot.control.RobotController
import com.robot.ai.AIController

class MainActivity : AppCompatActivity() {

    private lateinit var robotController: RobotController
    private lateinit var aiController: AIController
    private lateinit var objectDetectorHelper: ObjectDetectorHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // 🤖 ROBOT LAYER
        robotController = RobotController()

        // 🧠 IA DECISION LAYER
        aiController = AIController(robotController)

        // 📷 TENSORFLOW INIT
        objectDetectorHelper = ObjectDetectorHelper(
            context = this,
            objectDetectorListener = null
        )

        // 🔥 BRANCHEMENT FINAL (IMPORTANT)
        objectDetectorHelper.setExternalListener { label, score ->
            aiController.onDetection(label, score)
        }
    }
}
