package com.ctyeung.vulkanex

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.google.androidgamesdk.GameActivity
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception

class OffDecoderActivity : GameActivity() {

    companion object {
        init {
            System.loadLibrary("vulkanex")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uri = getOffUri(getIntent())
        uri?.apply {
            OffDecoder()?.apply {
                if (loadFrom(uri, contentResolver)) {
                    if (numVertices > 0 &&
                        numFaces > 0
                    ) {
                        render(this)
                    }
                }
            }
        }
    }

    private fun getOffUri(intent: Intent): Uri? {
        intent.getExtras()?.getString("off_uri_string")?.let {
            return Uri.parse(it)
        }
        return null
    }

    private fun render(off: OffDecoder) {
        off.apply {
            try {
                loadJNI(
                    numVertices,
                    vertices ?: throw Exception("Missing vertices"),
                    numFaces,
                    faces ?: throw Exception("Missing faces")
                )
            } catch (ex: Exception) {
                Log.e("OffDecoder", ex.toString())
            }
        }
    }

    external fun loadJNI(
        numOfVerticies: Int,
        vertices: FloatArray,
        numOfFaces: Int,
        faces: IntArray
    )
}