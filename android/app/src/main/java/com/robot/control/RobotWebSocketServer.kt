package com.robot.control

import fi.iki.elonen.NanoHTTPD
import org.json.JSONObject

class RobotWebSocketServer(
    private val robotController: RobotController
) : NanoHTTPD(8765) {

    override fun start() {
        super.start(SOCKET_READ_TIMEOUT, false)
    }

    override fun serve(session: IHTTPSession): Response {

        val body = mutableMapOf<String, String>()
        session.parseBody(body)

        val cmd = session.parameters["cmd"]?.firstOrNull() ?: ""

        robotController.onCommand(cmd)

        return newFixedLengthResponse("OK")
    }
}
