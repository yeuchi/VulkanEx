package com.ctyeung.vulkanex

/*
 * Same thing as a RectF
 * just being more explicit
 */
data class MeshBound(
    var minX: Float = 0f,
    var maxX: Float = 0f,
    var minY: Float = 0f,
    var maxY: Float = 0f,
    var minZ: Float = 0f,
    var maxZ: Float = 0f
) {

    fun collect(x: Float, y: Float, z: Float) {
        if (x < minX) {
            minX = x
        } else if (x > maxX) {
            maxX = x
        }

        if (y < minY) {
            minY = y
        } else if (y > maxY) {
            maxY = y
        }

        if (z < minZ) {
            minZ = z
        } else if (z > maxZ) {
            maxZ = z
        }
    }

    fun scale(x: Float, y: Float, z: Float): List<Float> {
        if (hasBound()) {
            // scale vertices range between 0 - 2
            // offset range -1 to 1

            val scaledX = calculateScale( x,
                0 - minX,           // baseOffset
                maxX - minX)        // dataRange

            val scaledY = calculateScale( y,
                0 - minY,           // baseOffset
                maxY - minY)        // dataRange

            val scaledZ = calculateScale( z,
                0 - minZ,           // baseOffset
                maxZ - minZ)        // dataRange

            return listOf(scaledX, scaledY, scaledZ)
        }
        return emptyList()
    }

    private fun calculateScale(n: Float, baseOffset: Float, dataRange: Float): Float {
        val destRange = 2
        val destOffset = 1
        val scaled = (n + baseOffset) * destRange / dataRange - destOffset
        return scaled
    }

    fun hasBound(): Boolean {
        return !(minX == maxX && minY == maxY && minZ == maxZ)
    }
}