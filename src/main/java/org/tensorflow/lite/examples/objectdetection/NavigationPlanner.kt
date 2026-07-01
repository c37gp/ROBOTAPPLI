package org.tensorflow.lite.examples.objectdetection

class NavigationPlanner {

    fun decide(scene: Map<String, Boolean>): String {

        val left = scene["LEFT"] == true
        val center = scene["CENTER"] == true
        val right = scene["RIGHT"] == true

        return when {

            center && !left && !right -> "STOP_AND_REPLAN"

            center && left && !right -> "RIGHT"

            center && right && !left -> "LEFT"

            !center -> "FORWARD"

            left && right -> "FORWARD"

            else -> "STOP"
        }
    }
}
