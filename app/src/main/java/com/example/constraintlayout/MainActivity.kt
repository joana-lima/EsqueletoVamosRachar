package com.example.constraintlayout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() , TextWatcher, TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private lateinit var edtConta: EditText
    private lateinit var edtQuant: EditText
    private lateinit var textView: TextView
    private var ttsSucess: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // componentes da tela
        edtConta = findViewById<EditText>(R.id.edtConta)
        edtQuant = findViewById<EditText>(R.id.edtQuant)
        textView = findViewById<TextView>(R.id.textView)

        // listeners
        edtConta.addTextChangedListener(this)
        edtQuant.addTextChangedListener(this)

        // Initialize TTS engine
        tts = TextToSpeech(this, this)

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        val valor: Double
        val quant: Double

        if (!edtConta.text.isNullOrBlank() && !edtQuant.text.isNullOrBlank()) {
            valor = edtConta.text.toString().toDouble()
            quant = edtQuant.text.toString().toDouble()
            textView.text = (valor / quant).toString()
        }
        else {
            textView.text = "Vamos Rachar!"
        }
    }

    fun clickFalar(v: View){
        if (tts.isSpeaking) {
            tts.stop()
        }
        if(ttsSucess) {
            var fala: String
            if (!textView.text.toString().isNullOrEmpty() && !textView.text.toString().equals("Vamos Rachar!")) {
                fala = "A parte de cada um é " + textView.text + " reais"
                tts.speak(fala, TextToSpeech.QUEUE_FLUSH, null, null)
            }
            else {
                fala = "Preencha um valor de conta e quantidade de pessoas"
                tts.speak(fala, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }

    fun clickLimpar(v: View) {
        edtConta.setText("")
        edtQuant.setText("")
        textView.text = "Vamos Rachar!"
    }

    fun share(v: View) {
        if (!textView.text.toString().isNullOrEmpty() && !textView.text.toString().equals("Vamos Rachar!")) {
            var shareText: String
            shareText = "Oi, o total da sua parte da conta é " + textView.text + " reais! Sent via Vamos Rachar!"
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    override fun onDestroy() {
            // Release TTS engine resources
            tts.stop()
            tts.shutdown()
            super.onDestroy()
        }

    override fun onInit(status: Int) {
            if (status == TextToSpeech.SUCCESS) {
                // TTS engine is initialized successfully
                tts.language = Locale.getDefault()
                ttsSucess=true
                Log.d("PDM23","Sucesso na Inicialização")
            } else {
                // TTS engine failed to initialize
                Log.e("PDM23", "Failed to initialize TTS engine.")
                ttsSucess=false
            }
        }


}

