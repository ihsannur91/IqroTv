package id.myindo.ecosystem.iqrotv.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.myindo.ecosystem.iqrotv.alquran.AyatFragment
import id.myindo.ecosystem.iqrotv.data.Surah

class AyatPagerAdapter(fragmentActivity: FragmentActivity, private val surah: Surah) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 1 // Satu halaman untuk setiap surah
    }

    override fun createFragment(position: Int): Fragment {
        return AyatFragment.newInstance(surah)
    }
}
