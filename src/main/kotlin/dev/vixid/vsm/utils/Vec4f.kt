package dev.vixid.vsm.utils

import dev.vixid.vsm.config.core.Position

class Vec4f(
    var x: Float = 0f,
    var y: Float = 0f,
    var z: Float = 0f,
    var w: Float = 0f
) {

    fun containsPosition(position: Position) : Boolean = position.x > x && position.x < z && position.y > y && position.y < w
}