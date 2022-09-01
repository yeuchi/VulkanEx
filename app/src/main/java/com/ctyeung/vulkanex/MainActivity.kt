package com.ctyeung.vulkanex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.ctyeung.vulkanex.databinding.ActivityMainBinding
import com.google.androidgamesdk.GameActivity

class MainActivity : AppCompatActivity() {


//    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Example of a call to a native method
//        binding.sampleText.text = stringFromJNI()

        findViewById<Button>(R.id.btn_load)?.apply {
            setOnClickListener {
                val intent = Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT)

                /*
                 * https://stackoverflow.com/questions/49697630/open-file-choose-in-android-app-using-kotlin
                 */
                startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            data?.dataString?.apply {
                val intent = Intent(applicationContext, OffDecoderActivity::class.java)
                intent.putExtra("off_uri_string", this)
                startActivity(intent)
            }
        }
    }
}