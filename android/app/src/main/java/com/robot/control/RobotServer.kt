package com.robot.control

import org.java_websocket.server.WebSocketServer
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import java.net.InetSocketAddress

class RobotServer(port: Int, private val bleSender: BleSender) : WebSocketServer(InetSocketAddress(port)) {

    private var autoMode = false

    override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {}

    override fun onMessage(conn: WebSocket?, message: String?) {
        message ?: return

        when (message) {
            "auto_on" -> autoMode = true
            "auto_off" -> autoMode = false

            "avant", "arriere", "gauche", "droite", "stop", "dechet" -> {
                if (!autoMode) bleSender.send(message)
            }
        }
    }

    override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {}

    override fun onError(conn: WebSocket?, ex: Exception?) {}

    override fun onStart() {}
}