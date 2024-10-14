package id.myindo.ecosystem.iqrotv.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import id.myindo.ecosystem.iqrotv.R
import id.myindo.ecosystem.iqrotv.data.Surah

class SurahAdapter(private val surahList: List<Surah>, private val onSurahClick: (Surah) -> Unit) :
    RecyclerView.Adapter<SurahAdapter.SurahViewHolder>() {

    private var selectedPosition = 0 // Default surah pertama terpilih

    inner class SurahViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val surahNameTextView: TextView = itemView.findViewById(R.id.tv_surah)
        private val surahAyatCountTextView: TextView = itemView.findViewById(R.id.surah_count)
        private val surahDescTextView: TextView = itemView.findViewById(R.id.tv_desc_surah)
        private val cardView: View = itemView.findViewById(R.id.cv_surah)

        fun bind(surah: Surah, isSelected: Boolean) {
            surahNameTextView.text = surah.name
            surahDescTextView.text = surah.desc
            surahAyatCountTextView.text = surah.ayatCount.toString()

            // Ubah background cardView jika item dipilih
            if (isSelected) {
                cardView.setBackgroundResource(R.color.green_alabaster)
            } else {
                cardView.setBackgroundResource(R.color.putih_transparan)
            }

            // Set click listener untuk item
            cardView.setOnClickListener {
                onSurahClick(surah)
                notifyItemChanged(selectedPosition) // Update item yang sebelumnya terpilih
                selectedPosition = adapterPosition // Simpan posisi item yang baru dipilih
                notifyItemChanged(selectedPosition) // Update item yang baru dipilih
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurahViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_surah, parent, false)
        return SurahViewHolder(view)
    }

    override fun onBindViewHolder(holder: SurahViewHolder, position: Int) {
        holder.bind(surahList[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = surahList.size
}



