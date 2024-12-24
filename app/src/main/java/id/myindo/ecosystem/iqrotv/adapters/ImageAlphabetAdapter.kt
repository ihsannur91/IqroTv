package id.myindo.ecosystem.iqrotv.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import id.myindo.ecosystem.iqrotv.databinding.ItemImageAlphabetBinding

class ImageAlphabetAdapter(
    private val context: Context,
    private val imageList: List<Pair<Int, String>>,
    private val onImageClick: (String) -> Unit
) : RecyclerView.Adapter<ImageAlphabetAdapter.ImageViewHolder>() {

    private var selectedSize = "small" // Default size adalah "small"

    // Ukuran gambar berdasarkan key size
    private val imageSizes = mapOf(
        "small" to Pair(80, 80),
        "medium" to Pair(120, 120),
        "large" to Pair(160, 160)
    )

    inner class ImageViewHolder(private val binding: ItemImageAlphabetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageResId: Int, size: String) {
            // Atur resource gambar
            binding.imageView.setImageResource(imageResId)

            // Set ukuran gambar berdasarkan key size
            val (width, height) = imageSizes[size] ?: Pair(80, 80)
            binding.imageView.layoutParams = binding.imageView.layoutParams.apply {
                this.width = width
                this.height = height
            }

            // Atur warna background berdasarkan apakah item terpilih
            binding.root.setBackgroundColor(
                if (size == selectedSize)
                    ContextCompat.getColor(context, android.R.color.holo_blue_light)
                else ContextCompat.getColor(context, android.R.color.transparent)
            )

            // Klik gambar
            binding.root.setOnClickListener {
                selectedSize = size // Update ukuran terpilih
                onImageClick(size) // Panggil callback
                notifyDataSetChanged() // Refresh semua item
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageAlphabetBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        // Gunakan ukuran sesuai posisi
        val size = when (position) {
            0 -> "small"
            1 -> "medium"
            2 -> "large"
            else -> "small" // Default jika ada yang tidak sesuai
        }
//        holder.bind(imageList[position], size)
    }

    override fun getItemCount(): Int = imageList.size
}
