package com.robot.ai

import com.robot.control.RobotController
import kotlin.math.abs

class AIController(
    private val robot: RobotController
) {

    // 🧠 mémoire légère (stabilisation)
    private var lastCommand: String = "stop"

    fun onDetection(label: String, score: Float) {

        if (score < 0.3f) return

        val l = label.lowercase()

        // 🗑 DÉCHETS PRIORITÉ MAX
        if (
            l.contains("bottle") ||
            l.contains("plastic") ||
            l.contains("can") ||
            l.contains("trash") ||
            l.contains("cup")
        ) {
            send("dechet")
            return
        }

        // 🧭 estimation "pseudo-position" (approximation IA vision)
        val position = estimatePosition(l, score)

        decide(position, score)
    }

    // 📍 estimation simplifiée de position dans l’image
    private fun estimatePosition(label: String, score: Float): Float {

        // simulation intelligente :
        // on suppose que score + type influence proximité/position

        var position = 0.5f // centre par défaut

        // objets “dangereux” = plus centraux
        if (label.contains("person") || label.contains("car")) {
            position = 0.5f
        }

        // objets petits = souvent loin → plus stable
        if (score < 0.5f) {
            position += 0.1f
        }

        // bruit contrôlé
        return position.coerceIn(0f, 1f)
    }

    // 🧠 moteur décisionnel semi-intelligent
    private fun decide(position: Float, score: Float) {

        val command = when {

            // 🚨 trop proche ou incertain
            score < 0.4f -> "stop"

            // ⬅️ objet à gauche
            position < 0.4f -> "droite"

            // ➡️ objet à droite
            position > 0.6f -> "gauche"

            // 🔁 obstacle frontal modéré
            score < 0.7f -> "avant"

            // 🚀 libre
            else -> "avant"
        }

        // 🧠 anti-spam (évite vibration robot)
        if (command != lastCommand) {
            send(command)
            lastCommand = command
        }
    }

    private fun send(cmd: String) {
        robot.onCommand(cmd)
    }
}
        }
    }
}
