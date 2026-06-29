package com.robot.control

class RobotController {

    // 🤖 mode : auto = IA contrôle, manuel = téléphone contrôle
    private var autoMode: Boolean = true

    private val microbit = MicrobitSender()

    // -------------------------
    // MODE CONTRÔLE GLOBAL
    // -------------------------

    fun setAutoMode(enabled: Boolean) {
        autoMode = enabled
    }

    fun isAutoMode(): Boolean {
        return autoMode
    }

    // -------------------------
    // POINT D’ENTRÉE UNIQUE
    // -------------------------

    fun onCommand(cmd: String) {

        when (cmd) {

            // 🔁 contrôle mode
            "auto_on" -> {
                autoMode = true
                return
            }

            "auto_off" -> {
                autoMode = false
                return
            }

            // 🤖 commandes robot
            "avant",
            "gauche",
            "droite",
            "stop",
            "reculer",
            "dechet" -> {

                // ⚠️ IA OU MANUEL
                // autoMode = IA autorisée
                // autoMode = false → contrôle manuel uniquement

                if (!autoMode) {
                    microbit.send(cmd)
                }
            }
        }
    }

    // -------------------------
    // INTERFACE IA (AIController)
    // -------------------------

    fun onAICommand(cmd: String) {

        // IA toujours autorisée en mode auto
        if (autoMode) {
            microbit.send(cmd)
        }
    }

    // -------------------------
    // FAILSAFE (sécurité robot)
    // -------------------------

    fun emergencyStop() {
        microbit.send("stop")
    }

    fun forceBackward() {
        microbit.send("reculer")
    }

    // -------------------------
    // DEBUG / TEST
    // -------------------------

    fun debugSend(cmd: String) {
        microbit.send(cmd)
    }
}
