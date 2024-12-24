package id.myindo.ecosystem.iqrotv.alquran

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.myindo.ecosystem.iqrotv.R
import id.myindo.ecosystem.iqrotv.adapters.AyatAdapter
import id.myindo.ecosystem.iqrotv.data.Ayat
import id.myindo.ecosystem.iqrotv.data.Surah


class AyatFragment : Fragment() {
    private lateinit var mediaPlayer: MediaPlayer
    private var currentAyatIndex: Int = 0
    private lateinit var ayatAdapter: AyatAdapter

    private var ayatList: List<Ayat> = emptyList()
    private var isPlaying: Boolean = false // Menyimpan status pemutaran
    private var dX: Float = 0f
    private var dY: Float = 0f
    private lateinit var playPauseButton: ImageButton
    private lateinit var stopButton: ImageButton
    private lateinit var replayButton: ImageButton
    private lateinit var audioControlLayout: CardView

    private lateinit var recyclerView: RecyclerView


    companion object {
        private const val ARG_SURAH = "surah"

        fun newInstance(surah: Surah): AyatFragment {
            val fragment = AyatFragment()
            val args = Bundle()
            args.putParcelable(ARG_SURAH, surah)
            fragment.arguments = args
            return fragment
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ayat, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_ayat)

        mediaPlayer = MediaPlayer()

        // Inisialisasi tombol dan layout kontrol audio
        playPauseButton = view.findViewById(R.id.button_play_pause)
        stopButton = view.findViewById(R.id.button_stop)
        audioControlLayout = view.findViewById(R.id.audio_control_layout)
        replayButton = view.findViewById(R.id.button_replay)

        arguments?.getParcelable<Surah>(ARG_SURAH)?.let { surah ->
            ayatList = surah.ayatList
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            ayatAdapter = AyatAdapter(ayatList) { ayat, position ->
                currentAyatIndex = position
                playAudio(ayat, position)
            }
            recyclerView.adapter = ayatAdapter
        }

        // Set listener untuk tombol play/pause
        playPauseButton.setOnClickListener {
            if (isPlaying) {
                pauseAudio()
            } else {
                resumeAudio()
            }
        }

        replayButton.setOnClickListener {
            replayAudio()
        }


        // Set listener untuk tombol stop
        stopButton.setOnClickListener {
            stopAudio()
        }

        audioControlLayout.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Aktifkan hardware layer biar gerak lebih mulus
                    view.setLayerType(View.LAYER_TYPE_HARDWARE, null)
                    dX = view.x - motionEvent.rawX
                    dY = view.y - motionEvent.rawY
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    // Hitung posisi baru
                    var newX = motionEvent.rawX + dX
                    var newY = motionEvent.rawY + dY

                    // Mendapatkan ukuran layar
                    val displayMetrics = view.context.resources.displayMetrics
                    val screenWidth = displayMetrics.widthPixels.toFloat()
                    val screenHeight = displayMetrics.heightPixels.toFloat()

                    // Mendapatkan ukuran view
                    val viewWidth = view.width.toFloat()
                    val viewHeight = view.height.toFloat()

                    // Membatasi posisi view biar ga keluar layar
                    newX = newX.coerceIn(0f, screenWidth - viewWidth)
                    newY = newY.coerceIn(0f, screenHeight - viewHeight)

                    view.animate()
                        .x(newX)
                        .y(newY)
                        .setDuration(0)
                        .start()
                    true
                }
                MotionEvent.ACTION_UP -> {
                    // Kembali ke layer default setelah pergerakan selesai
                    view.setLayerType(View.LAYER_TYPE_NONE, null)

                    // Cek apakah ini adalah klik dengan menghitung perbedaan posisi awal dan akhir
                    val clickThreshold = 10 // Threshold untuk mendeteksi klik (sesuaikan sesuai kebutuhan)
                    val isClick = kotlin.math.abs(motionEvent.rawX + dX - view.x) < clickThreshold &&
                            kotlin.math.abs(motionEvent.rawY + dY - view.y) < clickThreshold

                    if (isClick) {
                        view.performClick() // Memanggil performClick jika ini adalah klik
                    }
                    true
                }
                else -> false
            }
        }

        return view
    }

    private fun playAudio(ayat: Ayat, position: Int) {
        mediaPlayer.reset()
        // Ambil resource ID dari string path yang ada di Ayat
        val resId = resources.getIdentifier(ayat.audioFileName, "raw", requireContext().packageName)

        if (resId != 0) {
            val afd = requireContext().resources.openRawResourceFd(resId)
            mediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            afd.close() // Jangan lupa untuk menutup AssetFileDescriptor setelah digunakan
            mediaPlayer.prepare()
            mediaPlayer.start()

            isPlaying = true
            playPauseButton.setImageResource(R.drawable.ic_pause) // Ganti dengan ikon pause

            // Ubah background ayat yang sedang diputar
            ayatAdapter.setSelectedPosition(position)
            // Auto-scroll ke ayat yang sedang diputar
            recyclerView.smoothScrollToPosition(position)

            // Tampilkan kontrol audio saat audio mulai diputar
            audioControlLayout.visibility = View.VISIBLE

            mediaPlayer.setOnCompletionListener {
                isPlaying = false
                playPauseButton.setImageResource(R.drawable.ic_play) // Ganti kembali ke ikon play
                ayatAdapter.setSelectedPosition(-1) // Reset semua background
                currentAyatIndex++ // Berpindah ke ayat berikutnya

                if (currentAyatIndex < ayatList.size) {
                    playAudio(ayatList[currentAyatIndex], currentAyatIndex) // Mainkan ayat berikutnya
                } else {
                    // Audio selesai diputar sampai akhir, sembunyikan kontrol audio
                    audioControlLayout.visibility = View.GONE
                }
            }
        } else {
            Log.e("AyatFragment", "File audio tidak ditemukan untuk ayat: ${ayat.audioFileName}")
        }
    }

    private fun replayAudio() {
        if (isPlaying) {
            mediaPlayer.seekTo(0) // Mengatur posisi audio kembali ke awal
        } else {
            // Jika audio tidak sedang diputar, mulai pemutaran ulang
            playAudio(ayatList[currentAyatIndex], currentAyatIndex)
        }
        playPauseButton.setImageResource(R.drawable.ic_pause) // Set ke ikon pause setelah replay
    }

    private fun pauseAudio() {
        if (isPlaying) {
            mediaPlayer.pause()
            isPlaying = false
            playPauseButton.setImageResource(R.drawable.ic_play) // Ganti ke ikon play
        }
    }

    private fun resumeAudio() {
        if (!isPlaying) {
            mediaPlayer.start()
            isPlaying = true
            playPauseButton.setImageResource(R.drawable.ic_pause) // Ganti ke ikon pause
        }
    }

    private fun stopAudio() {
        if (isPlaying) {
            mediaPlayer.stop()
            isPlaying = false
            playPauseButton.setImageResource(R.drawable.ic_play) // Ganti ke ikon play
            ayatAdapter.setSelectedPosition(-1) // Reset semua background
            currentAyatIndex = 0 // Reset ke ayat awal

            // Sembunyikan kontrol audio saat audio dihentikan
            audioControlLayout.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release() // Pastikan MediaPlayer dirilis untuk mencegah memory leak
    }

}

