package com.robot.ai

import kotlin.math.abs

class ObstacleAvoidanceEngine {

    private var lastDecision: String = "avant"

    // mémoire sur 3 frames (stabilisation)
    private val history = mutableListOf<Float>()

    fun process(
        label: String,
        score: Float
    ): String {

        if (score < 0.3f) return "avant"

        val l = label.lowercase()

        // 🧱 obstacle prioritaire (person, voiture, mur)
        val isObstacle =
            l.contains("person") ||
            l.contains("car") ||
            l.contains("chair") ||
            l.contains("table") ||
            l.contains("wall")

        if (!isObstacle) return "avant"

        // 📊 estimation pseudo-distance (approximation réaliste)
        val distanceEstimate = estimateDistance(score)

        // 🧠 mémoire pour lisser les décisions
        history.add(distanceEstimate)
        if (history.size > 3) history.removeAt(0)

        val avgDistance = history.average().toFloat()

        return decide(avgDistance)
    }

    // 📏 approximation distance basée sur score
    private fun estimateDistance(score: Float): Float {
        // plus le score est haut → objet probablement proche
        return 1f - score
    }

    // 🧭 décision finale évitement
    private fun decide(distance: Float): String {

        return when {

            // 🚨 trop proche → stop + recul
            distance < 0.25f -> "reculer"

            // ⚠️ proche → tourner
            distance < 0.45f -> if (lastDecision == "gauche") "droite" else "gauche"

            // 🟡 moyen → ralentir
            distance < 0.65f -> "stop"

            // 🟢 libre
            else -> "avant"
        }.also {
            lastDecision = it
        }
    }
}
