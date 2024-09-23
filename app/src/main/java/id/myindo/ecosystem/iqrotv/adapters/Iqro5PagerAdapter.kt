package id.myindo.ecosystem.iqrotv.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.myindo.ecosystem.iqrotv.iqro5.Iqro5Page1Fragment

class Iqro5PagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 1
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> Iqro5Page1Fragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }

}