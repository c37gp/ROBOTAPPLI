package com.robot.control

import java.net.ServerSocket
import kotlin.concurrent.thread

class SimpleCommandServer(private val controller: RobotController) {

    fun start() {

        thread {

            val server = ServerSocket(8765)

            while (true) {

                val client = server.accept()
                val cmd = client.getInputStream().bufferedReader().readLine()

                if (cmd != null) {
                    controller.onCommand(cmd.trim())
                }

                client.close()
            }
        }
    }
}
