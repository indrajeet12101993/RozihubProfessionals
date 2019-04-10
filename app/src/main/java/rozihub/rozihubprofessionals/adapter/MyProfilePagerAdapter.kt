package rozihub.rozihubprofessionals.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import rozihub.rozihubprofessionals.fragment.*


class MyProfilePagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm)  {

    override fun getItem(position: Int): Fragment {
        return if (position == 0)
            BasicInfoFragmentShow()
        else if (position == 1)
            BussinessFragmentShow()
        else
            BussinessCatogoryFragmentShow()

    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        return if (position == 0)
            "Basic Info"
        else if (position == 1)
            "Your Business"
        else
            "Business Category"

    }


}