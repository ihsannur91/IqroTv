package id.myindo.ecosystem.iqrotv.iqro1

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer2
import id.myindo.ecosystem.iqrotv.R
import id.myindo.ecosystem.iqrotv.adapters.IqroPagerAdapter
import id.myindo.ecosystem.iqrotv.databinding.ActivityIqroBinding

class IqroActivity : AppCompatActivity(),
    IqroPage1Fragment.OnRowClickListener,
    IqroPage2Fragment.OnRowClickListener,
    IqroPage3Fragment.OnRowClickListener,
    IqroPage4Fragment.OnRowClickListener,
    IqroPage5Fragment.OnRowClickListener {

    private lateinit var binding: ActivityIqroBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: IqroPagerAdapter
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var nextPageButton: ImageButton
    private lateinit var backPageButton: ImageButton
    private lateinit var pageIndicator: TextView
    private lateinit var settingsIcon: ImageView
    private var voiceActor: String = "male"

    private var mode: Int = 1
    private var currentPage: Int = 0
    private var currentRow: Int = 0
    private var prevRow: Int = 0
    private lateinit var popupCardView: CardView
    private lateinit var popupTextView1: TextView
    private lateinit var popupTextView2: TextView
    private lateinit var popupTextView3: TextView

    private var isShowHidePopUp = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIqroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        popupCardView = findViewById(R.id.popupCardView)
        popupTextView1 = findViewById(R.id.popupTextView)
        popupTextView2 = findViewById(R.id.popupTextView2)
        popupTextView3 = findViewById(R.id.popupTextView3)
        viewPager = findViewById(R.id.view_pager)
        nextPageButton = findViewById(R.id.next_page_button)
        backPageButton = findViewById(R.id.back_page_button)
        pageIndicator = findViewById(R.id.page_indicator)
        settingsIcon = findViewById(R.id.settings_icon)

        binding.btnBack.setOnClickListener {
            // Stop and release MediaPlayer if it's playing
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.release()
                mediaPlayer = null
            }
            finish()
        }

        adapter = IqroPagerAdapter(this)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 5

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
                updatePageIndicator(position)
                updateNavigationButtons(position)
            }
        })

