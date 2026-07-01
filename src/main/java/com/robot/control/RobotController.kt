package com.robot.control

import android.content.Context
import com.robot.ai.AIController

class RobotController(
    private val ble: MicrobitBleSender
){

    fun forward() = ble.send("F")
    fun left() = ble.send("L")
    fun right() = ble.send("R")
    fun back() = ble.send("B")
    fun stop() = ble.send("S")

    fun followGarbage(){
        ble.send("G")
    }
}
