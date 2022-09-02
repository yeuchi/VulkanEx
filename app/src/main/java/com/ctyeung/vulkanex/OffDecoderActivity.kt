package com.ctyeung.vulkanex

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.androidgamesdk.GameActivity
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class OffDecoderActivity : GameActivity() {

    companion object {
        init {
            System.loadLibrary("vulkanex")
        }
    }

    var off = OffDecoder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uri = getOffUri(getIntent())
        uri?.apply {
            if(off.loadFrom(uri, contentResolver)) {
                off.vertices?.let {
                    render(it)
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

    private fun render(intArray: FloatArray) {
        /*
         * TODO pass it to ndk
         */
    }
}