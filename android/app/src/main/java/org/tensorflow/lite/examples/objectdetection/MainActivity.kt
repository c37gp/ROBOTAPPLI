package org.tensorflow.lite.examples.objectdetection

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.tensorflow.lite.examples.objectdetection.databinding.ActivityMainBinding
import com.robot.control.RobotController

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var robotController: RobotController

    // 👉 IMPORTANT : référence vers l’IA
    private var objectDetectorHelper: ObjectDetectorHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        // 🤖 ROBOT INIT
        robotController = RobotController()
        robotController.start()

        // 🔥 ATTENDRE QUE L’IA EXISTE PUIS BRANCHER
        setupAIHook()
    }

    private fun setupAIHook() {

        // ⚠️ Ici on doit attendre que le helper soit créé ailleurs dans le code
        // Dans la base TensorFlow, il est souvent dans CameraFragment

        // 👉 SOLUTION SIMPLE : boucle de tentative (safe)
        activityMainBinding.root.postDelayed({

            try {
                val fragment = supportFragmentManager
                    .fragments
                    .firstOrNull()

                if (fragment is ObjectDetectorFragmentHook) {

                    fragment.setDetectionListener { label, score ->
                        robotController.onDetection(label, score)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }, 1500)
    }

    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            finishAfterTransition()
        } else {
            super.onBackPressed()
        }
    }
}
