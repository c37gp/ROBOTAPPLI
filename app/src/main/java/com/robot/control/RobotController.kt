package com.robot.control

import android.content.Context
import com.robot.ai.AIController

class RobotController(private val context: Context? = null) {

    // Modes
    private var autoMode = true

    // Modules
    private var ble: MicrobitBleSender? = null
    private var ai: AIController? = null
    private var server: RobotWebSocketServer? = null

    // -------------------------
    // INIT SYSTEM
    // -------------------------

    fun init() {
        context?.let {
            ble = MicrobitBleSender(it)
        }

        ai = AIController(this)

        server = RobotWebSocketServer(this)
        server?.start()
    }

    // -------------------------
    // MODE CONTROL
    // -------------------------

    fun setAutoMode(enabled: Boolean) {
        autoMode = enabled
    }

    fun isAutoMode(): Boolean = autoMode

    // -------------------------
    // INPUT UNIFIÉ (WEB + IA + MANUEL)
    // -------------------------

    fun onCommand(cmd: String) {
        // Parse detection commands from TensorFlow
        if (cmd.startsWith("detect:")) {
            val parts = cmd.split(":")
            if (parts.size >= 3) {
                val label = parts[1]
                val score = parts[2].toFloatOrNull() ?: 0f
                ai?.onDetection(label, score)
                return
            }
        }

        when (cmd) {

            "auto_on" -> {
                autoMode = true
                return
            }

            "auto_off" -> {
                autoMode = false
                return
            }

            // Commandes directes robot
            "avant",
            "gauche",
            "droite",
            "stop",
            "reculer",
            "dechet" -> {

                if (!autoMode) {
                    send(cmd)
                }
            }
        }
    }

    // -------------------------
    // IA INPUT
    // -------------------------

    fun onAICommand(cmd: String) {
        if (autoMode) {
            send(cmd)
        }
    }

    // -------------------------
    // ENVOI FINAL ROBOT
    // -------------------------

    private fun send(cmd: String) {

        // BLE micro:bit
        ble?.send(cmd)

        // debug log
        println("ROBOT >> $cmd")
    }

    // -------------------------
    // SAFETY
    // -------------------------

    fun emergencyStop() {
        send("stop")
    }

    fun forceRecul() {
        send("reculer")
    }

    // -------------------------
    // CLEANUP
    // -------------------------

    fun destroy() {
        server?.stop()
        ble?.disconnect()
    }
}
