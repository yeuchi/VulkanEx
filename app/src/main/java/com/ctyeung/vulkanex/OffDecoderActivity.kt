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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uri = getOffUri(getIntent())
        uri?.apply {
            val list = loadOffFile(uri)
            if (list.isNotEmpty()) {
                render()
            }
        }
    }

    private fun getOffUri(intent: Intent): Uri? {
        intent.getExtras()?.getString("off_uri_string")?.let {
            return Uri.parse(it)
        }
        return null
    }

    private fun loadOffFile(uri:Uri):List<String> {
        val `in`: InputStream? = contentResolver.openInputStream(uri)
        val r = BufferedReader(InputStreamReader(`in`))
        var list = mutableListOf<String>()
        var line: String?
        while (r.readLine().also { line = it } != null) {
            line?.let {
                list.add(it)
            }
        }
        return list.toList()
    }

    private fun render() {
        /*
         * pass it to ndk
         */
    }
}