//        val bookFlipPageTransformer = BookFlipPageTransformer2().apply {
//            isEnableScale = true
//            scaleAmountPercent = 10f
//        }

        nextPageButton.setOnClickListener {
            val nextItem = viewPager.currentItem + 1
            if (nextItem < adapter.itemCount) {
                viewPager.setCurrentItem(nextItem, true)
            }
        }

        backPageButton.setOnClickListener {
            val previousItem = viewPager.currentItem - 1
            if (previousItem >= 0) {
                viewPager.setCurrentItem(previousItem, true)
            }
        }

        settingsIcon.setOnClickListener {
            showSettingsDialog()
        }

        binding.speakerIcon.setOnClickListener {
            showVoiceDialog()
        }

        // Initial page indicator
        updatePageIndicator(viewPager.currentItem)
        updateNavigationButtons(viewPager.currentItem)

    }


    private fun updatePageIndicator(currentPage: Int) {
        val totalPage = adapter.itemCount
        pageIndicator.text = "${currentPage + 1}/$totalPage"
    }

    private fun updateNavigationButtons(currentPage: Int) {
        when (currentPage) {
            0 -> {
                backPageButton.visibility = View.GONE
                nextPageButton.visibility = View.VISIBLE
            }
            adapter.itemCount - 1 -> {
                backPageButton.visibility = View.VISIBLE
                nextPageButton.visibility = View.GONE
            }
            else -> {
                backPageButton.visibility = View.VISIBLE
                nextPageButton.visibility = View.VISIBLE
            }
        }
    }

    private fun showSettingsDialog() {
        val modes = arrayOf("Mode 1: Play sound on click", "Mode 2: Play and auto-advance")
        AlertDialog.Builder(this)
            .setTitle("Select Mode")
            .setItems(modes) { _, which ->
                mode = which + 1
            }
            .show()
    }

    private fun showVoiceDialog() {
        val voiceActors = arrayOf("Male", "Female")
        var selectedVoiceActor = if (voiceActor == "male") 0 else 1

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Settings")


        // Pilihan voice actor
        builder.setSingleChoiceItems(voiceActors, selectedVoiceActor) { _, which ->
            selectedVoiceActor = which
        }

        builder.setPositiveButton("OK") { _, _ ->
            voiceActor = if (selectedVoiceActor == 0) "male" else "female"
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    override fun onRowClick(row: Int) {

        currentRow = row
        Log.d("onRowClick", "currentRow $currentRow prevRow $prevRow")
        highlightCurrentRow(currentPage, currentRow, prevRow, false)

        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
            mediaPlayer = null
        }

        val ayat = getAyatText(row)
        Log.d("IqroActivity", "value , $ayat")

        if (mode == 1) {
            showPopupWithText(ayat)
            playSoundForRow(currentPage, row)
        } else if (mode == 2) {
            playAndAdvance()
        }
    }

    private fun playSoundForRow(page: Int, row: Int) {
        val soundResId = when (page) {
            0 -> getSoundResourceForPage1(row)
            1 -> getSoundResourceForPage2(row)
            2 -> getSoundResourceForPage3(row)
            3 -> getSoundResourceForPage4(row)
            4 -> getSoundResourceForPage5(row)
            else -> -1
        }

        if (soundResId != -1) {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.release()
            }

            mediaPlayer = MediaPlayer.create(this, soundResId)
            mediaPlayer?.start()

            highlightCurrentRow(page, row, prevRow, true)

            mediaPlayer?.setOnCompletionListener {
                highlightCurrentRow(page, row, prevRow, false)
                showHidePopupWithAnimation(false)

            }
        } else {
            Log.d("IqroActivity", "Sound resource ID not found for page $page and row $row")
        }
    }

    private fun playAndAdvance() {
        val soundResId = when (currentPage) {
            0 -> getSoundResourceForPage1(currentRow)
            1 -> getSoundResourceForPage2(currentRow)
            2 -> getSoundResourceForPage3(currentRow)
            3 -> getSoundResourceForPage4(currentRow)
            4 -> getSoundResourceForPage5(currentRow)
            else -> -1
        }

        if (soundResId != -1) {
            // Hentikan dan rilis MediaPlayer jika sedang diputar
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(this, soundResId)

            // Highlight baris yang sedang diputar
            highlightCurrentRow(currentPage, currentRow, prevRow,true)

            // Mulai pemutaran suara
            mediaPlayer?.start()

            // Ambil teks ayat berdasarkan row saat ini
            val ayat = getAyatText(currentRow)
            showPopupWithText(ayat)

            // Set listener untuk ketika pemutaran selesai
            mediaPlayer?.setOnCompletionListener {
                // Hilangkan highlight dari baris yang selesai diputar
                highlightCurrentRow(currentPage, currentRow, prevRow,false)

                // Increment currentRow untuk pindah ke baris berikutnya
                Log.d("playAndAdvance", "currentRow $currentRow")
                currentRow++

                // Jika currentRow melebihi jumlah baris, pindah ke halaman berikutnya
               /* if (currentRow >= getTotalRowsForPage(currentPage)) {
                    currentRow = 0
                    currentPage++
                    // Pindah ke halaman berikutnya jika masih ada halaman tersisa
                    if (currentPage < adapter.itemCount) {
                        viewPager.setCurrentItem(currentPage, true)
                    }
                }*/

                playAndAdvance()

            }
        } else {
            Log.d("IqroActivity", "Sound resource ID not found for page $currentPage and row $currentRow")
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            showHidePopupWithAnimation(false)
        }
    }

        private fun getSoundResourceForPage1(row: Int): Int {
            return when (row) {
                0 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman1_baris2 else R.raw.halam1_baris1
                1 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman1_baris3 else R.raw.halaman1_baris2
                2 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman1_baris4 else R.raw.halaman1_baris3
                3 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman1_baris5 else R.raw.halaman1_baris4
                4 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman1_baris6 else R.raw.halaman1_baris5
                5 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman1_baris7 else R.raw.halaman1_baris6
                6 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman1_baris8 else R.raw.halaman1_baris7
                else -> -1
            }
        }

    private fun getSoundResourceForPage2(row: Int): Int {
        return when (row) {
            0 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman2_baris2 else R.raw.halaman2_baris1
            1 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman2_baris3 else R.raw.halaman2_baris2
            2 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman2_baris4 else R.raw.halaman2_baris3
            3 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman2_baris5 else R.raw.halaman2_baris4
            4 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman2_baris6 else R.raw.halaman2_baris5
            5 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman2_baris7 else R.raw.halaman2_baris6
            6 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman2_baris8 else R.raw.halaman2_baris7
            else -> -1
        }
    }

    private fun getSoundResourceForPage3(row: Int): Int {
        return when (row) {
            0 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman3_baris1 else R.raw.full_iqro1_halaman3_baris1
            1 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman3_baris2 else R.raw.full_iqro1_halaman3_baris2
            2 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman3_baris3 else R.raw.full_iqro1_halaman3_baris3
            3 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman3_baris4 else R.raw.full_iqro1_halaman3_baris4
            4 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman3_baris5 else R.raw.full_iqro1_halaman3_baris5
            5 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman3_baris6 else R.raw.full_iqro1_halaman3_baris6
            6 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman3_baris7 else R.raw.full_iqro1_halaman3_baris7
            7 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman3_baris8 else R.raw.full_iqro1_halaman3_baris8

            else -> -1
        }
    }

    private fun getSoundResourceForPage4(row: Int): Int {
        return when (row) {
            0 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman4_baris1 else R.raw.full_iqro1_halaman4_baris1
            1 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman4_baris2 else R.raw.full_iqro1_halaman4_baris2
            2 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman4_baris3 else R.raw.full_iqro1_halaman4_baris3
            3 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman4_baris4 else R.raw.full_iqro1_halaman4_baris4
            4 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman4_baris5 else R.raw.full_iqro1_halaman4_baris5
            5 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman4_baris6 else R.raw.full_iqro1_halaman4_baris6
            6 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman4_baris7 else R.raw.full_iqro1_halaman4_baris7
            7 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman4_baris8 else R.raw.full_iqro1_halaman4_baris8

            else -> -1
        }
    }

    private fun getSoundResourceForPage5(row: Int): Int {
        return when (row) {
            0 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman5_baris1 else R.raw.full_iqro1_halaman5_baris1
            1 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman5_baris2 else R.raw.full_iqro1_halaman4_baris2
            2 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman5_baris3 else R.raw.full_iqro1_halaman4_baris3
            3 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman5_baris4 else R.raw.full_iqro1_halaman4_baris4
            4 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman5_baris5 else R.raw.full_iqro1_halaman4_baris5
            5 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman5_baris6 else R.raw.full_iqro1_halaman4_baris6
            6 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman5_baris7 else R.raw.full_iqro1_halaman4_baris7
            7 -> if (voiceActor == "male") R.raw.m_full_iqro1_halaman5_baris8 else R.raw.full_iqro1_halaman4_baris8
            else -> -1
        }
    }


    private fun showPopupWithText(ayat: List<String>) {
        val fragment = supportFragmentManager.findFragmentByTag("f$currentPage")

        // Reset visibility and text before updating
        popupTextView1.isVisible = true
        popupTextView2.isVisible = true
        popupTextView3.isVisible = true
        popupTextView1.text = ""
        popupTextView2.text = ""
        popupTextView3.text = ""

        // Update text sesuai dengan fragment yang sedang aktif
        if (fragment is IqroPage1Fragment || fragment is IqroPage2Fragment || fragment is IqroPage3Fragment || fragment is IqroPage4Fragment || fragment is IqroPage5Fragment) {
            when (ayat.size) {
                1 -> {
                    popupTextView1.text = ""
                    popupTextView2.text = ""
                    popupTextView3.text = ayat[0]
                    popupTextView3.isVisible = true
                }
                2 -> {
                    popupTextView1.text = ayat[1]
                    popupTextView2.text = ayat[0]
                    popupTextView3.isVisible = false
                }
                3 -> {
                    popupTextView1.text = ayat[1]
                    popupTextView2.text = ayat[0]
                    popupTextView3.text = ayat[2]
                    popupTextView3.isVisible = true
                }
            }
        }

        showHidePopupWithAnimation(true)

        Log.d("IqroActivity", "showPopupWithText: ${ayat.joinToString()}")

        // Tambahkan animasi untuk memunculkan popup
        popupCardView.alpha = 0f
        popupCardView.scaleX = 0.8f
        popupCardView.scaleY = 0.8f
        popupCardView.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(300)
            .start()

    }

    private fun showHidePopupWithAnimation(isHide: Boolean) {
        Log.d("IqroActivity", "Popup disembunyikan tanpa animasi")
        popupCardView.alpha = 0f
        popupCardView.scaleX = 0.8f
        popupCardView.scaleY = 0.8f
        popupCardView.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(300)
            .start()
        val isShowHidePopUp = isHide
        popupCardView.isVisible = isShowHidePopUp
        popupTextView1.isVisible = isShowHidePopUp
        popupTextView2.isVisible = isShowHidePopUp
        popupTextView3.isVisible = isShowHidePopUp
    }

    override fun getAyatText(index: Int): List<String> {
        val fragment = supportFragmentManager.findFragmentByTag("f$currentPage")
        return when (fragment) {
            is IqroPage1Fragment -> fragment.getAyatText(index)
            is IqroPage2Fragment -> fragment.getAyatText(index)
            is IqroPage3Fragment -> fragment.getAyatText(index)
            is IqroPage4Fragment -> fragment.getAyatText(index)
            is IqroPage5Fragment -> fragment.getAyatText(index)
            else -> listOf("")
        }
    }

    private fun getTotalRowsForPage(page: Int): Int {
        return when (page) {
            0 -> 10
            1 -> 10
            2 -> 10
            3 -> 10
            4 -> 10
            else -> 0
        }
    }

    private fun highlightCurrentRow(page: Int, row: Int, prevRow: Int, highlight: Boolean) {
        val fragment = supportFragmentManager.findFragmentByTag("f$page")
        when {
            page == 0 && fragment is IqroPage1Fragment -> fragment.highlightRow(row, prevRow, highlight)
            page == 1 && fragment is IqroPage2Fragment -> fragment.highlightRow(row, prevRow, highlight)
            page == 2 && fragment is IqroPage3Fragment -> fragment.highlightRow(row, prevRow, highlight)
            page == 3 && fragment is IqroPage4Fragment -> fragment.highlightRow(row, prevRow, highlight)
            page == 4 && fragment is IqroPage5Fragment -> fragment.highlightRow(row, prevRow, highlight)
        }
    }

}