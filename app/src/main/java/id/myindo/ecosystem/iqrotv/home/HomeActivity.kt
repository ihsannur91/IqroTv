package id.myindo.ecosystem.iqrotv.home

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.myindo.ecosystem.iqrotv.MainActivity
import id.myindo.ecosystem.iqrotv.R
import id.myindo.ecosystem.iqrotv.alquran.AlquranActivity
import id.myindo.ecosystem.iqrotv.databinding.ActivityHomeBinding
import id.myindo.ecosystem.iqrotv.iqro1.IqroActivity
import jp.wasabeef.blurry.Blurry

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            icAlquran.setOnClickListener {
                val intent = Intent(this@HomeActivity,AlquranActivity::class.java)
                startActivity(intent)
            }
            icIqro.setOnClickListener {
                val intent = Intent(this@HomeActivity,MainActivity::class.java)
                startActivity(intent)
            }

        }

        binding.backgroundImage.post{
            Blurry.with(this)
                .radius(5)
                .sampling(2)
                .async()
                .onto(findViewById(R.id.backgroundImage))
        }

        binding.cardView.setCardBackgroundColor(Color.TRANSPARENT)

    }



}