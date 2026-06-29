package com.robot.control

class RobotController {

    private var autoMode = true

    private val microbit = MicrobitSender()

    fun setAutoMode(enabled: Boolean) {
        autoMode = enabled
    }

    fun onCommand(cmd: String) {

        when (cmd) {

            "auto_on" -> autoMode = true
            "auto_off" -> autoMode = false

            else -> {
                if (!autoMode) {
                    microbit.send(cmd)
                }
            }
        }
    }
}
