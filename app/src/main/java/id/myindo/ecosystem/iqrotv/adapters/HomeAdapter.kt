package id.myindo.ecosystem.iqrotv.adapters

import android.content.ClipData.Item
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import id.myindo.ecosystem.iqrotv.AiActivity
import id.myindo.ecosystem.iqrotv.MainActivity
import id.myindo.ecosystem.iqrotv.R
import id.myindo.ecosystem.iqrotv.alquran.AlquranActivity
import id.myindo.ecosystem.iqrotv.data.MenuItem

class HomeAdapter(
    private val menuList: List<MenuItem>,
    private val onItemClick: (MenuItem) -> Unit
) : RecyclerView.Adapter<HomeAdapter.MenuViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu_home, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = menuList[position]
        holder.bind(item)

        // Highlight item yang dipilih
        holder.itemView.isSelected = (selectedPosition == position)

        // Set click listener
        holder.itemView.setOnClickListener {
            // Update posisi yang dipilih
            val previousPosition = selectedPosition
            selectedPosition = holder.adapterPosition

            // Notify perubahan untuk highlight
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)

            // Pindah activity atau halaman
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = menuList.size

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        private val itemTitle: TextView = view.findViewById(R.id.card_title)
        private val itemSubtitle: TextView = view.findViewById(R.id.card_subtitle)
        private val itemIcon: ImageView = view.findViewById(R.id.card_image)
//        private val statusBadge: TextView = view.findViewById(R.id.card_update)

        fun bind(menuItem: MenuItem) {
//            itemTitle.text = menuItem.title
            itemSubtitle.text = menuItem.title
            itemIcon.setImageResource(menuItem.iconRes)

        }
    }
}

