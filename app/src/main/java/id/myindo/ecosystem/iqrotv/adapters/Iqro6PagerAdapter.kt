package id.myindo.ecosystem.iqrotv.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.myindo.ecosystem.iqrotv.iqro6.Iqro6Page1Fragment
import id.myindo.ecosystem.iqrotv.iqro6.Iqro6Page2Fragment
import id.myindo.ecosystem.iqrotv.iqro6.Iqro6Page3Fragment
import id.myindo.ecosystem.iqrotv.iqro6.Iqro6Page4Fragment
import id.myindo.ecosystem.iqrotv.iqro6.Iqro6Page5Fragment

class Iqro6PagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> Iqro6Page1Fragment()
            1 -> Iqro6Page2Fragment()
            2 -> Iqro6Page3Fragment()
            3 -> Iqro6Page4Fragment()
            4 -> Iqro6Page5Fragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}