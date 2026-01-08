package com.novikon.di_cookiefortune.ui

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.novikon.di_cookiefortune.R

class MainActivity : AppCompatActivity() {

    private var mp: MediaPlayer? = null  // Declarar como propiedad de la clase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val imatgeCookie =  findViewById<ImageView>(R.id.imageCookie)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imatgeCookie.setOnClickListener {

            mp = MediaPlayer.create(this, R.raw.cookie)
            mp?.start()
            val vibrator = getSystemService(Vibrator::class.java)
            vibrator?.vibrate(
                VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE)
            )
            val intent = Intent(this, PhraseActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        mp?.stop()
        mp?.release()
        mp = null
    }
}