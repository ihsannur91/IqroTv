package id.myindo.ecosystem.iqrotv.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import id.myindo.ecosystem.iqrotv.R
import id.myindo.ecosystem.iqrotv.data.Ayat

class AyatAdapter(
    private val ayatList: List<Ayat>,
    private val onAyatClick: (Ayat, Int) -> Unit,
    private var textSize: Float
) : RecyclerView.Adapter<AyatAdapter.AyatViewHolder>() {

    private var selectedPosition = -1 // Simpan indeks baris yang sedang diputar

    inner class AyatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ayatNumberTextView: TextView = itemView.findViewById(R.id.ayat_number)
        val arabicTextView: TextView = itemView.findViewById(R.id.ayat_arabic)
        val latinTextView: TextView = itemView.findViewById(R.id.ayat_latin)
        val translationTextView: TextView = itemView.findViewById(R.id.ayat_translation)

        fun bind(ayat: Ayat, position: Int) {
            ayatNumberTextView.text = ayat.number
            arabicTextView.text = ayat.arabicText
            latinTextView.text = ayat.latinText
            translationTextView.text = ayat.translation

            // Terapkan ukuran teks sesuai dengan textSize
            arabicTextView.textSize = textSize
            latinTextView.textSize = textSize
            translationTextView.textSize = textSize

            // Ubah background sesuai posisi
            if (position == selectedPosition) {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.green_alabaster))
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.putih_transparan))
            }

            itemView.setOnClickListener {
                onAyatClick(ayat, position)
                // Simpan indeks dari baris yang sedang diputar
                selectedPosition = position
                notifyDataSetChanged() // Refresh tampilan untuk menampilkan perubahan
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AyatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ayat, parent, false)
        return AyatViewHolder(view)
    }

    override fun onBindViewHolder(holder: AyatViewHolder, position: Int) {
        holder.bind(ayatList[position], position)
    }

    override fun getItemCount(): Int = ayatList.size

    // Metode baru untuk mengatur posisi yang sedang dipilih
    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged() // Refresh tampilan untuk menampilkan background yang benar
    }

    // Metode untuk memperbarui ukuran teks
    fun updateTextSize(newTextSize: Float) {
        textSize = newTextSize
        notifyDataSetChanged() // Refresh semua elemen untuk menerapkan ukuran teks baru
    }
}
