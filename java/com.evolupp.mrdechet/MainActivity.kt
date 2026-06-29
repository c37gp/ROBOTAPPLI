package com.robot.control

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var server: RobotServer
    private lateinit var bleSender: BleSender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ⚠️ ici plus tard : initialiser BLE après pairing micro:bit
        bleSender = BleSender(this)

        server = RobotServer(8765, bleSender)
        server.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        server.stop()
    }
}
