package com.robot.ai

import com.robot.control.RobotController

class AIController(
    private val robot: RobotController
) {

    private val avoidance = ObstacleAvoidanceEngine()

    fun onDetection(label: String, score: Float) {

        val l = label.lowercase()

        // 🗑 déchets priorité absolue
        if (
            l.contains("bottle") ||
            l.contains("plastic") ||
            l.contains("can") ||
            l.contains("trash")
        ) {
            robot.onCommand("dechet")
            return
        }

        // 🤖 évitement d’obstacles réel
        val command = avoidance.process(label, score)

        robot.onCommand(command)
    }
}
