package com.ctyeung.vulkanex

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.util.Log.DEBUG
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception

class OffDecoder {
    private var _numVertices: Int = 0
    val numVertices: Int
        get() {
            return _numVertices
        }

    private var _numFaces: Int = 0
    val numFaces: Int
        get() {
            return _numFaces
        }

    private var _numEdges: Int = 0
    val numEdges: Int
        get() {
            return _numEdges
        }

    private var _vertices: FloatArray? = null

    val vertices: FloatArray?
        get() {
            return _vertices
        }

    fun loadFrom(uri: Uri, contentResolver: ContentResolver): Boolean {
        val `in`: InputStream? = contentResolver.openInputStream(uri)
        val r = BufferedReader(InputStreamReader(`in`))

        // 1st line optional OFF
        if (!validateOFFHeader(r)) {
            return false
        }
        if (!parseMeshInfo(r)) {
            return false
        }
        if (!parseVertices(r)) {
            return false
        }

        /*
         * TODO parse face, color, etc
         */
        return true
    }

    private fun removeComment(string: String): String {
        val list = string.split('#')
        return list.get(0)
    }

    /*
     * 1st line is OFF (optional)
     */
    private fun validateOFFHeader(r: BufferedReader): Boolean {
        val line1 = r.readLine()
        return if (line1.contains("OFF")) {
            true
        } else {
            false
        }
    }

    /*
     * 2nd line: the number of vertices, number of faces, and number of edges
     */
    private fun parseMeshInfo(r: BufferedReader): Boolean {
        val line2 = r.readLine()
        val metrics = line2.split(' ')
        if (metrics.size == 3) {
            _numVertices = metrics.get(0).toIntOrNull() ?: return false
            _numFaces = metrics.get(1).toIntOrNull() ?: return false
            _numEdges = metrics.get(2).toIntOrNull() ?: return false
            return true
        }
        return false
    }

    private fun parseVertices(r: BufferedReader): Boolean {
        try {
            _vertices = FloatArray(_numVertices + 1)
            _vertices?.apply {

                var index = 0
                var line: String?
                while (r.readLine().also { line = it } != null &&
                    index < _numVertices) {
                    line?.let {
                        /*
                         * TODO Filter comment #
                         */
                        val xyz = it.split(' ')
                        for (i in 0..2) {
                            this[index++] = xyz.get(i).toFloat()
                        }
                    }
                }
                return true
            }
        } catch (e: Exception) {
            Log.e("OffDecoder", e.toString())
        }
        return false
    }
}