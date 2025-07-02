package dev.vixid.vsm.config.core

import com.google.gson.annotations.Expose

class Position(x: Int, y: Int) {

    constructor(x: Double, y: Double): this(x.toInt(), y.toInt())

    @Expose
    var x: Int = x

    @Expose
    var y: Int = y

    fun set(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    fun set(x: Double, y: Double) {
        this.x = x.toInt()
        this.y = y.toInt()
    }
}