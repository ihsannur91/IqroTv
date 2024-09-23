package id.myindo.ecosystem.iqrotv.alquran

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.myindo.ecosystem.iqrotv.R
import id.myindo.ecosystem.iqrotv.databinding.ActivityAlquranBinding

class AlquranActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAlquranBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlquranBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}