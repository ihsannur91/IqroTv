package id.myindo.ecosystem.iqrotv

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import id.myindo.ecosystem.iqrotv.databinding.ActivityMainBinding
import id.myindo.ecosystem.iqrotv.iqro1.IqroActivity

class MainActivity : FragmentActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var listFragment: ListFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listFragment = ListFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.list_fragment, listFragment)
        transaction.commit()

        listFragment.setOnContentSelectedListener {
            updateBanner(it)
        }

        listFragment.setOnContentClickedListener {
            openIqroActivity(it)
            Log.d("MainActivity", "onCreate: $it")
        }

        listFragment

    }

    private fun updateBanner(data: DataModel.Result.Detail) {
        binding.title.text = data.title
        binding.description.text = data.overview

        val resourceId = resources.getIdentifier(data.backdrop_path, "drawable", packageName)
        binding.imgBanner.setImageResource(resourceId)
    }

    private fun openIqroActivity(data: DataModel.Result.Detail) {
        val intent = when (data.title) {
            "IQRO 1" -> Intent(this, IqroActivity::class.java)
            "IQRO 2" -> Intent(this, IqroActivity::class.java)
            "IQRO 3" -> Intent(this, IqroActivity::class.java)
            "IQRO 4" -> Intent(this, IqroActivity::class.java)
            "IQRO 5" -> Intent(this, IqroActivity::class.java)
            "IQRO 6" -> Intent(this, Iqro6Activity::class.java)
            else -> null
        }

        intent?.let {
            it.putExtra("TITLE", data.title)
            it.putExtra("OVERVIEW", data.overview)
            val resourceId = resources.getIdentifier(data.backdrop_path, "drawable", packageName)
            it.putExtra("IMAGE_RESOURCE", resourceId)
            startActivity(it)
        }
    }

}