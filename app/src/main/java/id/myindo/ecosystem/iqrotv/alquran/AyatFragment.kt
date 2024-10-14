package id.myindo.ecosystem.iqrotv.alquran

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ayat, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_ayat)
        mediaPlayer = MediaPlayer()

        arguments?.getParcelable<Surah>(ARG_SURAH)?.let { surah ->
            ayatList = surah.ayatList
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            ayatAdapter = AyatAdapter(ayatList) { ayat, position ->
                playAudio(position, recyclerView) // Kirim RecyclerView untuk auto-scroll
            }

            recyclerView.adapter = ayatAdapter
        }

        return view
    }

    private fun playAudio(startPosition: Int, recyclerView: RecyclerView) {
        mediaPlayer.reset()
        currentAyatIndex = startPosition

        // Mulai dari ayat yang dipilih dan mainkan hingga akhir
        playAudioForAyat(currentAyatIndex, recyclerView)
    }

    private fun playAudioForAyat(position: Int, recyclerView: RecyclerView) {
        if (position >= ayatList.size) return

        val ayat = ayatList[position]
        val resId = resources.getIdentifier(ayat.audioFileName, "raw", requireContext().packageName)

        if (resId != 0) {
            try {
                mediaPlayer.reset() // Pastikan MediaPlayer di-reset sebelum diatur ke sumber baru
                val afd = requireContext().resources.openRawResourceFd(resId)
                mediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                afd.close()
                mediaPlayer.prepare()
                mediaPlayer.start()

                // Ubah background ayat yang sedang diputar
                ayatAdapter.setSelectedPosition(position)

                // Auto-scroll ke ayat yang sedang diputar
                recyclerView.smoothScrollToPosition(position)

                // Setelah audio selesai diputar
                mediaPlayer.setOnCompletionListener {
                    // Reset background ayat yang telah diputar
                    ayatAdapter.setSelectedPosition(-1) // Reset semua background
                    currentAyatIndex++ // Berpindah ke ayat berikutnya
                    playAudioForAyat(currentAyatIndex, recyclerView) // Mainkan ayat berikutnya
                }
            } catch (e: Exception) {
                Log.e("AyatFragment", "Error playing audio for ayat: ${ayat.audioFileName}", e)
            }
        } else {
            Log.e("AyatFragment", "File audio tidak ditemukan untuk ayat: ${ayat.audioFileName}")
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}



