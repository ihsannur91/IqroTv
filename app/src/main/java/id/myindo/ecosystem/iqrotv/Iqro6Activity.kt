package id.myindo.ecosystem.iqrotv

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
import androidx.viewpager2.widget.ViewPager2
import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer2
import id.myindo.ecosystem.iqrotv.adapters.Iqro6PagerAdapter
import id.myindo.ecosystem.iqrotv.databinding.ActivityIqro6Binding

class Iqro6Activity : AppCompatActivity(),
    Iqro6Page1Fragment.OnRowClickListener {

    private lateinit var binding: ActivityIqro6Binding
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: Iqro6PagerAdapter
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var nextPageButton: ImageButton
    private lateinit var backPageButton: ImageButton
    private lateinit var pageIndicator: TextView
    private lateinit var settingsIcon: ImageView

    private var mode: Int = 1
    private var currentPage: Int = 0
    private var currentRow: Int = 0
    private lateinit var popupCardView: CardView
    private lateinit var popupTextView1: TextView
    private lateinit var popupTextView2: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIqro6Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("TITLE")
        val overview = intent.getStringExtra("OVERVIEW")
        val imageResource = intent.getIntExtra("IMAGE_RESOURCE", 0)
        popupCardView = findViewById(R.id.popupCardView)
        popupTextView1 = findViewById(R.id.popupTextView)
        popupTextView2 = findViewById(R.id.popupTextView2)
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

        adapter = Iqro6PagerAdapter(this)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
                updatePageIndicator(position)
                updateNavigationButtons(position)
            }
        })

        val bookFlipPageTransformer = BookFlipPageTransformer2().apply {
            isEnableScale = true
            scaleAmountPercent = 10f
        }

        viewPager.setPageTransformer(bookFlipPageTransformer)

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

    override fun onRowClick(row: Int) {
        val (ayat1, ayat2) = getAyatText(row)
        Log.d("iqroActivity", "value , $ayat2 , $ayat1")
        if (mode == 1) {
            showPopupWithText(ayat1, ayat2)
            playSoundForRow(currentPage, row)
        } else if (mode == 2) {
            currentRow = row
            showPopupWithText(ayat1, ayat2)
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
            // Stop and release MediaPlayer if it's playing
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.release()
            }
            mediaPlayer = MediaPlayer.create(this, soundResId)
            mediaPlayer?.start()

            // Highlight the row in the fragment
            highlightCurrentRow(page, row, true)
            mediaPlayer?.setOnCompletionListener {
                // Remove highlight after sound completes
                highlightCurrentRow(page, row, false)
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
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(this, soundResId)
            highlightCurrentRow(currentPage, currentRow, true)
            mediaPlayer?.start()
            mediaPlayer?.setOnCompletionListener {
                highlightCurrentRow(currentPage, currentRow, false)
                currentRow++
                if (currentRow >= getTotalRowsForPage(currentPage)) {
                    currentRow = 0
                    currentPage++
                    if (currentPage < adapter.itemCount) {
                        viewPager.setCurrentItem(currentPage, true)
                    }
                }
                if (currentPage < adapter.itemCount) {
                    playAndAdvance()
                }
            }
        } else {
            Log.d("IqroActivity", "Sound resource ID not found for page $currentPage and row $currentRow")
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    private fun getSoundResourceForPage1(row: Int): Int {
        return when (row) {
            0 -> R.raw.halam1_baris1
            1 -> R.raw.halaman1_baris2
            2 -> R.raw.halaman1_baris3
            3 -> R.raw.halaman1_baris4
            4 -> R.raw.halaman1_baris5
            5 -> R.raw.halaman1_baris6
            6 -> R.raw.halaman1_baris7
            // Add more sounds here
            else -> -1
        }
    }

    private fun getSoundResourceForPage2(row: Int): Int {
        return when (row) {
            0 -> R.raw.halaman2_baris1
            1 -> R.raw.halaman2_baris2
            2 -> R.raw.halaman2_baris3
            3 -> R.raw.halaman2_baris4
            4 -> R.raw.halaman2_baris5
            5 -> R.raw.halaman2_baris6
            6 -> R.raw.halaman2_baris7
            // Add more sounds here
            else -> -1
        }
    }

    private fun getSoundResourceForPage3(row: Int): Int {
        return when (row) {
            0 -> R.raw.full_iqro1_halaman3_baris1
            1 -> R.raw.full_iqro1_halaman3_baris2
            2 -> R.raw.full_iqro1_halaman3_baris3
            3 -> R.raw.full_iqro1_halaman3_baris4
            4 -> R.raw.full_iqro1_halaman3_baris5
            5 -> R.raw.full_iqro1_halaman3_baris6
            6 -> R.raw.full_iqro1_halaman3_baris7
            7 -> R.raw.full_iqro1_halaman3_baris8
            else -> -1
        }
    }

    private fun getSoundResourceForPage4(row: Int): Int {
        return when (row) {
            0 -> R.raw.full_iqro1_halaman4_baris1
            1 -> R.raw.full_iqro1_halaman4_baris2
            2 -> R.raw.full_iqro1_halaman4_baris3
            3 -> R.raw.full_iqro1_halaman4_baris4
            4 -> R.raw.full_iqro1_halaman4_baris5
            5 -> R.raw.full_iqro1_halaman4_baris6
            6 -> R.raw.full_iqro1_halaman4_baris7
            7 -> R.raw.full_iqro1_halaman4_baris8
            else -> -1
        }
    }

    private fun getSoundResourceForPage5(row: Int): Int {
        return when (row) {
            0 -> R.raw.full_iqro1_halaman5_baris1
            1 -> R.raw.full_iqro1_halaman5_baris2
            2 -> R.raw.full_iqro1_halaman5_baris3
            3 -> R.raw.full_iqro1_halaman5_baris4
            4 -> R.raw.full_iqro1_halaman5_baris5
            5 -> R.raw.full_iqro1_halaman5_baris6
            6 -> R.raw.full_iqro1_halaman5_baris7
            7 -> R.raw.full_iqro1_halaman5_baris8
            else -> -1
        }
    }

    private fun showPopupWithText(text1: String, text2: String) {
        popupTextView1.text = text1
        popupTextView2.text = text2
        popupCardView.visibility = View.VISIBLE

        Log.d("IqroActivity", "showPopupWithText true ")

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

        // Hide popup after sound ends
        mediaPlayer?.setOnCompletionListener {
            Log.d("IqroActivity", "Suara selesai diputar, menghilangkan popup...")

            hidePopupWithAnimation()
            highlightCurrentRow(currentPage, currentRow, false)
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    private fun hidePopupWithAnimation() {
        popupCardView.visibility = View.GONE
        popupTextView1.visibility = View.GONE
        popupTextView2.visibility = View.GONE
        Log.d("IqroActivity", "Popup disembunyikan tanpa animasi")
    }

    override fun getAyatText(index: Int): Pair<String, String> {
        val fragment = supportFragmentManager.findFragmentByTag("f$currentPage")
        return if (fragment is Iqro6Page1Fragment) {
            fragment.getAyatText(index)
        } else {
            Pair("", "")
        }
    }

    private fun getTotalRowsForPage(page: Int): Int {
        return when (page) {
            0 -> 10 // Adjust based on the number of rows in page 1
            1 -> 10
            else -> 0
        }
    }

    private fun highlightCurrentRow(page: Int, row: Int, highlight: Boolean) {
        val fragment = supportFragmentManager.findFragmentByTag("f$page")
        when {
            page == 0 && fragment is Iqro6Page1Fragment -> fragment.highlightRow(row, highlight)
        }
    }

}