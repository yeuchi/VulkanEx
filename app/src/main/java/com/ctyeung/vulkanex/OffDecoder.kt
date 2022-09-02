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

    private fun preloadInit() {
        _numEdges = 0
        _numVertices = 0
        _numFaces = 0
    }

    fun loadFrom(uri: Uri, contentResolver: ContentResolver): Boolean {
        val `in`: InputStream? = contentResolver.openInputStream(uri)
        val r = BufferedReader(InputStreamReader(`in`))

        try {
            preloadInit()
            parseOFFHeader(r)
            parseMeshInfo(r)
            parseVertices(r)
        } catch (e: Exception) {
            Log.e("OffDecoder", e.toString())
        }
        return true
    }

    private fun removeComment(string: String?): String? {
        string?.split('#')?.apply {
            return this[0]
        }
        return null
    }

    /*
     * 1st line is OFF (optional)
     */
    private fun parseOFFHeader(r: BufferedReader): Boolean {
        var line: String?
        while (r.readLine().also { line = it } != null) {
            val filtered = removeComment(line)
            return filtered?.contains("OFF") ?: throw Exception("missing OFF header")
        }
        throw Exception("comments only")
    }

    /*
     * 2nd line: the number of vertices, number of faces, and number of edges
     */
    private fun parseMeshInfo(r: BufferedReader): Boolean {
        var line: String?
        while (r.readLine().also { line = it } != null) {
            removeComment(line)?.let {
                val metrics = it.split(' ')
                if (metrics.size == 3) {
                    _numVertices =
                        metrics.get(0).toIntOrNull() ?: throw Exception("missing num verticies")
                    _numFaces = metrics.get(1).toIntOrNull() ?: throw Exception("missing num faces")
                    _numEdges = metrics.get(2).toIntOrNull() ?: throw Exception("missing num edges")
                    return true
                }
            }
        }
        throw Exception("missing metric info")
    }

    private fun parseVertices(r: BufferedReader): Boolean {
        _vertices = FloatArray(_numVertices)
        _vertices?.apply {

            var index = 0
            var line: String?
            while (r.readLine().also { line = it } != null &&
                index < _numVertices) {
                val filtered = removeComment(line)
                filtered?.let {
                    val xyz = it.split(' ')
                    for (i in 0..2) {
                        this[index] = xyz.get(i).toFloat()
                        index++
                    }
                }
            }
            return true
        }
        throw Exception("failed allocation")
    }
}