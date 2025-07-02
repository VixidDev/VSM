package dev.vixid.vsm.utils

import dev.vixid.vsm.config.core.Position

class Rectangle(var x: Float = 0f, var y: Float = 0f, var width: Float = 0f, var height: Float = 0f) {

    constructor(position: Position, width: Float, height: Float) :
            this(position.x.toFloat(), position.y.toFloat(), width, height)

    fun containsPosition(position: Position): Boolean {
        return position.x >= x && position.x <= x + width && position.y >= y && position.y <= height + y
    }

}