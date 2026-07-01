class SceneAnalyzer {

    data class Obj(
        val label: String,
        val x: Float,
        val y: Float,
        val w: Float,
        val h: Float,
        val score: Float
    )

    enum class Action {
        FORWARD, LEFT, RIGHT, BACK, STOP, FOLLOW_GARBAGE
    }

    fun decide(objects: List<Obj>): Action {

        var centerObstacle = false
        var leftBlocked = false
        var rightBlocked = false

        var garbageCenter = false

        for (o in objects) {

            val cx = o.x + o.w/2

            val isObstacle = o.label in listOf("person","chair","car","wall")
            val isGarbage = o.label in listOf("bottle","cup","can")

            when {
                cx < 0.33 -> if(isObstacle) leftBlocked = true
                cx > 0.66 -> if(isObstacle) rightBlocked = true
                else -> {
                    if(isObstacle) centerObstacle = true
                    if(isGarbage) garbageCenter = true
                }
            }
        }

        if (garbageCenter) return Action.FOLLOW_GARBAGE

        if (centerObstacle) {
            return when {
                !rightBlocked -> Action.RIGHT
                !leftBlocked -> Action.LEFT
                else -> Action.BACK
            }
        }

        return Action.FORWARD
    }
}
