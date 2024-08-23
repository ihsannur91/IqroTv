package id.myindo.ecosystem.iqrotv.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.myindo.ecosystem.iqrotv.iqro1.IqroPage1Fragment
import id.myindo.ecosystem.iqrotv.iqro1.IqroPage2Fragment
import id.myindo.ecosystem.iqrotv.iqro1.IqroPage3Fragment
import id.myindo.ecosystem.iqrotv.iqro1.IqroPage4Fragment
import id.myindo.ecosystem.iqrotv.iqro1.IqroPage5Fragment

class IqroPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> IqroPage1Fragment()
            1 -> IqroPage2Fragment()
            2 -> IqroPage3Fragment()
            3 -> IqroPage4Fragment()
            4 -> IqroPage5Fragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }

}