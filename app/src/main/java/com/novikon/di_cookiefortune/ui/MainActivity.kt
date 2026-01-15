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
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.novikon.di_cookiefortune.R

class MainActivity : AppCompatActivity() {
    val analytics = Firebase.analytics
    private val auth: FirebaseAuth = Firebase.auth

    private var mp: MediaPlayer? = null  // Declarar como propiedad de la clase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Sign-in anónimo
        signInAnonymously()

        val imatgeCookie = findViewById<ImageView>(R.id.imageCookie)

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

            // Evento: Cuando el usuario abre la galleta
            val bundle = Bundle().apply {
                putString("action", "open_cookie")
            }
            analytics.logEvent("open_cookie", bundle)

            val intent = Intent(this, PhraseActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signInAnonymously() {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val uid = user?.uid
                    // guardar uid si cal
                    // Aquí puedes guardar el uid en SharedPreferences, base de datos, etc.
                } else {
                    // gestió error
                    task.exception?.printStackTrace()
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        mp?.stop()
        mp?.release()
        mp = null
    }
}