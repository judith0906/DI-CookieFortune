package com.novikon.di_cookiefortune.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.novikon.di_cookiefortune.R

class PhraseActivity : AppCompatActivity() {

    private lateinit var txtPhrase: TextView
    private lateinit var btnReload: FloatingActionButton
    private lateinit var btnCopy: FloatingActionButton
    private lateinit var btnShare: FloatingActionButton

    private lateinit var phrases: Array<String>
    private var currentPhrase: String = ""

    val analytics = Firebase.analytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_phrase)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.phrase)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar vistas
        txtPhrase = findViewById(R.id.txt_phrase)
        btnReload = findViewById(R.id.btn_reload)
        btnCopy = findViewById(R.id.btn_copy)
        btnShare = findViewById(R.id.btn_share)

        // Cargar array de frases desde strings.xml
        phrases = resources.getStringArray(R.array.fortune_phrases)

        // Mostrar frase aleatoria inicial
        showRandomPhrase()

        // a) Evento: Cuando el usuario abre la galleta y ve una frase
        val bundle = Bundle().apply {
            putString("phrase", currentPhrase)
        }
        analytics.logEvent("view_phrase", bundle)

        // Listener para botón reload
        btnReload.setOnClickListener {
            showRandomPhrase()

            // d) Evento: Cuando el usuario recarga para ver otra frase
            val reloadBundle = Bundle().apply {
                putString("phrase", currentPhrase)
            }
            analytics.logEvent("reload_phrase", reloadBundle)
        }

        // Listener para botón copiar
        btnCopy.setOnClickListener {
            copyPhraseToClipboard()

            // c) Evento: Cuando el usuario copia la frase al portapapeles
            val copyBundle = Bundle().apply {
                putString(FirebaseAnalytics.Param.METHOD, "copy")
                putString("phrase", currentPhrase)
            }
            analytics.logEvent("copy_phrase", copyBundle)
        }

        // Listener para botón compartir
        btnShare.setOnClickListener {
            sharePhrase()

            // b) Evento: Cuando el usuario comparte la frase
            val shareBundle = Bundle().apply {
                putString(FirebaseAnalytics.Param.METHOD, "share")
                putString("phrase", currentPhrase)
            }
            analytics.logEvent("share_phrase", shareBundle)
        }
    }

    /**
     * Muestra una frase aleatoria que sea diferente a la actual
     */
    private fun showRandomPhrase() {
        if (phrases.isEmpty()) return

        var newPhrase: String
        do {
            newPhrase = phrases.random()
        } while (newPhrase == currentPhrase && phrases.size > 1)

        currentPhrase = newPhrase
        txtPhrase.text = currentPhrase
    }

    /**
     * Copia la frase actual al portapapeles
     */
    private fun copyPhraseToClipboard() {
        if (currentPhrase.isEmpty()) return

        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Frase de la fortuna", currentPhrase)
        clipboard.setPrimaryClip(clip)

        // Mostrar mensaje de confirmación
        Toast.makeText(this, "Frase copiada al portapapeles", Toast.LENGTH_SHORT).show()
    }

    /**
     * Abre el selector de compartir usando Intent implícito
     */
    private fun sharePhrase() {
        if (currentPhrase.isEmpty()) return

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, currentPhrase)

        // Crear el selector de compartir
        val chooser = Intent.createChooser(intent, "Compartir via...")
        startActivity(chooser)
    }
}