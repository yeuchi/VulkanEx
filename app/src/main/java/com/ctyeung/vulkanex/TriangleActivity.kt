package com.ctyeung.vulkanex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.androidgamesdk.GameActivity

class TriangleActivity : GameActivity() {
    companion object {
        init {
            System.loadLibrary("vulkanex")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}