package com.robot.control

class RobotController {

    private var autoMode: Boolean = false

    private val server = SimpleCommandServer(this)
    private val microbit = MicrobitSender()

    fun start() {
        server.start()
    }

    fun setAutoMode(enabled: Boolean) {
        autoMode = enabled
    }

    fun onCommand(command: String) {

        when (command) {

            "auto_on" -> setAutoMode(true)
            "auto_off" -> setAutoMode(false)

            "avant",
            "arriere",
            "gauche",
            "droite",
            "stop",
            "dechet" -> {

                if (!autoMode) {
                    microbit.send(command)
                }
            }
        }
    }

    fun onDetection(label: String, confidence: Float) {

        if (!autoMode) return

        val l = label.lowercase()

        if (
            l.contains("bottle") ||
            l.contains("plastic") ||
            l.contains("can") ||
            l.contains("trash") ||
            l.contains("cup")
        ) {
            microbit.send("dechet")
            return
        }

        when {
            confidence < 0.4f -> microbit.send("stop")
            confidence < 0.7f -> microbit.send("gauche")
            else -> microbit.send("avant")
        }
    }
}
