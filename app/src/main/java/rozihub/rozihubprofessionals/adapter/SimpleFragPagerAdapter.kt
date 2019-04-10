package rozihub.rozihubprofessionals.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import rozihub.rozihubprofessionals.fragment.MyBookingFragment
import rozihub.rozihubprofessionals.fragment.TodayFragment
import rozihub.rozihubprofessionals.fragment.TomorrowFragment

class SimpleFragPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        return if (position == 0)
            TodayFragment()
        else if (position == 1)
            TomorrowFragment()
        else
            MyBookingFragment()

    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        return if (position == 0)
            "Today"
        else if (position == 1)
            "Tomorrow"
        else
            "All"

    }
}