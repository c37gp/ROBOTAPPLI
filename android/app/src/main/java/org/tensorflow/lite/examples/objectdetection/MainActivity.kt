package org.tensorflow.lite.examples.objectdetection

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.tensorflow.lite.examples.objectdetection.databinding.ActivityMainBinding
import com.robot.control.RobotController

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var robotController: RobotController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        // 🤖 INIT ROBOT LAYER
        robotController = RobotController()
        robotController.start()

        // 🔥 HOOK: connexion IA → robot
        initRobotHook()
    }

    /**
     * Upgrade v2 : liaison propre avec le flux TensorFlow
     * (on intercepte les résultats de détection sans casser le code IA)
     */
    private fun initRobotHook() {

        // ⚠️ IMPORTANT :
        // TensorFlow Lite appelle généralement un fragment pour les résultats.
        // On crée un observer simple via callback global.

        val fragment = supportFragmentManager.findFragmentById(
            R.id.fragment_container
        )

        if (fragment is ObjectDetectorFragmentHook) {
            fragment.setDetectionListener { label, score ->
                robotController.onDetection(label, score)
            }
        }
    }

    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            finishAfterTransition()
        } else {
            super.onBackPressed()
        }
    }
}
