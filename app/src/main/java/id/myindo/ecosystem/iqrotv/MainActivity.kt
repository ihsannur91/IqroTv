package id.myindo.ecosystem.iqrotv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import id.myindo.ecosystem.iqrotv.databinding.ActivityMainBinding

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
        val intent = Intent(this, IqroActivity::class.java)
        intent.putExtra("TITLE", data.title)
        intent.putExtra("OVERVIEW", data.overview)
        val resourceId = resources.getIdentifier(data.backdrop_path, "drawable", packageName)
        intent.putExtra("IMAGE_RESOURCE", resourceId)
        startActivity(intent)
    }

}