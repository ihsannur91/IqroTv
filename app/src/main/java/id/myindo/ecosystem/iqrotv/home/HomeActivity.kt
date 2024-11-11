package id.myindo.ecosystem.iqrotv.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import id.myindo.ecosystem.iqrotv.search.AiActivity
import id.myindo.ecosystem.iqrotv.iqro1.MainActivity
import id.myindo.ecosystem.iqrotv.R
import id.myindo.ecosystem.iqrotv.adapters.HomeAdapter
import id.myindo.ecosystem.iqrotv.alquran.AlquranActivity
import id.myindo.ecosystem.iqrotv.data.MenuItem
import id.myindo.ecosystem.iqrotv.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var recyclerView: RecyclerView
    private lateinit var menuAdapter: HomeAdapter
    private val menuList = listOf(
        MenuItem("Al-Quran", "+5000 Channels", R.drawable.alquran6),
        MenuItem("Iqro", "+1200 Series", R.drawable.iqro4),
        MenuItem("Pencarian Ayat", "+500 Stations", R.drawable.ic_search)
    )
    private lateinit var cardView: CardView
    private var dX: Float = 0f
    private var dY: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        menuAdapter = HomeAdapter(menuList) { menuItem ->
            onMenuItemClick(menuItem)
        }

        recyclerView.adapter = menuAdapter

//        viewPager = findViewById(R.id.viewPager)
//        val menuItems = listOf(
//            MenuItem("Alquran", "Pembelajaran Alquran", R.drawable.ic_alquran, ""),
//            MenuItem("Iqra", "Pembelajaran Iqra", R.drawable.iqra1, ""),
//            MenuItem("Search Engine", "Pencarian pintar", R.drawable.img_search_book, "")
//        )
//        viewPager.adapter = HomeAdapter(menuItems)
//
//        // Transformer untuk efek zoom-out
//        val transformer = CompositePageTransformer()
//        transformer.addTransformer { page, position ->
//            val scale = 0.85f + (1 - abs(position)) * 0.15f
//            page.scaleY = scale
//            page.scaleX = scale
//            page.translationX = -20 * abs(position)
//        }
//        viewPager.setPageTransformer(ZoomOutPageTransformer())
//

//        binding.backgroundImage.post{
//            Blurry.with(this)
//                .radius(4)
//                .sampling(1)
//                .async()
//                .onto(findViewById(R.id.backgroundImage))
//        }

//        binding.cardView.setCardBackgroundColor(Color.TRANSPARENT)

    }

    private fun onMenuItemClick(menuItem: MenuItem) {
        val targetActivity = when (menuItem.title) {
            "Al-Quran" -> AlquranActivity::class.java
            "Iqro" -> MainActivity::class.java
            "Pencarian Ayat" -> AiActivity::class.java
            else -> null
        }

        targetActivity?.let {
            val intent = Intent(this, it)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    inner class ZoomOutPageTransformer : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            val scaleFactor = Math.max(0.85f, 1 - Math.abs(position))
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor
            view.alpha = 0.5f + (scaleFactor - 0.85f) / (1 - 0.85f) * (1 - 0.5f)
            view.translationX = view.width * -position * 0.25f
        }
    }



}