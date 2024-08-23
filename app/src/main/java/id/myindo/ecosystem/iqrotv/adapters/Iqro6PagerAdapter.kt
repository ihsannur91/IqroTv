package id.myindo.ecosystem.iqrotv.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.myindo.ecosystem.iqrotv.Iqro6Page1Fragment
import id.myindo.ecosystem.iqrotv.iqro1.IqroPage1Fragment
import id.myindo.ecosystem.iqrotv.iqro1.IqroPage2Fragment
import id.myindo.ecosystem.iqrotv.iqro1.IqroPage3Fragment
import id.myindo.ecosystem.iqrotv.iqro1.IqroPage4Fragment
import id.myindo.ecosystem.iqrotv.iqro1.IqroPage5Fragment

class Iqro6PagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 1
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> Iqro6Page1Fragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}