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

    private var _faces = ArrayList<OffFace>()
    val faces: IntArray?
        get() {
            /*
             * TODO Tesselation needed
             *  or some other format of array
             */
            return null
        }

    val facesNormal: FloatArray?
        get() {
            /*
             * compute cross product of face plane
             */
            return null
        }

    fun loadFrom(uri: Uri, contentResolver: ContentResolver): Boolean {
        val `in`: InputStream? = contentResolver.openInputStream(uri)
        val r = BufferedReader(InputStreamReader(`in`))

        try {
            preloadInit()
            parseOFFHeader(r)
            parseMeshInfo(r)
            parseVertices(r)
            parseFaces(r)
            return true
        } catch (e: Exception) {
            Log.e("OffDecoder", e.toString())
            return false
        }
    }

    private fun preloadInit() {
        _numEdges = 0
        _numVertices = 0
        _numFaces = 0
    }

    private fun removeComment(string: String?): String? {
        string?.split('#')?.apply {
            /*
             * if # found or not, check for content
             * 1. line has comment -> check content before #
             * 1.5. no content before # -> return null
             * 2. blank line -> return null
             * 3. real content -> return content
             */
            return if (this[0].isEmpty()) {
                return null
            } else {
                this[0]
            }
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

    /*
     * One line for each colored vertex:
     * x y z r g b a
     * for vertex 0, 1, ..., vertex_count-1
     */
    private fun parseVertices(r: BufferedReader): Boolean {
        _vertices = FloatArray(_numVertices * 3)
        _vertices?.apply {
            var index = 0
            var line: String?
            while (r.readLine().also { line = it } != null &&
                index < _numVertices) {
                val filtered = removeComment(line)
                filtered?.let {
                    /*
                     * TODO handle optional RGB color
                     */
                    val xyz = it.split(' ')
                    var count = 0
                    for (i in xyz.indices) {
                        val vertex = xyz.get(i)
                        if (vertex.isNotEmpty() && count < 3) {
                            this[index * 3 + count] = vertex.toFloat()
                            count++
                        }
                    }
                    if (count != 3) {
                        throw Exception("invalid vertex count")
                    }
                    index++
                }
            }
            return true
        }
        throw Exception("failed allocate vertices")
    }

    /*
     * One line for each polygonal face:
     * n v1 v2 ... vn r g b a,
     * the number of vertices, and the vertex indices for each face.
     */
    private fun parseFaces(r: BufferedReader): Boolean {
        _faces.clear()
        var index = 0
        var line: String?
        while (r.readLine().also { line = it } != null &&
            index < _numFaces) {
            val filtered = removeComment(line)
            filtered?.let {

                val abc = it.split(' ')
                var entryCount = 0
                var faceVertexCount = 0
                var list: IntArray? = null

                for (i in abc.indices) {
                    val num = abc.get(i)
                    if (num.isNotEmpty()) {
                        when {
                            entryCount == 0 -> {
                                faceVertexCount = num.toIntOrNull()
                                    ?: throw Exception("invalid face vertex count")
                                entryCount++
                                list = IntArray(faceVertexCount)
                            }

                            entryCount in 1 until faceVertexCount + 1 -> {
                                list?.apply {
                                    this[entryCount - 1] = num.toIntOrNull()
                                        ?: throw Exception("invalid face vertex count")
                                    entryCount++
                                }
                            }

                            else -> {
                                /*
                                 * TODO handle optional RGB color
                                 *  TODO handle faces with more than 3 edges
                                 */
                            }
                        }
                    }
                }
                if (entryCount != faceVertexCount + 1) {
                    throw Exception("invalid face entries")
                }
                _faces.add(
                    OffFace(
                        entryCount,
                        list ?: throw Exception("missing face entries")
                    )
                )
                index++
            }
        }
        return true
    }
}