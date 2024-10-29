package id.myindo.ecosystem.iqrotv

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.View
import android.Manifest
import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import id.myindo.ecosystem.iqrotv.adapters.ChatAdapter
import id.myindo.ecosystem.iqrotv.databinding.ActivityAiBinding
import java.util.Locale

class AiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAiBinding
    private lateinit var chatAdapter: ChatAdapter
    private val chatList = mutableListOf<String>()
    private lateinit var gestureDetector: GestureDetectorCompat
    private lateinit var inputField: TextInputEditText

    private val dummyData = mapOf(
        "apa hukum sholat" to "Solat adalah wajib berdasarkan QS. Al-Baqarah [2]: 43: وَاَقِيْمُوا الصَّلٰوةَ وَاٰتُوا الزَّكٰوةَ وَارْكَعُوْا مَعَ الرّٰكِعِيْنَ 'Dan dirikanlah salat, tunaikanlah zakat, dan ruku'lah beserta orang-orang yang ruku'. Solat juga menjadi salah satu rukun Islam yang harus dikerjakan.'",
        "waktu sholat" to "Waktu solat telah dijelaskan dalam QS. An-Nisa' [4]: 103: فَاِذَا قَضَيْتُمُ الصَّلٰوةَ فَاذْكُرُوا اللّٰهَ قِيَامًا وَّقُعُوْدًا وَّعَلٰى جُنُوْبِكُمْۚ فَاِذَا اطْمَأْنَنْتُمْ فَاَقِيْمُوا الصَّلٰوةَۚ اِنَّ الصَّلٰوةَ كَانَتْ عَلَى الْمُؤْمِنِيْنَ كِتٰبًا مَّوْقُوْتًا 'Sesungguhnya salat itu adalah kewajiban yang ditentukan waktunya atas orang-orang yang beriman.'"
    )
    private val typingDelay = 50L  // Delay antara karakter saat "mengetik"
    private lateinit var speechRecognizer: SpeechRecognizer
    private val RECORD_AUDIO_REQUEST_CODE = 1
    private lateinit var recognitionDialog: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputField = findViewById<TextInputEditText>(R.id.search_input)
        val searchButton = findViewById<ConstraintLayout>(R.id.enter_button)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val placeholderLayout = findViewById<LinearLayout>(R.id.placeholderLayout)
        val micButton = findViewById<ConstraintLayout>(R.id.micButton)

        chatAdapter = ChatAdapter(chatList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = chatAdapter

        binding.imgBack.setOnClickListener {
            finish()
        }

        // Cek apakah ada input, jika tidak tampilkan placeholder
        searchButton.setOnClickListener {
            val input = inputField.text.toString().lowercase(Locale.getDefault())

            // Sembunyikan placeholder jika user sudah mengisi input
            if (input.isNotEmpty()) {
                placeholderLayout.visibility = View.GONE
                val response = dummyData[input] ?: "Maaf, saya tidak menemukan jawaban untuk pertanyaan Anda."

                // Tambahkan input pengguna ke dalam list
                chatList.add("Anda: $input")
                chatAdapter.notifyDataSetChanged()

                // Tampilkan animasi mengetik untuk jawaban
                simulateTypingResponse(response, recyclerView)

                // Kosongkan input field setelah submit
                inputField.text?.clear()
            }
        }

        gestureDetector = GestureDetectorCompat(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                closeKeyboard()
                return true
            }
        })

        // Set listener pada root view untuk mendeteksi gestur ketukan
        val rootView: View = findViewById(R.id.rootView)
        rootView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            false
        }
        binding.micButton.setOnClickListener {
            btnClick()
        }

    }

    private fun btnClick(){
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_RESULTS,Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Bertanyalah...")
        activityResultLauncher.launch(intent)
    }

    private val activityResultLauncher : ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
            if (result.resultCode == RESULT_OK && result.data != null){
                val resultData = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                val spokenText = resultData?.get(0)
                binding.searchInput.text = spokenText?.let { Editable.Factory.getInstance().newEditable(it) }
            }
        }

    private fun simulateTypingResponse(response: String, recyclerView: RecyclerView) {
        var currentText = ""
        val handler = Handler(Looper.getMainLooper())

        for (i in response.indices) {
            handler.postDelayed({
                currentText += response[i]
                // Perbarui item terakhir dengan efek mengetik
                if (chatList.size > 0) {
                    chatList[chatList.size - 1] = "Pak Ustad Michael : $currentText"
                    chatAdapter.notifyItemChanged(chatList.size - 1)
                }
                // Scroll ke bawah untuk memastikan teks terbaru terlihat
                recyclerView.scrollToPosition(chatList.size - 1)
            }, i * typingDelay)
        }
    }


    private fun closeKeyboard() {
        // Tutup keyboard
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(inputField.windowToken, 0)
    }
}